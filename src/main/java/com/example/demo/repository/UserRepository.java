package com.example.demo.repository;

import com.example.demo.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data JPA will automatically generate the implementation of this methods
    User findByUsername(String username);

    User findByToken(String token);

    List<User> findAllByUsername(String username);

    List<User> findAllByToken(String token);
    
}
