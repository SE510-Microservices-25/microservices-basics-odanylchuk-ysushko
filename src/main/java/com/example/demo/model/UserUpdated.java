package com.example.demo.model;

import java.io.Serializable;

public class UserUpdated extends UserEvent implements Serializable {

    public UserUpdated(long userId, String username, String token) {
        super(userId, username, token);
    }

}
