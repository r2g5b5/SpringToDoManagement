package com.example.springtodomanagement.services;

import com.example.springtodomanagement.dtos.todo.AddTodoRequest;
import com.example.springtodomanagement.dtos.todo.GetTodoResponse;
import com.example.springtodomanagement.dtos.todo.GetTodosResponse;
import com.example.springtodomanagement.dtos.todo.UpdateTodoRequest;
import com.example.springtodomanagement.wrapper.BaseResult;
import com.example.springtodomanagement.wrapper.Result;

import java.util.List;

public interface TodoService {

    Result<Long> addTodo(AddTodoRequest request);
    BaseResult updateTodo(UpdateTodoRequest request);
    Result<List<GetTodosResponse>> getTodos();
    Result<GetTodoResponse> getTodo(Long id);
    BaseResult deleteTodo(Long id);
    BaseResult completeTodo(Long id);
    BaseResult inCompleteTodo(Long id);

}
