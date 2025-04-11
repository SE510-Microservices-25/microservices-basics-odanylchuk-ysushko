package com.example.demo.event;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserCreated extends UserEvent implements Serializable {

    public UserCreated(long userId, String username, String token) {
        super(userId, username, token);
    }

    @JsonCreator
    public UserCreated(
           @JsonProperty("id") Long id,
           @JsonProperty("username") String username,
           @JsonProperty("token") String token) {
        super(id, username, token);
    }
    
}
