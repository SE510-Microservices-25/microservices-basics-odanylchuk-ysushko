package com.example.demo;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import com.example.demo.service.RabbitMqService;
import com.example.demo.model.User;
import com.example.demo.model.UserCreated;
import com.example.demo.model.UserUpdated;
import com.example.demo.model.UserDeleted;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {
    private final RabbitMqService rabbitMqService;
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository, RabbitMqService rabbitMqService) {
        this.userRepository = userRepository;
        this.rabbitMqService = rabbitMqService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        
        // Publish UserCreated event
        UserCreated event = new UserCreated(savedUser.getId(), savedUser.getUsername(), savedUser.getToken());
        rabbitMqService.sendUserCreatedEvent(event);

        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody User updatedUser) {
        Optional<User> existingUserOpt = userRepository.findById(id);

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            existingUser.setToken(updatedUser.getToken());
            existingUser.setUsername(updatedUser.getUsername());
            userRepository.save(existingUser);
    
            UserUpdated event = new UserUpdated(existingUser.getId(), existingUser.getUsername(), existingUser.getToken());
            rabbitMqService.sendUserUpdatedEvent(event);
    
            return ResponseEntity.ok(existingUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        userRepository.deleteById(id);

        // Publish UserDeleted event
        UserDeleted event = new UserDeleted(id);
        rabbitMqService.sendUserDeletedEvent(event);

        return ResponseEntity.noContent().build();
    }
}
