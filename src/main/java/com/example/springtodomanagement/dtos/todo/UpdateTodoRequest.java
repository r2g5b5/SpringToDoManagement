package com.example.springtodomanagement.dtos.todo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "title is mandatory")
    @Size(min = 3, max = 20, message = "title must be between 3 and 20 characters")
    private String title;
    private String description;
    private boolean completed;


}
