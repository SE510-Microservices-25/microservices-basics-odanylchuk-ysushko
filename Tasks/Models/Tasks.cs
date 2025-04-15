using System.Collections.Generic;
using Tasks.Domain;

namespace Tasks.Models
{
    public class TaskModel : IHasDomainEvents
    {
        public int id { get; set; }
        public string title { get; set; } = string.Empty;
        public bool is_completed { get; set; }
        
        public List<object> DomainEvents { get; } = new List<object>();

        public void ClearDomainEvents() => DomainEvents.Clear();
        
        public void MarkCompleted()
        {
            is_completed = true;
            DomainEvents.Add(new TaskCompletedEvent { TaskId = id, Title = title });
        }
    }
}
