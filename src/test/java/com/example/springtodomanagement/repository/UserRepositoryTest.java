package com.example.springtodomanagement.repository;

import com.example.springtodomanagement.entities.Role;
import com.example.springtodomanagement.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    //it's not necessary to write tests for userRepository for now,
    // because all the methods are given us by jpa, and we didn't write query's

    @Autowired
    private UserRepository underTest;

    @Autowired
    private RoleRepository roleRepository;

    private final String username = "hazhir49";
    private final String email = "test@test.com";

    @BeforeEach
    void setUp() {
        Role adminRole = new Role(1L, "ADMIN");
        Role userRole = new Role(2L, "USER");
        roleRepository.save(adminRole);
        roleRepository.save(userRole);

        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        roles.add(userRole);

        User user = new User(1L, "hazhir", username, email, "h1234@", roles);
        underTest.save(user);
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void findByUsername() {
        //act
        Optional<User> actual = underTest.findByUsername(username);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getUsername()).isEqualTo(username);
        assertThat(actual.get().getRoles()).extracting(Role::getName).containsExactlyInAnyOrder("ADMIN", "USER");

    }

    @Test
    void existsByEmail() {
        //act
        Boolean actual = underTest.existsByEmail(email);

        //assert
        assertThat(actual).isTrue();
    }

    @Test
    void findByUsernameOrEmail() {
        //act
        Optional<User> actual = underTest.findByUsernameOrEmail(username, email);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getUsername()).isEqualTo(username);
        assertThat(actual.get().getEmail()).isEqualTo(email);
    }

    @Test
    void existsByUsername() {
        //act
        Boolean actual = underTest.existsByUsername(username);

        //assert
        assertThat(actual).isTrue();
    }
}