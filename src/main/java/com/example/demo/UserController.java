package com.example.demo;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import com.example.demo.service.OutboxPublisherService;
import com.example.demo.service.UserCommandService;
import com.example.demo.service.UserQueryService;
import com.example.demo.model.User;
import com.example.demo.event.*;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final OutboxPublisherService outboxPublisherService;

    public UserController(UserCommandService userCommandService, 
                          UserQueryService userQueryService, 
                          OutboxPublisherService outboxPublisherService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
        this.outboxPublisherService = outboxPublisherService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userQueryService.getAllUsers();
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userQueryService.getUserByUsername(username);
        return user.isPresent() ? ResponseEntity.ok(user.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User userForm) {
        User CreatedUser = userCommandService.createUser(userForm.getUsername(), userForm.getToken());
        
        // Publish UserCreated event
        UserCreated event = new UserCreated(CreatedUser.getId(), CreatedUser.getUsername(), CreatedUser.getToken());
        outboxPublisherService.saveOutboxEvent(event);

        return ResponseEntity.ok(CreatedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody User updatedUser) {
        Optional<User> existingUserOpt = userQueryService.getUserById(id);

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            existingUser.setToken(updatedUser.getToken());
            existingUser.setUsername(updatedUser.getUsername());
            userQueryService.saveUser(existingUser);
    
            UserUpdated event = new UserUpdated(existingUser.getId(), 
                                                existingUser.getUsername(),
                                                existingUser.getToken());
            outboxPublisherService.saveOutboxEvent(event);
    
            return ResponseEntity.ok(existingUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        if (!userQueryService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        userCommandService.deleteUserById(id);

        
        UserDeleted event = new UserDeleted(id);
        outboxPublisherService.saveOutboxEvent(event);

        return ResponseEntity.noContent().build();
    }
}
