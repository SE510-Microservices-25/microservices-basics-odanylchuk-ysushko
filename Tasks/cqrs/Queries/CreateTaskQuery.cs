using MediatR;

namespace Tasks.cqrs.Queries;

public record CreateTaskQuery(string Title) : IRequest<int>;