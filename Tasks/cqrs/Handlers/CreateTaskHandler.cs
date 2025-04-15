using MediatR;
using Tasks.cqrs.Queries;
using Tasks.Data;
using Tasks.Models;

namespace Tasks.cqrs.Handlers;

public class CreateTaskHandler : IRequestHandler<CreateTaskQuery, int>
{
    private readonly AppDbContext _context;

    public CreateTaskHandler(AppDbContext context)
    {
        _context = context;
    }

    public async Task<int> Handle(CreateTaskQuery request, CancellationToken cancellationToken)
    {
        var task = new TaskModel
        {
            title = request.Title,
            is_completed = false
        };

        _context.TaskModel.Add(task);
        await _context.SaveChangesAsync(cancellationToken);

        return task.id;
    }
}