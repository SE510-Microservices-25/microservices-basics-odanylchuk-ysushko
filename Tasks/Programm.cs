using Tasks.Models;
using Tasks.Data;
using DotNetEnv;
using Microsoft.EntityFrameworkCore;

Env.Load();
var builder = WebApplication.CreateBuilder(args);

var pgUser = Environment.GetEnvironmentVariable("PG_USER");
var pgPassword = Environment.GetEnvironmentVariable("PG_PASSWORD");

// Set the connection string with the environment variables
var connectionString = builder.Configuration.GetConnectionString("DefaultConnection")
    .Replace("${PG_USER}", pgUser)
    .Replace("${PG_PASSWORD}", pgPassword);

builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseNpgsql(connectionString));

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();
app.MapControllers();

app.Run();
