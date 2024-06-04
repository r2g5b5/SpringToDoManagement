package com.example.springtodomanagement.services.impl;

import com.example.springtodomanagement.dtos.todo.AddTodoRequest;
import com.example.springtodomanagement.dtos.todo.GetTodoResponse;
import com.example.springtodomanagement.dtos.todo.GetTodosResponse;
import com.example.springtodomanagement.dtos.todo.UpdateTodoRequest;
import com.example.springtodomanagement.entities.Todo;
import com.example.springtodomanagement.repository.TodoRepository;
import com.example.springtodomanagement.services.TodoService;
import com.example.springtodomanagement.wrapper.BaseResult;
import com.example.springtodomanagement.wrapper.Error;
import com.example.springtodomanagement.wrapper.ErrorCodes;
import com.example.springtodomanagement.wrapper.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {


    private final TodoRepository repository;

    @Override
    @Transactional
    public Result<Long> addTodo(AddTodoRequest request) {
        Todo todo2 = repository.save(request.toTodo());
        return new Result<>(todo2.getId());
    }

    @Override
    @Transactional
    public BaseResult updateTodo(UpdateTodoRequest request) {
        Optional<Todo> todo2 = repository.findById(request.getId());
        if (todo2.isEmpty()) {
            return new BaseResult(new Error("cant update todo with id: " + request.getId(), ErrorCodes.NOT_FOUND, "updateTodo"));
        }
        todo2.get().update(request.getTitle(), request.getDescription(), request.isCompleted());
        repository.save(todo2.get());
        return new BaseResult();
    }

    @Override
    public Result<List<GetTodosResponse>> getTodos() {
        List<Todo> todos = repository.findAll();
        return new Result<>(GetTodosResponse.fromTodoList(todos));
    }

    @Override
    public Result<GetTodoResponse> getTodo(Long id) {
        Optional<Todo> todoOptional = repository.findById(id);
        if (todoOptional.isPresent()) {
            Todo todo = todoOptional.get();
            return new Result<>(GetTodoResponse.fromTodo(todo));
        }
        return new Result<>(new Error("cant find todo with id: " + id, ErrorCodes.NOT_FOUND, "todoId"));
    }

    @Override
    @Transactional
    public BaseResult deleteTodo(Long id) {
        if (!repository.existsById(id)) {
            return new BaseResult(new Error("cant find todo with id: " + id, ErrorCodes.NOT_FOUND, "todoId"));
        }
        repository.deleteById(id);
        return new BaseResult();
    }

    @Override
    @Transactional
    public BaseResult completeTodo(Long id) {
        Optional<Todo> todo2 = repository.findById(id);
        if (todo2.isEmpty()) {
            return new BaseResult(new Error("cant complete todo with id: " + id, ErrorCodes.NOT_FOUND, "todoId"));
        }
        todo2.get().updateCompleteStatus(true);
        repository.save(todo2.get());
        return new BaseResult();
    }

    @Override
    @Transactional
    public BaseResult inCompleteTodo(Long id) {
        Optional<Todo> todo2 = repository.findById(id);
        if (todo2.isEmpty()) {
            return new BaseResult(new Error("cant inComplete todo with id: " + id, ErrorCodes.NOT_FOUND, "todoId"));
        }
        todo2.get().updateCompleteStatus(false);
        repository.save(todo2.get());
        return new BaseResult();
    }

}
