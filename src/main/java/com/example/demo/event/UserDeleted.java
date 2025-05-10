package com.example.demo.event;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDeleted extends UserEvent implements Serializable {

    public UserDeleted(long userId) {
        super(userId, null, null);
    }

    @JsonCreator
    public UserDeleted(
           @JsonProperty("id") Long id,
           @JsonProperty("username") String username,
           @JsonProperty("token") String token) {
        super(id, username, token);
    }

}
