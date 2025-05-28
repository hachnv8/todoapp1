package com.todoapp.todoapp.controller;

import com.todoapp.todoapp.entity.Todo;
import com.todoapp.todoapp.exception.TodoNotFoundException;
import com.todoapp.todoapp.kafka.TodoProducer;
import com.todoapp.todoapp.redis.model.TodoRedis;
import com.todoapp.todoapp.service.impl.TodoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoController {
    private final TodoServiceImpl todoService;
    private final TodoProducer todoProducer;

    // Create a new Todo
    @PostMapping
    public ResponseEntity<TodoRedis> createTodo(@RequestBody TodoRedis todo) {
        TodoRedis createdTodo = todoService.createTodo(todo);
        todoProducer.sendTodoCreated("New Todo created: " + todo.getTitle());
        return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
    }

    // Get all Todos
    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos() {
        List<Todo> todos = todoService.getAllTodos();
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    // Get a Todo by ID
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable String id) {
        Optional<Todo> todo = todoService.getTodoById(id);
        return todo.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update an existing Todo
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable String id, @RequestBody Todo todo) {
        try {
            Todo updatedTodo = todoService.updateTodo(id, todo);
            return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
        } catch (TodoNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a Todo by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoById(@PathVariable String id) {
        try {
            todoService.deleteTodoById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (TodoNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Mark a Todo as completed
    @PatchMapping("/{id}/complete")
    public ResponseEntity<Todo> markAsCompleted(@PathVariable String id) {
        try {
            Todo completedTodo = todoService.markAsCompleted(id);
            return new ResponseEntity<>(completedTodo, HttpStatus.OK);
        } catch (TodoNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
