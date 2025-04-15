using MediatR;
using Microsoft.EntityFrameworkCore;
using System.Threading;
using System.Threading.Tasks;
using Tasks.cqrs.Queries;
using Tasks.Data;
using Tasks.Models;


namespace Tasks.cqrs.Handlers;

public class DeleteTaskHandler : IRequestHandler<DeleteTaskQuery, bool>
{
    private readonly AppDbContext _context;

    public DeleteTaskHandler(AppDbContext context)
    {
        _context = context;
    }

    public async Task<bool> Handle(DeleteTaskQuery request, CancellationToken cancellationToken)
    {
        var task = await _context.TaskModel.FirstOrDefaultAsync(t => t.title == request.Title, cancellationToken);

        if (task == null)
        {
            return false;
        }

        _context.TaskModel.Remove(task);
        await _context.SaveChangesAsync(cancellationToken);
        return true;
    }
}