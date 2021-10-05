package com.wooteco.nolto.auth.ui.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {

    private long userId;
    private String refreshToken;
    private String clientIP;

    public RefreshTokenRequest(long userId, String refreshToken, String clientIP) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.clientIP = clientIP;
    }
}
