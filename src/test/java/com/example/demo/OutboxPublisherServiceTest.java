package com.example.demo;

import com.example.demo.entity.OutboxMessage;
import com.example.demo.event.UserCreated;
import com.example.demo.event.UserDeleted;
import com.example.demo.event.UserUpdated;
import com.example.demo.repository.IOutboxMessageRepository;
import com.example.demo.service.OutboxPublisherService;
import com.example.demo.service.RabbitMqService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OutboxPublisherServiceTest {

    @Mock
    private IOutboxMessageRepository outboxMessageRepository;

    @Mock
    private RabbitMqService rabbitMqService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OutboxPublisherService outboxPublisherService;

    private OutboxMessage userCreatedMessage;
    private OutboxMessage userUpdatedMessage;
    private OutboxMessage userDeletedMessage;
    private UserCreated userCreated;
    private UserUpdated userUpdated;
    private UserDeleted userDeleted;

    @BeforeEach
    public void setup() throws Exception {
        userCreated = new UserCreated(1L, "user1", "token1");
        userUpdated = new UserUpdated(1L, "updatedUser", "updatedToken");
        userDeleted = new UserDeleted(1L);

        userCreatedMessage = new OutboxMessage();
        userCreatedMessage.setType(UserCreated.class.getName());
        userCreatedMessage.setPayload(new ObjectMapper().writeValueAsString(userCreated));
        userCreatedMessage.setProcessed(false);

        userUpdatedMessage = new OutboxMessage();
        userUpdatedMessage.setType(UserUpdated.class.getName());
        userUpdatedMessage.setPayload(new ObjectMapper().writeValueAsString(userUpdated));
        userUpdatedMessage.setProcessed(false);

        userDeletedMessage = new OutboxMessage();
        userDeletedMessage.setType(UserDeleted.class.getName());
        userDeletedMessage.setPayload(new ObjectMapper().writeValueAsString(userDeleted));
        userDeletedMessage.setProcessed(false);
    }

    @Test
    public void testSaveOutboxEvent_SavesMessageToRepository() {
        // Arrange
        when(outboxMessageRepository.save(any(OutboxMessage.class))).thenReturn(userCreatedMessage);

        // Act
        outboxPublisherService.saveOutboxEvent(userCreated);

        // Assert
        verify(outboxMessageRepository, times(1)).save(any(OutboxMessage.class));
    }
}