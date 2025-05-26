package com.todoapp.todoapp.redis.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("TodoCache")
@Data
public class TodoRedis {
    @Id
    private String id;
    private String title;
    private boolean done;
}
