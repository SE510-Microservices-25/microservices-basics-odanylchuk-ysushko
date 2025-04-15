using MediatR;

namespace Tasks.cqrs.Queries;

public record DeleteTaskQuery(string Title) : IRequest<bool>;