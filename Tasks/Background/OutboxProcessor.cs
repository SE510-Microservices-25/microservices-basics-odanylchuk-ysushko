using MassTransit;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using System.Text.Json;
using Tasks.Data;
using Tasks.OrderService.Outbox;

namespace Tasks.Background
{
    public class OutboxProcessor : BackgroundService
    {
        private readonly IServiceProvider _serviceProvider;
        private readonly ILogger<OutboxProcessor> _logger;

        public OutboxProcessor(IServiceProvider serviceProvider, ILogger<OutboxProcessor> logger)
        {
            _serviceProvider = serviceProvider;
            _logger = logger;
        }

        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            while (!stoppingToken.IsCancellationRequested)
            {
                using (var scope = _serviceProvider.CreateScope())
                {
                    var dbContext = scope.ServiceProvider.GetRequiredService<AppDbContext>();
                    
                    var publishEndpoint = scope.ServiceProvider.GetRequiredService<IPublishEndpoint>();
                    
                    var outboxMessages = dbContext.OutboxMessages
                        .Where(m => !m.Processed)
                        .ToList();

                    foreach (var message in outboxMessages)
                    {
                        var eventType = Type.GetType(message.Type);
                        if (eventType == null)
                        {
                            _logger.LogWarning("Could not resolve type for message {MessageId}", message.Id);
                            continue;
                        }

                        var eventData = JsonSerializer.Deserialize(message.Payload, eventType);
                        if (eventData == null)
                        {
                            _logger.LogWarning("Deserialization failed for message {MessageId}", message.Id);
                            continue;
                        }

                        await publishEndpoint.Publish(eventData, stoppingToken);
                        _logger.LogInformation("Published message {MessageId}", message.Id);

                        message.Processed = true;
                    }

                    await dbContext.SaveChangesAsync(stoppingToken);
                }

                await Task.Delay(5000, stoppingToken); // Delay before next check
            }
        }
    }
}
