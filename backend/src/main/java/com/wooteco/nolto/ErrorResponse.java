package com.wooteco.nolto;

public class ErrorResponse {
    private String errorMessage;

    public ErrorResponse() {
    }

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static ErrorResponse of(String errorMessage) {
        return new ErrorResponse(errorMessage);
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
