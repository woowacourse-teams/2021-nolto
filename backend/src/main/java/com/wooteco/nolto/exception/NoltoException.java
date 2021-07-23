package com.wooteco.nolto.exception;

import com.wooteco.nolto.exception.dto.ExceptionResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoltoException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final ExceptionResponse body;

    public NoltoException(HttpStatus httpStatus, ErrorType errorType) {
        super(errorType.getMessage());
        this.httpStatus = httpStatus;
        this.body = new ExceptionResponse(errorType.getErrorCode(), errorType.getMessage());
    }
}
