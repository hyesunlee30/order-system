package com.encore.ordering.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.Map;



@RestControllerAdvice // 복습하기
@Slf4j
public class ExceptionHandlerClass {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String,Object>> entityNotFoundExceptionHandler(EntityNotFoundException e) {
        log.error("Handler entity not found "+e.getMessage());
        return ErrorResponseDTO.makeMessage(HttpStatus.NOT_FOUND, e.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.error("IllegalArgumentException "+e.getMessage());
        return ErrorResponseDTO.makeMessage(HttpStatus.BAD_REQUEST, e.getMessage());
    }


}
