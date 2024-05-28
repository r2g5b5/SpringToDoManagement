package com.example.springtodomanagement.dtos.todo;

import com.example.springtodomanagement.entities.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTodoRequest {
    private Long Id;
    private String title;
    private String description;
    private boolean completed;


}
