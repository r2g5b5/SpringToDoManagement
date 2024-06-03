package com.example.springtodomanagement.dtos.todo;

import com.example.springtodomanagement.entities.Todo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddTodoRequest {

    @NotNull(message = "title is mandatory")
    @Size(min = 3, max = 20, message = "title must be between 3 and 20 characters")
    private String title;

    private String description;

    @NotNull(message = "title is mandatory")
    private boolean completed = false;

    public Todo toTodo() {
        return new Todo(null, this.title, this.description, this.completed);
    }
}
