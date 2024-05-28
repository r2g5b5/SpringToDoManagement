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
public class AddTodoRequest {

    private String title;
    private String description;
    private boolean completed;

    public Todo toTodo() {
        return new Todo(null,this.title,this.description,this.completed);
    }
}
