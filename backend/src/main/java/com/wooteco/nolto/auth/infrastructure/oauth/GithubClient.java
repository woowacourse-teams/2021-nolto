package com.wooteco.nolto.auth.infrastructure.oauth;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.auth.infrastructure.oauth.dto.GithubUserResponse;
import com.wooteco.nolto.auth.ui.dto.OAuthTokenResponse;
import com.wooteco.nolto.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GithubClient extends OAuthClientDetail {

    private final static String GITHUB_USERINFO_REQUEST_URL = "https://api.github.com/user";
    private final static String GITHUB_TOKEN_REQUEST_URL = "https://github.com/login/oauth/access_token";

    private final GithubOAuthInfo githubOAuthInfo;

    @Override
    public User generateUserInfo(OAuthTokenResponse oauthToken) {
        HttpHeaders headers = requestUserInfoHeaders(oauthToken);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        GithubUserResponse githubUserResponse = restTemplate.exchange(
                GITHUB_USERINFO_REQUEST_URL,
                HttpMethod.GET,
                httpEntity,
                GithubUserResponse.class
        ).getBody();
        return Objects.requireNonNull(githubUserResponse).toUser();
    }

    @Override
    public OAuthTokenResponse generateAccessToken(String code) {
        HttpEntity<MultiValueMap<String, String>> request = generateAccessTokenRequest(code);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                GITHUB_TOKEN_REQUEST_URL,
                HttpMethod.POST,
                request,
                OAuthTokenResponse.class
        ).getBody();
    }

    @Override
    public boolean checkType(SocialType socialType) {
        return SocialType.GITHUB.equals(socialType);
    }

    @Override
    protected MultiValueMap<String, String> generateAccessTokenRequestParam(String code) {
        return githubOAuthInfo.generateAccessTokenRequestParam(code);
    }
}
