package com.wooteco.nolto.auth.ui.dto;

import lombok.Getter;

@Getter
public class RefreshTokenResponse {

    private final String token;
    private final long expiredIn;

    public RefreshTokenResponse(String token, long expiredIn) {
        this.token = token;
        this.expiredIn = expiredIn;
    }
}
