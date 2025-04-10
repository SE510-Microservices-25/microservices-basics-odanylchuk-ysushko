package com.example.demo.service;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.OutboxMessage;
import com.example.demo.event.UserCreated;
import com.example.demo.event.UserDeleted;
import com.example.demo.event.UserUpdated;
import com.example.demo.repository.IOutboxMessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OutboxPublisherService {
    private final IOutboxMessageRepository outboxMessageRepository; 
    private final RabbitMqService rabbitMqService;

    public OutboxPublisherService(IOutboxMessageRepository outboxRepo, RabbitMqService rabbitMqService) {
        this.outboxMessageRepository = outboxRepo;
        this.rabbitMqService = rabbitMqService;
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void publishEvents() {
        List<OutboxMessage> messages = outboxMessageRepository.findByProcessedFalse();
        ObjectMapper objectMapper = new ObjectMapper();

        for (OutboxMessage msg : messages) {
            try {
                Class<?> clazz = Class.forName(msg.getType());
                Object event = objectMapper.readValue(msg.getPayload(), clazz);

                if (event instanceof UserCreated) {
                    rabbitMqService.sendUserCreatedEvent((UserCreated) event);
                } else if (event instanceof UserUpdated) {
                    rabbitMqService.sendUserUpdatedEvent((UserUpdated) event);
                } else if (event instanceof UserDeleted) {
                    rabbitMqService.sendUserDeletedEvent((UserDeleted) event);
                }
            } catch (ClassNotFoundException e) {
                System.out.printf("Failed to process message with type: %s, error: %s%n", msg.getType(), e.getMessage());
            } catch (JsonProcessingException e) {
                System.out.printf("Failed to process json payload: %s, error: %s%n", msg.getPayload(), e.getMessage());
            }
        }
    }

    public void saveOutboxEvent(Object payload) {
        try {
            String json = new ObjectMapper().writeValueAsString(payload);
            OutboxMessage message = new OutboxMessage();
            message.setType(payload.getClass().getName());
            message.setPayload(json);
            message.setProcessed(false);
            outboxMessageRepository.save(message);
        } catch (JsonProcessingException e) {
            System.out.printf("Failed to save message with payload: {}", payload, e);

        }
    }

}
