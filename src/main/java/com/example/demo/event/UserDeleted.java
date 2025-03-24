package com.example.demo.model;

import java.io.Serializable;

public class UserDeleted extends UserEvent implements Serializable {

    public UserDeleted(long userId) {
        super(userId, null, null);
    }


    
}
