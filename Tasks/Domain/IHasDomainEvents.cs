using System.Collections.Generic;

namespace Tasks.Domain;

public interface IHasDomainEvents
{
    List<object> DomainEvents { get; }

    void ClearDomainEvents();
}