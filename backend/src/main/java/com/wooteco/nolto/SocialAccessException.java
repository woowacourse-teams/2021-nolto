package com.wooteco.nolto;

public class SocialAccessException extends RuntimeException {

    public SocialAccessException() {
    }

    public SocialAccessException(String message) {
        super(message);
    }

    public SocialAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
