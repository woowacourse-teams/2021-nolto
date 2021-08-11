package com.wooteco.nolto.auth.infrastructure.oauth;

import com.wooteco.nolto.auth.domain.SocialOAuthInfo;
import com.wooteco.nolto.auth.domain.SocialType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@Component
public class GoogleOAuthInfo implements SocialOAuthInfo {

    @Value("${oauth.google.client.id}")
    private String clientId;

    @Value("${oauth.google.client.secret}")
    private String clientSecret;

    @Value("${oauth.google.scope}")
    private String scope;

    @Value("${oauth.google.redirect-uri}")
    private String redirectUri;

    private static final String RESPONSE_TYPE = "code";
    private static final String GRANT_TYPE = "authorization_code";

    @Override
    public String getResponseType() {
        return RESPONSE_TYPE;
    }

    @Override
    public boolean checkType(SocialType type) {
        return SocialType.GOOGLE.equals(type);
    }

    public MultiValueMap<String, String> generateAccessTokenRequestParam(String code) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("client_id", clientId);
        param.add("client_secret", clientSecret);
        param.add("code", code);
        param.add("redirect_uri", redirectUri);
        param.add("grant_type", GRANT_TYPE);
        return param;
    }
}
