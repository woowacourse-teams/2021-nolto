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
public class GithubOAuthInfo implements SocialOAuthInfo {

    @Value("${oauth.github.client.id}")
    private String clientId;

    @Value("${oauth.github.client.secret}")
    private String clientSecret;

    @Value("${oauth.github.scope}")
    private String scope;

    @Value("${oauth.github.redirect-uri}")
    private String redirectUri;

    @Override
    public String getResponseType() {
        return "";
    }

    @Override
    public boolean checkType(SocialType type) {
        return SocialType.GITHUB.equals(type);
    }

    @Override
    public MultiValueMap<String, String> generateAccessTokenRequestParam(String code) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("client_id", clientId);
        param.add("client_secret", clientSecret);
        param.add("redirect_uri", redirectUri);
        param.add("code", code);
        return param;
    }
}
