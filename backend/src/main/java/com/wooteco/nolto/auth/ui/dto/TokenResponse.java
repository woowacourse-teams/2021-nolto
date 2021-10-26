package com.wooteco.nolto.auth.ui.dto;

import lombok.Getter;

@Getter
public class TokenResponse {

    private final String value;
    private final long expiredIn;

    public TokenResponse(String value, long expiredIn) {
        this.value = value;
        this.expiredIn = expiredIn;
    }
}