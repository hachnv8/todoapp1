package com.todoapp.todoapp.redis.repository;

import com.todoapp.todoapp.redis.model.TodoRedis;
import org.springframework.data.repository.CrudRepository;

public interface TodoRedisRepository extends CrudRepository<TodoRedis, String> {
}
