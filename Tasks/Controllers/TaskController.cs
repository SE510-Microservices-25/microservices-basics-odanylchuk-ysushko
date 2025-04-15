using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Tasks.Data;
using Tasks.Models;

namespace Tasks.Controllers;

[Route("api/task/")]
[ApiController]
public class TasksController : ControllerBase
{
    private readonly AppDbContext _context;

    public TasksController(AppDbContext context)
    {
        _context = context;
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<TaskModel>>> GetTasks()
    {
        var tasks = await _context.TaskModel.ToListAsync();
    
        if (tasks == null || tasks.Count == 0)
        {
            return NotFound("No tasks found.");
        }

        return tasks;
    }
    
    [HttpGet("{id}")]
    public async Task<ActionResult<TaskModel>> GetTaskById([FromRoute] int id)
    {
        var task = await _context.TaskModel.FindAsync(id);
    
        if (task == null)
        {
            return NotFound($"Task with ID {id} not found.");
        }

        return task;
    }
    
    [HttpPost("create")]
    public async Task<IActionResult> CreateTask([FromBody] string title)
    {
        if (string.IsNullOrEmpty(title))
        {
            return BadRequest("Task name is required.");
        }
        
        var task = new TaskModel
        {
            title = title,
        };
        _context.TaskModel.Add(task);
        await _context.SaveChangesAsync();
        
        return Ok(task);
    }

}