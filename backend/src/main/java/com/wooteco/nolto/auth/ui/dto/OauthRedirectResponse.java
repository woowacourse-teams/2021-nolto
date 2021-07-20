package com.wooteco.nolto.auth.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OauthRedirectResponse {

    private String clientId;
    private String redirectUri;
    private String scope;

}
