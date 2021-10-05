package com.wooteco.nolto.auth.ui.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private Long expiredIn;

    public TokenResponse(String accessToken, String refreshToken, Long expiredIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiredIn = expiredIn;
    }

    public static TokenResponse of(String accessToken, RefreshTokenResponse refreshToken) {
        return new TokenResponse(accessToken, refreshToken.getToken(), refreshToken.getExpiredIn());
    }
}
