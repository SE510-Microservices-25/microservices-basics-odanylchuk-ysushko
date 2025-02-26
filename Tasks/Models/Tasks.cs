namespace  Tasks.Models;
public class TaskModel
{
    public int id { get; set; }
    public string title { get; set; } = string.Empty;
    public bool is_completed { get; set; }
}
