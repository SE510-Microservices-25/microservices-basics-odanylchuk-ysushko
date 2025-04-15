using System;

namespace Tasks.OrderService.Outbox;

public class OutboxMessage
{
    public Guid Id { get; set; } = Guid.NewGuid();
    public string Type { get; set; } = string.Empty;  // Event type (e.g., fully qualified name)
    public string Payload { get; set; } = string.Empty; // Serialized JSON of the event
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    public bool Processed { get; set; } = false;        // Whether the event has been published
}

