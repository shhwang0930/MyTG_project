package com.shhwang0930.mytg.common.exception;

import com.shhwang0930.mytg.common.model.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.annotation.PostConstruct;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final HttpHeaders header = new HttpHeaders();

    @PostConstruct
    public void init(){
        header.setContentType(MediaType.APPLICATION_JSON);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleTypeMismatch(IllegalArgumentException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(ErrorMessage.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build(), header, HttpStatus.BAD_REQUEST);
    }
}
