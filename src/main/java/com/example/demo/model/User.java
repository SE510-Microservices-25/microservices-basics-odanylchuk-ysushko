package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Column(nullable = false, unique = true)
    protected String username;

    @Column(nullable = false, unique = true)
    protected String token;

    // Default constructor (required by JPA)
    protected User() {}

    public User(String username, String token) {
        if (username == null || username.isBlank() || token == null || token.isBlank()) {
            throw new IllegalArgumentException("Username and token must not be empty.");
        }
        this.username = username;
        this.token = token;
    }

    // Getters and setters
    public long getId() { return id; }
    public String getToken() { return token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public void setToken(String token) { this.token = token; }
}
