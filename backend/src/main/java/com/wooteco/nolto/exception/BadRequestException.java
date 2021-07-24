package com.wooteco.nolto.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends NoltoException {
    public static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;

    public BadRequestException(ErrorType errorType) {
        super(BAD_REQUEST, errorType);
    }
}
