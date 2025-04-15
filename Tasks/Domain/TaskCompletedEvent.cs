namespace Tasks.Domain;

public class TaskCompletedEvent
{
    public int TaskId { get; set; }
    public string Title { get; set; } = string.Empty;
}