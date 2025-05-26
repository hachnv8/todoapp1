package com.todoapp.todoapp.service;

import com.todoapp.todoapp.entity.Todo;
import com.todoapp.todoapp.redis.model.TodoRedis;
import com.todoapp.todoapp.redis.repository.TodoRedisRepository;
import com.todoapp.todoapp.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoSyncService {
    private final TodoRedisRepository redisRepo;
    private final TodoRepository todoRepo;

    @Scheduled(fixedRate = 120000) // 4 hours = 4 * 60 * 60 * 1000 ms
    public void syncToMySQL() {
        Iterable<TodoRedis> todos = redisRepo.findAll();
        List<Todo> todoList = new ArrayList<>();

        for (TodoRedis cached : todos) {
            Todo entity = new Todo();
            entity.setId(Long.valueOf(cached.getId()));
            entity.setTitle(cached.getTitle());
            entity.setDone(cached.isDone());
            todoList.add(entity);
        }

        todoRepo.saveAll(todoList);
    }
}
