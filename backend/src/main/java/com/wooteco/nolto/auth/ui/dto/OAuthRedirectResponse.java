package com.wooteco.nolto.auth.ui.dto;

import com.wooteco.nolto.auth.domain.SocialOAuthInfo;
import lombok.Getter;

@Getter
public class OAuthRedirectResponse {
    private final String client_id;
    private final String redirect_uri;
    private final String scope;
    private final String response_type;

    public OAuthRedirectResponse(String client_id, String redirect_uri, String scope, String response_type) {
        this.client_id = client_id;
        this.redirect_uri = redirect_uri;
        this.scope = scope;
        this.response_type = response_type;
    }

    public static OAuthRedirectResponse of(SocialOAuthInfo socialOAuthInfo) {
        return new OAuthRedirectResponse(
                socialOAuthInfo.getClientId(),
                socialOAuthInfo.getRedirectUri(),
                socialOAuthInfo.getScope(),
                socialOAuthInfo.getResponseType()
        );
    }
}
