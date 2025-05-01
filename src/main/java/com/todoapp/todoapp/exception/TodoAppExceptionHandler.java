package com.todoapp.todoapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class TodoAppExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<Object> handleTodoNotFoundException() {
        String errorDetails = "Todo not found";
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
