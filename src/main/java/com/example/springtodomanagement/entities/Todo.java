package com.example.springtodomanagement.entities;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "todos")
public class Todo {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    private boolean completed;

    public void update(String title, String description, boolean completed) {
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateCompleteStatus(boolean completed) {
        this.completed = completed;
    }
}
