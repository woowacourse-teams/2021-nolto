package com.wooteco.nolto.exception;

import com.wooteco.nolto.exception.dto.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class ControllerAdvice {
    private final Logger log = LoggerFactory.getLogger(ControllerAdvice.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String defaultMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        ExceptionResponse errorResponse = new ExceptionResponse("common-001", defaultMessage);
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NoltoException.class)
    public ResponseEntity<ExceptionResponse> handleNoltoException(NoltoException e) {
        log.info(e.getBody().getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(e.getBody());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.internalServerError().body("놀토 관리자에게 문의하세요");
    }
}
