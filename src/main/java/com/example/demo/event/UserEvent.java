package com.example.demo.event;

public abstract class UserEvent {
    private long userId;
    private String username;
    private String token;

    public UserEvent(long userId, String username, String token) {
        this.userId = userId;
        this.username = username;
        this.token = token;
    }

    // Getters and setters
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return token;
    }

    public void setEmail(String token) {
        this.token = token;
    }   
}
