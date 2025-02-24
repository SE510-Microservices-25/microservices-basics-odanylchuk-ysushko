package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String token;

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
    public Long getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getToken() { return token; }
}
