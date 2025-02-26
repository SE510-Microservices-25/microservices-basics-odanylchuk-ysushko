using Microsoft.EntityFrameworkCore;
using Tasks.Models;

namespace Tasks.Data;

public class AppDbContext : DbContext
{
    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

    public DbSet<TaskModel> TaskModel { get; set; }
    
    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<TaskModel>()
            .ToTable("TaskModel", "tasks");

        base.OnModelCreating(modelBuilder);
    }
}
