package com.wooteco.nolto.auth.infrastructure.oauth;

import com.wooteco.nolto.auth.domain.OAuthClient;
import com.wooteco.nolto.auth.ui.dto.OAuthTokenResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

public abstract class OAuthClientDetail implements OAuthClient {
    private final static String HEADER_NAME_AUTHORIZATION = "Authorization";
    private final static String HEADER_NAME_CONTENT_TYPE = "Content-type";
    private final static String HEADER_VALUE_CONTENT_TYPE = "application/x-www-form-urlencoded;charset-utf-8";

    protected abstract MultiValueMap<String, String> generateAccessTokenRequestParam(String code);

    protected HttpHeaders requestUserInfoHeaders(OAuthTokenResponse oauthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_NAME_AUTHORIZATION, oauthToken.getToken_type() + " " + oauthToken.getAccess_token());
        headers.add(HEADER_NAME_CONTENT_TYPE, HEADER_VALUE_CONTENT_TYPE);
        return headers;
    }

    protected HttpEntity<MultiValueMap<String, String>> generateAccessTokenRequest(String code) {
        MultiValueMap<String, String> param = this.generateAccessTokenRequestParam(code);
        HttpHeaders headers = this.generateAccessTokenRequestHeaders();
        return new HttpEntity<>(param, headers);
    }

    protected HttpHeaders generateAccessTokenRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_NAME_CONTENT_TYPE, HEADER_VALUE_CONTENT_TYPE);
        return headers;
    }
}
