package com.example.demo.repository;

import com.example.demo.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserQueryRepository extends JpaRepository<User, Long> {

    Optional<User> findById(long id);

    Optional<User> findByUsername(String username);

    List<User> findAllByUsername(String username);

    List<User> findAllByToken(String token);

    boolean existsByUsername(String username);

    boolean existsByToken(String token);

    boolean existsById(long id);

}
