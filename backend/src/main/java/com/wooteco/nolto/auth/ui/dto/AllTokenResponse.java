package com.wooteco.nolto.auth.ui.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllTokenResponse {
    private final TokenResponse accessToken;
    private final TokenResponse refreshToken;

    public AllTokenResponse(TokenResponse accessToken, TokenResponse refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
