package com.todoapp.todoapp.service;

import com.todoapp.todoapp.entity.Todo;
import com.todoapp.todoapp.redis.model.TodoRedis;
import com.todoapp.todoapp.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TodoSyncService {
    private final GenericRedisService redisService;
    private final TodoRepository todoRepo;

    @Scheduled(fixedRate = 120000) // 4 hours = 4 * 60 * 60 * 1000 ms
    public void syncToMySQL() {
        System.out.println("Scheduled sync started...");
        Map<Object, Object> cachedTodos = redisService.getAll("todoRedis");

        if (cachedTodos == null || cachedTodos.isEmpty()) return;

        List<Todo> todoList = new ArrayList<>();
        List<String> idsToDelete = new ArrayList<>();

        for (Map.Entry<Object, Object> entry : cachedTodos.entrySet()) {
            String id = (String) entry.getKey();
            TodoRedis cached = (TodoRedis) entry.getValue();

            Todo entity = new Todo();
            entity.setId(cached.getId());
            entity.setTitle(cached.getTitle());
            entity.setDone(true);

            todoList.add(entity);
            idsToDelete.add(id);
        }

        // Save to DB
        todoRepo.saveAll(todoList);

        // Delete from Redis using your service
        redisService.deleteBatch("todoRedis", idsToDelete);
    }
}
