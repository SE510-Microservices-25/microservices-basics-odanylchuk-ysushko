using MediatR;
using System.Collections.Generic;
using Tasks.Models;

namespace Tasks.cqrs.Queries;

public record GetTasksQuery() : IRequest<List<TaskModel>>;