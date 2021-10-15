package com.wooteco.nolto.auth.ui.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {

    private String refreshToken;
    private String clientIP;

    public RefreshTokenRequest(String refreshToken, String clientIP) {
        this.refreshToken = refreshToken;
        this.clientIP = clientIP;
    }
}
