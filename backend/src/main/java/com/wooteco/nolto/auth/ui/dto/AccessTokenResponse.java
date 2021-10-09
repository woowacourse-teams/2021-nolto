package com.wooteco.nolto.auth.ui.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccessTokenResponse {

    private final String accessToken;

    public AccessTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
