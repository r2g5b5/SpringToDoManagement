package com.example.springtodomanagement.dtos.todo;

import com.example.springtodomanagement.entities.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetTodosResponse {
    private Long Id;
    private String title;
    private String description;
    private boolean completed;

    public static GetTodosResponse fromTodo(Todo todo) {
        return new GetTodosResponse(todo.getId(), todo.getTitle(), todo.getDescription(), todo.isCompleted());
    }

    public static List<GetTodosResponse> fromTodoList(List<Todo> todos) {
        return todos.stream()
                .map(GetTodosResponse::fromTodo)
                .collect(Collectors.toList());
    }

}
