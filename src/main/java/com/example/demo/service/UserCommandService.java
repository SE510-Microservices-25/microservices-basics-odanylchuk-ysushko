package com.example.demo.service;

import com.example.demo.entity.OutboxMessage;
import com.example.demo.model.User;
import com.example.demo.repository.IOutboxMessageRepository;
import com.example.demo.repository.IUserCommandRepository;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserCommandService {

    private final IUserCommandRepository userCommandRepository;
    private final IOutboxMessageRepository outboxMessageRepository;

    public UserCommandService(IUserCommandRepository userCommandRepository, IOutboxMessageRepository outboxMessageRepository) {
        this.userCommandRepository = userCommandRepository;
        this.outboxMessageRepository = outboxMessageRepository;
    }

    @Transactional
    public User createUser(String username, String token) {
        User user = new User(username, token);

        OutboxMessage message = new OutboxMessage();
        message.setType("UserCreated");
        message.setCreatedAt(Instant.now());
        message.setProcessed(false);
        outboxMessageRepository.save(message);
        return userCommandRepository.save(user);
    }

    public User updateUser(Long id, String newUsername, String newToken) {
        User user = userCommandRepository.findById(id).orElseThrow();
        user.setUsername(newUsername);
        user.setToken(newToken);
        return userCommandRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userCommandRepository.deleteById(id);
    }

    public void deleteUserByName(String username) {
        userCommandRepository.deleteByUsername(username);
    }

    public void deleteUserByToken(String token) {
        userCommandRepository.deleteByToken(token);
    }
}
