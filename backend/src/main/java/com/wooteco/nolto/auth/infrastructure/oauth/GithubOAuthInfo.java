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
    private String client_id;

    @Value("${oauth.github.client.secret}")
    private String client_secret;

    @Value("${oauth.github.scope}")
    private String scope;

    @Value("${oauth.github.redirect-uri}")
    private String redirect_uri;

    @Override
    public String getResponse_type() {
        return "";
    }

    @Override
    public boolean checkType(SocialType type) {
        return SocialType.GITHUB.equals(type);
    }

    @Override
    public MultiValueMap<String, String> generateAccessTokenRequestParam(String code) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("client_id", client_id);
        param.add("client_secret", client_secret);
        param.add("redirect_uri", redirect_uri);
        param.add("code", code);
        return param;
    }
}
