package com.example.springtodomanagement.services.impl;

import com.example.springtodomanagement.dtos.todo.AddTodoRequest;
import com.example.springtodomanagement.dtos.todo.GetTodoResponse;
import com.example.springtodomanagement.dtos.todo.GetTodosResponse;
import com.example.springtodomanagement.dtos.todo.UpdateTodoRequest;
import com.example.springtodomanagement.entities.Todo;
import com.example.springtodomanagement.repository.TodoRepository;
import com.example.springtodomanagement.wrapper.BaseResult;
import com.example.springtodomanagement.wrapper.ErrorCodes;
import com.example.springtodomanagement.wrapper.Result;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository repository;

    @InjectMocks
    private TodoServiceImpl service;

    private AddTodoRequest addTodoRequest;
    private UpdateTodoRequest updateTodoRequest;
    private Todo todo;

    @BeforeEach
    void setUp() {
        addTodoRequest = AddTodoRequest.builder()
                .title("title Req")
                .description("title Desc")
                .completed(false).build();
        todo = Todo.builder()
                .title("title Req")
                .description("title Desc")
                .completed(false).build();

        updateTodoRequest= UpdateTodoRequest.builder()
                .title("title updated ver")
                .description("des updated ver")
                .completed(true).build();

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void addTodo() {

        //arrange
        when(repository.save(any(Todo.class))).thenReturn(todo);

        //act
        Result<Long> result = service.addTodo(addTodoRequest);

        //assert
        ArgumentCaptor<Todo> todoCaptor = ArgumentCaptor.forClass(Todo.class);
        verify(repository).save(todoCaptor.capture());
        Todo savedTodo = todoCaptor.getValue();

        assertThat(savedTodo.getTitle()).isEqualTo(addTodoRequest.getTitle());
        assertThat(savedTodo.getDescription()).isEqualTo(addTodoRequest.getDescription());
        assertThat(savedTodo.isCompleted()).isEqualTo(addTodoRequest.isCompleted());
        assertThat(result.isSuccess).isTrue();
        assertThat(result.getData()).isEqualTo(todo.getId());

    }


    @Test
    void updateTodo_TodoNotFound() {
        // Arrange
        when(repository.findById(updateTodoRequest.getId())).thenReturn(Optional.empty());

        // Act
        BaseResult result = service.updateTodo(updateTodoRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.isSuccess).isFalse();
        assertThat(result.errors.getFirst().getErrorCode()).isEqualTo(ErrorCodes.NOT_FOUND);
        assertThat(result.errors.getFirst().getMessage()).isEqualTo("cant update todo with id: " + updateTodoRequest.getId());
        verify(repository, times(1)).findById(updateTodoRequest.getId());
        verify(repository, never()).save(any(Todo.class));
    }

    @Test
    void updateTodo() {
        when(repository.findById(updateTodoRequest.getId())).thenReturn(Optional.of(todo));

        BaseResult result = service.updateTodo(updateTodoRequest);

        // Assert
        assertThat(result).isNotNull();
        verify(repository, times(1)).findById(updateTodoRequest.getId());
        verify(repository, times(1)).save(todo);
        assertThat(todo.getTitle()).isEqualTo(updateTodoRequest.getTitle());
        assertThat(todo.getDescription()).isEqualTo(updateTodoRequest.getDescription());
        assertThat(todo.isCompleted()).isEqualTo(updateTodoRequest.isCompleted());

    }

    @Test
    void getTodos() {
        // Arrange
        List<Todo> todoList = new ArrayList<>();
        todoList.add(new Todo(1L, "Todo 1", "Description 1", false));
        todoList.add(new Todo(2L, "Todo 2", "Description 2", true));

        when(repository.findAll()).thenReturn(todoList);

        // Act
        Result<List<GetTodosResponse>> result = service.getTodos();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();

        List<GetTodosResponse> responseList = result.getData();
        assertThat(responseList).isNotNull();

        assertThat(responseList.getFirst().getId()).isEqualTo(todoList.getFirst().getId());
        assertThat(responseList.getFirst().getTitle()).isEqualTo(todoList.getFirst().getTitle());
        assertThat(responseList.get(0).getDescription()).isEqualTo(todoList.get(0).getDescription());
        assertThat(responseList.get(0).isCompleted()).isEqualTo(todoList.get(0).isCompleted());

        assertThat(responseList.get(1).getId()).isEqualTo(todoList.get(1).getId());
        assertThat(responseList.get(1).getTitle()).isEqualTo(todoList.get(1).getTitle());
        assertThat(responseList.get(1).getDescription()).isEqualTo(todoList.get(1).getDescription());
        assertThat(responseList.get(1).isCompleted()).isEqualTo(todoList.get(1).isCompleted());

    }

    @Test
    void getTodo() {
        when(repository.findById(1L)).thenReturn(Optional.ofNullable(todo));

        Result<GetTodoResponse> result=service.getTodo(1L);

        assertThat(result).isNotNull();
        assertThat(result.isSuccess).isTrue();
        assertThat(result.data.getId()).isEqualTo(todo.getId());
        assertThat(result.data.getTitle()).isEqualTo(todo.getTitle());
        assertThat(result.data.getDescription()).isEqualTo(todo.getDescription());
        assertThat(result.data.isCompleted()).isEqualTo(todo.isCompleted());
    }

    @Test
    void cantFindTodoById(){
        when(repository.findById(100L)).thenReturn(Optional.empty());

        Result<GetTodoResponse> result = service.getTodo(100L);

        assertThat(result.isSuccess).isFalse();

    }

    @Test
    void deleteTodo() {
        when(repository.existsById(1L)).thenReturn(true);

        BaseResult result=service.deleteTodo(1L);

        assertThat(result.isSuccess).isTrue();
        verify(repository,times(1)).deleteById(1L);
    }

    @Test
    void nonExistingTodo() {
        // Arrange
        when(repository.existsById(100L)).thenReturn(false);

        // Act
        BaseResult result = service.deleteTodo(100L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.isSuccess).isFalse();
    }

    @Test
    void completeTodo() {
        when(repository.findById(1L)).thenReturn(Optional.of(todo));

        BaseResult result=service.completeTodo(1L);

        assertThat(result.isSuccess).isTrue();

        verify(repository,times(1)).save(todo);
        assertThat(todo.isCompleted()).isTrue();
    }

    @Test
    void completeTodo_NonExistingTodo() {
        // Arrange
        when(repository.findById(100L)).thenReturn(Optional.empty());

        // Act
        BaseResult result = service.completeTodo(100L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.isSuccess).isFalse();

        assertThat(result.errors).isNotNull();
        assertThat(result.errors.getFirst().getErrorCode()).isEqualTo(ErrorCodes.NOT_FOUND);
        verify(repository, never()).save(any(Todo.class));
    }

    @Test
    void inCompleteTodo() {
        when(repository.findById(1L)).thenReturn(Optional.of(todo));

        BaseResult result=service.inCompleteTodo(1L);

        assertThat(result.isSuccess).isTrue();

        verify(repository,times(1)).save(todo);
        assertThat(todo.isCompleted()).isFalse();


    }

    @Test
    void inCompleteTodo_NonExistingTodo() {
        // Arrange
        when(repository.findById(100L)).thenReturn(Optional.empty());

        // Act
        BaseResult result = service.inCompleteTodo(100L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.isSuccess).isFalse();

        assertThat(result.errors).isNotNull();
        assertThat(result.errors.getFirst().getErrorCode()).isEqualTo(ErrorCodes.NOT_FOUND);
        verify(repository, never()).save(any(Todo.class));
    }
}