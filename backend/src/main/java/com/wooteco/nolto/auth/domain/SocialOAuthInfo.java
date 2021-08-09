package com.wooteco.nolto.auth.domain;

import org.springframework.util.MultiValueMap;

public interface SocialOAuthInfo {
    String getClientId();

    String getClientSecret();

    String getRedirectUri();

    String getScope();

    String getResponseType();

    boolean checkType(SocialType type);

    MultiValueMap<String, String> generateAccessTokenRequestParam(String code);
}
