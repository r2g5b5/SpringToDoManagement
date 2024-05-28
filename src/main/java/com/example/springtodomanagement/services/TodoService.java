package com.example.springtodomanagement.services;

import com.example.springtodomanagement.dtos.todo.AddTodoRequest;
import com.example.springtodomanagement.dtos.todo.GetTodoResponse;
import com.example.springtodomanagement.dtos.todo.GetTodosResponse;
import com.example.springtodomanagement.dtos.todo.UpdateTodoRequest;
import com.example.springtodomanagement.wrapper.Result;

import java.util.List;

public interface TodoService {

    Result<Long> addTodo(AddTodoRequest request);
    Result<Long> updateTodo(UpdateTodoRequest request);
    Result<List<GetTodosResponse>> getTodos();
    Result<GetTodoResponse> getTodo(Long id);
    void deleteTodo(Long id);
    Result<Long> completeTodo(Long id);
    Result<Long> inCompleteTodo(Long id);

}
