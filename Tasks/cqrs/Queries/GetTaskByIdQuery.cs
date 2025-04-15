using MediatR;
using System;
using Tasks.Models;

namespace Tasks.cqrs.Queries;

public record GetTaskByIdQuery(int Id) : IRequest<TaskModel>;
