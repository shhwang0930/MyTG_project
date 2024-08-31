package com.shhwang0930.mytg.common.advice;

import com.shhwang0930.mytg.common.model.ErrorMessage;
import com.shhwang0930.mytg.common.model.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final HttpHeaders headers = new HttpHeaders();


    @PostConstruct
    public void init() {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<?> exceptionHandler(RuntimeException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return new ResponseEntity<>(ErrorMessage.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("INTERNAL_SERVER_ERROR")
                .build(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> exceptionHandler(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return new ResponseEntity<>(ErrorMessage.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("INTERNAL_SERVER_ERROR")
                .build(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
