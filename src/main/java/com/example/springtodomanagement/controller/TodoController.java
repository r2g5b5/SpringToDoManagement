package com.example.springtodomanagement.controller;

import com.example.springtodomanagement.dtos.todo.AddTodoRequest;
import com.example.springtodomanagement.dtos.todo.GetTodoResponse;
import com.example.springtodomanagement.dtos.todo.GetTodosResponse;
import com.example.springtodomanagement.dtos.todo.UpdateTodoRequest;
import com.example.springtodomanagement.services.impl.TodoServiceImpl;
import com.example.springtodomanagement.wrapper.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoController {

    @Autowired
    private TodoServiceImpl service;

    @PostMapping("/todo")
    public Result<Long> addTodo(@RequestBody AddTodoRequest request){
       return service.addTodo(request);
    }

    @PutMapping("/todo")
    public Result<Long> updateTodo(@RequestBody UpdateTodoRequest request){
        return service.updateTodo(request);
    }

    @GetMapping("/todos")
    public Result<List<GetTodosResponse>> getTodos(){
        return service.getTodos();
    }

    @GetMapping("todo/{id}")
    public Result<GetTodoResponse> getTodo(@PathVariable("id") Long id){
        return service.getTodo(id);
    }

    @DeleteMapping("todo/{id}")
    public void deleteTodo(@PathVariable("id") Long id){
        service.deleteTodo(id);
    }

    @PatchMapping("/todo/complete/{id}")
    public Result<Long> completeTodo(@PathVariable("id") Long id){
        return service.completeTodo(id);
    }

    @PatchMapping("/todo/incomplete/{id}")
    public Result<Long> inCompleteTodo(@PathVariable("id") Long id){
        return service.inCompleteTodo(id);
    }

}
