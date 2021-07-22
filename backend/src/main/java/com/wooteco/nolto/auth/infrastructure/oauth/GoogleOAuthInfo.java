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
    private String client_id;

    @Value("${oauth.google.client.secret}")
    private String client_secret;

    @Value("${oauth.google.scope}")
    private String scope;

    @Value("${oauth.google.redirect-uri}")
    private String redirect_uri;

    private final String response_type = "code";
    private final String grant_type = "authorization_code";

    @Override
    public boolean checkType(SocialType type) {
        return SocialType.GOOGLE.equals(type);
    }

    public MultiValueMap<String, String> generateAccessTokenRequestParam(String code) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("client_id", client_id);
        param.add("client_secret", client_secret);
        param.add("code", code);
        param.add("redirect_uri", redirect_uri);
        param.add("grant_type", grant_type);
        return param;
    }
}
