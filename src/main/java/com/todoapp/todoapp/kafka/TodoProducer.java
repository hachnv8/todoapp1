package com.todoapp.todoapp.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendTodoCreated(String message) {
        kafkaTemplate.send("todo-created", message);
    }
}
