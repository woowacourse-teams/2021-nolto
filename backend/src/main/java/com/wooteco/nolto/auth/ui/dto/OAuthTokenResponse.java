package com.wooteco.nolto.auth.ui.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuthTokenResponse {
    private String token_type;
    private String scope;
    private String access_token;

    public OAuthTokenResponse(String token_type, String scope, String access_token) {
        this.token_type = token_type;
        this.scope = scope;
        this.access_token = access_token;
    }
}
