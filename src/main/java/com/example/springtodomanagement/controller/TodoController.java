package com.example.springtodomanagement.controller;

import com.example.springtodomanagement.dtos.todo.AddTodoRequest;
import com.example.springtodomanagement.dtos.todo.GetTodoResponse;
import com.example.springtodomanagement.dtos.todo.GetTodosResponse;
import com.example.springtodomanagement.dtos.todo.UpdateTodoRequest;
import com.example.springtodomanagement.parametrs.BaseRequestBody;
import com.example.springtodomanagement.services.impl.TodoServiceImpl;
import com.example.springtodomanagement.wrapper.BaseResult;
import com.example.springtodomanagement.wrapper.Result;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class TodoController {

    private TodoServiceImpl service;

    @PostMapping("/todo/create")
    public Result<Long> create(@RequestBody AddTodoRequest request) {
        return service.addTodo(request);
    }

    @PutMapping("/todo/update")
    public BaseResult update(@RequestBody UpdateTodoRequest request) {
        return service.updateTodo(request);
    }

    @GetMapping("/todo/getAll")
    public Result<List<GetTodosResponse>> getAll() {
        return service.getTodos();
    }

    @GetMapping("todo/getById")
    public Result<GetTodoResponse> getById(Long id) {
        return service.getTodo(id);
    }

    @DeleteMapping("todo/delete")
    public BaseResult delete(@RequestBody BaseRequestBody requestBody) {
        return service.deleteTodo(requestBody.getId());
    }

    @PatchMapping("/todo/complete")
    public BaseResult complete(@RequestBody BaseRequestBody requestBody) {
        return service.completeTodo(requestBody.getId());
    }

    @PatchMapping("/todo/incomplete")
    public BaseResult incomplete(@RequestBody BaseRequestBody requestBody) {
        return service.inCompleteTodo(requestBody.getId());
    }

}
