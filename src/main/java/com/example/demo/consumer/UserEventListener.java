package com.example.demo.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import com.example.demo.event.*;

@Service
public class UserEventListener {

    @RabbitListener(queues = "user.created.queue")
    public void handleUserCreated(UserCreated event) {
        System.out.println("User Created Event Received: " + event.getUsername());
        // Handle event (e.g., send welcome email)
    }

    @RabbitListener(queues = "user.updated.queue")
    public void handleUserUpdated(UserUpdated event) {
        System.out.println("User Updated Event Received: " + event.getUsername());
        // Handle event (e.g., update profile in cache)
    }

    @RabbitListener(queues = "user.deleted.queue")
    public void handleUserDeleted(UserDeleted event) {
        System.out.println("User Deleted Event Received: " + event.getUserId());
        // Handle event (e.g., delete user data from services)
    }
}
