namespace TasksController;

using Microsoft.AspNetCore.Mvc;

[Route("api/task/")]
[ApiController]
public class TasksController : ControllerBase
{
    // public TasksController()  db in future
    // {
    //     
    // }

    [HttpGet]
    public IActionResult GetAll()
    {
        return Ok("All tasks");
    }
    
    [HttpGet("{id}")]
    public IActionResult GetTask([FromRoute] int id)
    {
        return Ok($"Task with id: {id}");
    }
    
    [HttpGet ("list/{listId}")]
    public IActionResult GetTaskList([FromRoute] int listId)
    {
        return Ok($"Task list with ID: {listId}");
    }
}