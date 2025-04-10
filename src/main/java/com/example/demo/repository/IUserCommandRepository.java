package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserCommandRepository extends JpaRepository<User, Long> {
    // Crud operations

    //delete operations
    void deleteByUsername(String username);

    void deleteByToken(String token);

    void deleteById(long id);
    
}