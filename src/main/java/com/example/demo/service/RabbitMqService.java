package com.example.demo.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import com.example.demo.model.UserCreated;
import com.example.demo.model.UserUpdated;
import com.example.demo.model.UserDeleted;

@Service
public class RabbitMqService {
    private final RabbitTemplate rabbitTemplate;

    public RabbitMqService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendUserCreatedEvent(UserCreated event) {
        rabbitTemplate.convertAndSend("user.exchange", "user.created", event);
    }

    public void sendUserUpdatedEvent(UserUpdated event) {
        rabbitTemplate.convertAndSend("user.exchange", "user.updated", event);
    }

    public void sendUserDeletedEvent(UserDeleted event) {
        rabbitTemplate.convertAndSend("user.exchange", "user.deleted", event);
    }
}
