package com.wooteco.nolto.auth.ui.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {
    private AccessTokenResponse accessTokenResponse;
    private String refreshToken;
    private long expiredIn;

    public TokenResponse(AccessTokenResponse accessTokenResponse, String refreshToken, long expiredIn) {
        this.accessTokenResponse = accessTokenResponse;
        this.refreshToken = refreshToken;
        this.expiredIn = expiredIn;
    }

    public static TokenResponse of(String accessToken, String refreshToken, long expiredIn) {
        return new TokenResponse(new AccessTokenResponse(accessToken), refreshToken, expiredIn);
    }

    public String getAccessToken() {
        return accessTokenResponse.getAccessToken();
    }
}
