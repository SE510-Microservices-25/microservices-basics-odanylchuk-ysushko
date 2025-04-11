using Tasks.Models;
using Tasks.Data;
using DotNetEnv;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.HttpOverrides;

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

app.UseSwagger();
app.UseSwaggerUI();

// app.UseHttpsRedirection();
app.MapControllers();

if (args.Contains("migrate"))
{
    using (var scope = app.Services.CreateScope())
    {
        var dbContext = scope.ServiceProvider.GetRequiredService<AppDbContext>();
        dbContext.Database.Migrate();
    }
    return;
}

var forwardedHeadersOptions = new ForwardedHeadersOptions
{
    ForwardedHeaders = ForwardedHeaders.XForwardedFor | ForwardedHeaders.XForwardedProto,
};
forwardedHeadersOptions.KnownNetworks.Clear();
forwardedHeadersOptions.KnownProxies.Clear();
app.UseForwardedHeaders(forwardedHeadersOptions);


app.Run();
