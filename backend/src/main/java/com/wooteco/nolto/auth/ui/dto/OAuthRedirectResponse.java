package com.wooteco.nolto.auth.ui.dto;

import com.wooteco.nolto.auth.domain.SocialOAuthInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthRedirectResponse {
    private final String client_id;
    private final String redirect_uri;
    private final String scope;
    private final String response_type;

    public static OAuthRedirectResponse of(SocialOAuthInfo socialOAuthInfo) {
        return new OAuthRedirectResponse(
                socialOAuthInfo.getClient_id(),
                socialOAuthInfo.getRedirect_uri(),
                socialOAuthInfo.getScope(),
                socialOAuthInfo.getResponse_type()
        );
    }
}
