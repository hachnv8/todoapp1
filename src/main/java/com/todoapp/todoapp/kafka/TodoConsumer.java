package com.todoapp.todoapp.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TodoConsumer {

    @KafkaListener(topics = "todo-created", groupId = "todo-group")
    public void handleTodoCreated(String message) {
//        System.out.println("Received todo: " + message);
    }
}
