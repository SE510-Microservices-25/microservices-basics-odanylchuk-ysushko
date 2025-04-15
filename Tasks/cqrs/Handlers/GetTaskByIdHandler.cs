using MediatR;
using Microsoft.EntityFrameworkCore;
using Tasks.cqrs.Queries;
using Tasks.Data;
using Tasks.Models;

namespace Tasks.cqrs.Handlers;

public class GetTaskByIdHandler : IRequestHandler<GetTaskByIdQuery, TaskModel>
{
    private readonly AppDbContext _context;

    public GetTaskByIdHandler(AppDbContext context)
    {
        _context = context;
    }

    public async Task<TaskModel> Handle(GetTaskByIdQuery request, CancellationToken cancellationToken)
    {
        return await _context.TaskModel.FirstOrDefaultAsync(t => Equals(t.id, request.Id), cancellationToken);
    }
}