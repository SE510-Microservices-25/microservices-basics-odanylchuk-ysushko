using MediatR;
using Microsoft.EntityFrameworkCore;
using Tasks.cqrs.Queries;
using Tasks.Data;
using Tasks.Models;

namespace Tasks.cqrs.Handlers;

public class GetTasksHandler : IRequestHandler<GetTasksQuery, List<TaskModel>>
{
    private readonly AppDbContext _context;

    public GetTasksHandler(AppDbContext context)
    {
        _context = context;
    }

    public async Task<List<TaskModel>> Handle(GetTasksQuery request, CancellationToken cancellationToken)
    {
        return await _context.TaskModel.ToListAsync(cancellationToken);
    }
}