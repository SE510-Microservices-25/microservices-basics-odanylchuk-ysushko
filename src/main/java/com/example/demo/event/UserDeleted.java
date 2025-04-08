package com.example.demo.event;

import java.io.Serializable;

public class UserDeleted extends UserEvent implements Serializable {

    public UserDeleted(long userId) {
        super(userId, null, null);
    }

}
