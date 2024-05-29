package com.example.springtodomanagement.controller;

import com.example.springtodomanagement.dtos.todo.AddTodoRequest;
import com.example.springtodomanagement.dtos.todo.GetTodoResponse;
import com.example.springtodomanagement.dtos.todo.GetTodosResponse;
import com.example.springtodomanagement.dtos.todo.UpdateTodoRequest;
import com.example.springtodomanagement.parametrs.BaseRequestBody;
import com.example.springtodomanagement.services.TodoService;
import com.example.springtodomanagement.wrapper.BaseResult;
import com.example.springtodomanagement.wrapper.Result;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class TodoController {

    private TodoService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/todo/create")
    public Result<Long> create(@RequestBody AddTodoRequest request) {
        return service.addTodo(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/todo/update")
    public BaseResult update(@RequestBody UpdateTodoRequest request) {
        return service.updateTodo(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/todo/getAll")
    public Result<List<GetTodosResponse>> getAll() {
        return service.getTodos();
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("todo/getById")
    public Result<GetTodoResponse> getById(Long id) {
        return service.getTodo(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("todo/delete")
    public BaseResult delete(@RequestBody BaseRequestBody requestBody) {
        return service.deleteTodo(requestBody.getId());
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PatchMapping("/todo/complete")
    public BaseResult complete(@RequestBody BaseRequestBody requestBody) {
        return service.completeTodo(requestBody.getId());
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PatchMapping("/todo/incomplete")
    public BaseResult incomplete(@RequestBody BaseRequestBody requestBody) {
        return service.inCompleteTodo(requestBody.getId());
    }

}
