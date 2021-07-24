package com.wooteco.nolto.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends NoltoException {
    public static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;

    public NotFoundException(ErrorType errorType) {
        super(NOT_FOUND, errorType);
    }
}
