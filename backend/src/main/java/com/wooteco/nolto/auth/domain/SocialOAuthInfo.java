package com.wooteco.nolto.auth.domain;

import org.springframework.util.MultiValueMap;

public interface SocialOAuthInfo {
    String getClient_id();

    String getClient_secret();

    String getRedirect_uri();

    String getScope();

    String getResponse_type();

    boolean checkType(SocialType type);

    MultiValueMap<String, String> generateAccessTokenRequestParam(String code);
}
