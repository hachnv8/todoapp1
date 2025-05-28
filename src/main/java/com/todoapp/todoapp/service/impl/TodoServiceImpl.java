package com.todoapp.todoapp.service.impl;

import com.todoapp.todoapp.entity.Todo;
import com.todoapp.todoapp.exception.TodoNotFoundException;
import com.todoapp.todoapp.redis.model.TodoRedis;
import com.todoapp.todoapp.redis.repository.TodoRedisRepository;
import com.todoapp.todoapp.repository.TodoRepository;
import com.todoapp.todoapp.service.GenericRedisService;
import com.todoapp.todoapp.service.TodoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;
    private final GenericRedisService redisService;

    @Override
    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    @Override
    public Optional<Todo> getTodoById(String id) {
        return todoRepository.findById(id);
    }

    @Override
    public Todo updateTodo(String id, Todo todo) {
        // Check if the Todo exists
        if (todoRepository.existsById(id)) {
            todo.setId(id);
            return todoRepository.save(todo);
        }
        throw new TodoNotFoundException("Todo with id " + id + " not found");
    }

    @Override
    public void deleteTodoById(String id) {
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
        } else {
            throw new TodoNotFoundException("Todo with id " + id + " not found");
        }
    }

    @Override
    public Todo markAsCompleted(String id) {
        Optional<Todo> todoOptional = todoRepository.findById(id);
        if (todoOptional.isPresent()) {
            Todo todo = todoOptional.get();
            todo.setCompleted(true);
            return todoRepository.save(todo);
        }
        throw new TodoNotFoundException("Todo with id " + id + " not found");
    }

    @Override
    public TodoRedis createTodo(TodoRedis todo) {
        String id = UUID.randomUUID().toString();
        todo.setId(id);  // Set ID so you can retrieve later
        redisService.save("todoRedis", id, todo);
        return redisService.get("todoRedis", id, TodoRedis.class);
    }
}
