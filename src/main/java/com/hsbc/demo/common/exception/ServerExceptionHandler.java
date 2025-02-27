package com.hsbc.demo.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ServerExceptionHandler {

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<Map<String,String>> handleServerException(ServerException e) {
        log.error(e.getMessage());
        Map<String, String> map = new HashMap<>();
        map.put("code",e.getError().getCode());
        map.put("msg",e.getError().getMsg());
        return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,String>> handleException(Exception e) {
        log.error(e.getMessage());
        Map<String, String> map = new HashMap<>();
        map.put("msg",e.getMessage());
        return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
