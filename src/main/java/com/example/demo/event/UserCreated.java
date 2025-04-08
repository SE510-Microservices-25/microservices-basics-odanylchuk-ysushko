package com.example.demo.event;

import java.io.Serializable;

public class UserCreated extends UserEvent implements Serializable {

    public UserCreated(long userId, String username, String token) {
        super(userId, username, token);
    }
    
}
