package com.wooteco.nolto.auth.infrastructure.oauth;

import com.wooteco.nolto.SocialAccessException;
import com.wooteco.nolto.auth.domain.OAuthClient;
import com.wooteco.nolto.auth.infrastructure.oauth.dto.GithubUserResponse;
import com.wooteco.nolto.auth.infrastructure.oauth.dto.OAuthUserResponse;
import com.wooteco.nolto.auth.ui.dto.OAuthTokenResponse;
import com.wooteco.nolto.user.domain.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

public abstract class OAuthClientDetail implements OAuthClient {
    private final static String HEADER_NAME_AUTHORIZATION = "Authorization";
    private final static String HEADER_NAME_CONTENT_TYPE = "Content-type";
    private final static String HEADER_VALUE_CONTENT_TYPE = "application/x-www-form-urlencoded;charset-utf-8";

    protected abstract MultiValueMap<String, String> generateAccessTokenRequestParam(String code);

    public User requestUserInfo(OAuthTokenResponse oauthToken, String userInfoRequestUrl, Class<? extends OAuthUserResponse> responseType) {
        HttpHeaders headers = requestUserInfoHeaders(oauthToken);
        RestTemplate restTemplate = new RestTemplate();
        try {
            OAuthUserResponse userResponse = restTemplate.exchange(
                    userInfoRequestUrl,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    responseType
            ).getBody();
            return Objects.requireNonNull(userResponse).toUser();
        } catch (HttpStatusCodeException e) {
            throw new SocialAccessException("로그인 요청에 실패했습니다.");
        }
    }

    public OAuthTokenResponse requestAccessToken(String code, String tokenRequestUrl) {
        HttpEntity<MultiValueMap<String, String>> request = this.generateAccessTokenRequest(code);
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.exchange(
                    tokenRequestUrl,
                    HttpMethod.POST,
                    request,
                    OAuthTokenResponse.class
            ).getBody();
        } catch (HttpStatusCodeException e) {
            throw new SocialAccessException("로그인 요청에 실패했습니다.");
        }
    }

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
