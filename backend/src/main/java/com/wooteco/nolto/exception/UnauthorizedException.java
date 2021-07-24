package com.wooteco.nolto.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends NoltoException {
    public static final HttpStatus UNAUTHORIZED = HttpStatus.UNAUTHORIZED;

    public UnauthorizedException(ErrorType errorType) {
        super(UNAUTHORIZED, errorType);
    }
}
