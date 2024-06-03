package com.example.springtodomanagement.repository;

import com.example.springtodomanagement.entities.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository underTest;

    private final String roleName = "ADMIN";

    @BeforeEach
    void setUp() {
        Role role = new Role(1L, roleName);

        underTest.save(role);
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void findByName() {
        //act
        Role actualRole = underTest.findByName(roleName);

        //assert
        assertThat(actualRole.getName()).isEqualTo(roleName);

    }
}