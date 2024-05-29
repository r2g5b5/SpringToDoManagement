package com.example.springtodomanagement.repository;

import com.example.springtodomanagement.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String userName);
    Boolean existsByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username,String email);
    Boolean existsByUsername(String username);
}
