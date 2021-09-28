package com.wooteco.nolto.exception;

import com.wooteco.nolto.exception.dto.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {
    private final Logger log = LoggerFactory.getLogger(ControllerAdvice.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String defaultMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        ExceptionResponse errorResponse = new ExceptionResponse(ErrorType.DATA_BINDING_ERROR.getErrorCode(), defaultMessage);
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionResponse> handleBindExceptionException(BindException e) {
        ExceptionResponse errorResponse = new ExceptionResponse(ErrorType.DATA_BINDING_ERROR.getErrorCode(), getExceptionMessage(e));
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private String getExceptionMessage(BindException e) {
        return e.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
    }

    @ExceptionHandler(NoltoException.class)
    public ResponseEntity<ExceptionResponse> handleNoltoException(NoltoException e) {
        log.info(e.getBody().getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(e.getBody());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionResponse.of(ErrorType.NOT_FOUND));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        StringBuilder sb = new StringBuilder();

        sb.append(e.getMessage());
        sb.append("%n༼;´༎ຶ \u06DD ༎ຶ༽ \uD835\uDE52\uD835\uDE5D\uD835\uDE6E\uD835\uDE67\uD835\uDE56\uD835\uDE63\uD835\uDE64...\n");
        sb.append("༼;´༎ຶ \u06DD ༎ຶ༽ 에러 로그 시작 ༼;´༎ຶ \u06DD ༎ຶ༽\n");
        String[] spiltMessages = Arrays.toString(e.getStackTrace()).split(",");

        for (String splitMessage : spiltMessages) {
            sb.append(splitMessage).append("\n");
        }

        sb.append("༼;´༎ຶ \u06DD ༎ຶ༽ 에러 로그 끝 ༼;´༎ຶ \u06DD ༎ຶ༽\n");
        sb.append("༼;´༎ຶ \u06DD ༎ຶ༽ \uD835\uDE52\uD835\uDE5D\uD835\uDE6E\uD835\uDE67\uD835\uDE56\uD835\uDE63\uD835\uDE64...");
        log.error("༼;´༎ຶ \u06DD ༎ຶ༽ 에러 메세지: {}", sb);
        return ResponseEntity.internalServerError().body("놀토 관리자에게 문의하세요");
    }
}
