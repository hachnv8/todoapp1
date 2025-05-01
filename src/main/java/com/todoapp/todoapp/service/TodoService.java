package com.todoapp.todoapp.service;

import com.todoapp.todoapp.entity.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoService {
    // Create a new Todo
    Todo createTodo(Todo todo);

    // Get a list of all Todos
    List<Todo> getAllTodos();

    // Get a Todo by its ID
    Optional<Todo> getTodoById(Long id);

    // Update an existing Todo
    Todo updateTodo(Long id, Todo todo);

    // Delete a Todo by its ID
    void deleteTodoById(Long id);

    // Mark a Todo as completed
    Todo markAsCompleted(Long id);
}
