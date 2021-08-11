package com.wooteco.nolto.exception;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends NoltoException {
    public static final HttpStatus INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR;

    public InternalServerErrorException(ErrorType errorType) {
        super(INTERNAL_SERVER_ERROR, errorType);
    }
}
