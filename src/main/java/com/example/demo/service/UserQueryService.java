package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.IUserQueryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserQueryService {

    private final IUserQueryRepository userQueryRepository;

    public UserQueryService(IUserQueryRepository userQueryRepository) {
        this.userQueryRepository = userQueryRepository;
    }

    public Optional<User> getUserById(long id) {
        return userQueryRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userQueryRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userQueryRepository.findAll();
    }

    public boolean existsById(long id) {
        return userQueryRepository.existsById(id);
    }

    public boolean existsByUsername(String username) {
        return userQueryRepository.existsByUsername(username);
    }

    public boolean existsByToken(String token) {
        return userQueryRepository.existsByToken(token);
    }

    public User saveUser(User user) {
        return userQueryRepository.save(user);
    }
}
