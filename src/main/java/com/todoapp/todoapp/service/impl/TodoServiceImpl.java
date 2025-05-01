package com.todoapp.todoapp.service.impl;

import com.todoapp.todoapp.entity.Todo;
import com.todoapp.todoapp.exception.TodoNotFoundException;
import com.todoapp.todoapp.repository.TodoRepository;
import com.todoapp.todoapp.service.TodoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;

    @Override
    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    @Override
    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    @Override
    public Todo updateTodo(Long id, Todo todo) {
        // Check if the Todo exists
        if (todoRepository.existsById(id)) {
            todo.setId(id);
            return todoRepository.save(todo);
        }
        throw new TodoNotFoundException("Todo with id " + id + " not found");
    }

    @Override
    public void deleteTodoById(Long id) {
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
        } else {
            throw new TodoNotFoundException("Todo with id " + id + " not found");
        }
    }

    @Override
    public Todo markAsCompleted(Long id) {
        Optional<Todo> todoOptional = todoRepository.findById(id);
        if (todoOptional.isPresent()) {
            Todo todo = todoOptional.get();
            todo.setCompleted(true);
            return todoRepository.save(todo);
        }
        throw new TodoNotFoundException("Todo with id " + id + " not found");
    }
}
