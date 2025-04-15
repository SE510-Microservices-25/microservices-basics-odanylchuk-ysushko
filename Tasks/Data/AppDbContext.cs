using System.Text.Json;
using Microsoft.EntityFrameworkCore;
using Tasks.Models;
using Tasks.OrderService.Outbox;
using System.Collections.Generic;
using Tasks.Domain;

namespace Tasks.Data
{
    public class AppDbContext : DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

        public DbSet<TaskModel> TaskModel { get; set; }
        
        public DbSet<OutboxMessage> OutboxMessages { get; set; }
        
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<TaskModel>()
                .ToTable("TaskModel", "tasks");
            
            modelBuilder.Entity<OutboxMessage>()
                .ToTable("OutboxMessage", "tasks");
            
            base.OnModelCreating(modelBuilder);
        }

        public override int SaveChanges()
        {
            ProcessOutboxMessages();
            return base.SaveChanges();
        }

        public override async Task<int> SaveChangesAsync(CancellationToken cancellationToken = default)
        {
            ProcessOutboxMessages();
            return await base.SaveChangesAsync(cancellationToken);
        }
        
        private void ProcessOutboxMessages()
        {
            var domainEvents = ChangeTracker.Entries<IHasDomainEvents>()
                .SelectMany(e => e.Entity.DomainEvents)
                .ToList();

            foreach (var domainEvent in domainEvents)
            {
                var outboxMessage = new OutboxMessage
                {
                    Type = domainEvent.GetType().FullName!,
                    Payload = JsonSerializer.Serialize(domainEvent),
                    CreatedAt = DateTime.UtcNow,
                    Processed = false
                };

                OutboxMessages.Add(outboxMessage);
            }
        }
    }
}