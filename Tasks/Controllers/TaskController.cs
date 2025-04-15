using MediatR;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Threading.Tasks;
using Tasks.Models;
using Tasks.cqrs.Queries;

namespace Tasks.Controllers
{
    [Route("api/task/")]
    [ApiController]
    public class TasksController : ControllerBase
    {
        private readonly IMediator _mediator;

        public TasksController(IMediator mediator)
        {
            _mediator = mediator;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<TaskModel>>> GetTasks()
        {
            var tasks = await _mediator.Send(new GetTasksQuery());
            
            if (tasks == null || tasks.Count == 0)
            {
                return NotFound("No tasks found.");
            }

            return Ok(tasks);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<TaskModel>> GetTaskById([FromRoute] int id)
        {
            var task = await _mediator.Send(new GetTaskByIdQuery(id));
            
            if (task == null)
            {
                return NotFound($"Task with ID {id} not found.");
            }

            return Ok(task);
        }

        [HttpPost("create")]
        public async Task<IActionResult> CreateTask([FromBody] string title)
        {
            if (string.IsNullOrEmpty(title))
            {
                return BadRequest("Task name is required.");
            }
            
            var newTaskId = await _mediator.Send(new CreateTaskQuery(title));
            
            return CreatedAtAction(nameof(GetTaskById), new { id = newTaskId }, newTaskId);
        }
        
        [HttpDelete("delete")]
        public async Task<IActionResult> DeleteTask([FromQuery] string title)
        {
            if (string.IsNullOrEmpty(title))
            {
                return BadRequest("Task title is required.");
            }
            
            var result = await _mediator.Send(new DeleteTaskQuery(title));
            
            if (!result)
            {
                return NotFound($"Task with title '{title}' not found.");
            }

            return NoContent();
        }
    }
}