package com.wooteco.nolto.exception.dto;

import lombok.Getter;

@Getter
public class ExceptionResponse {

    private String errorCode;
    private String message;

    public ExceptionResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}