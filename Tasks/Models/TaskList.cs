namespace Tasks.Models;

public class TaskList
{
    public int Id { get; set; }
    public string Name { get; set; } = string.Empty;
    public List<TaskModel> Tasks { get; set; } = new List<TaskModel>();
}
