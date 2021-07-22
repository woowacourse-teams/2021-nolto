package com.wooteco.nolto.auth.infrastructure.oauth;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.auth.infrastructure.oauth.dto.GoogleUserResponse;
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
public class GoogleClient extends OAuthClientDetail {

    private final static String GOOGLE_USERINFO_REQUEST_URL = "https://openidconnect.googleapis.com/v1/userinfo";
    private final static String GOOGLE_TOKEN_REQUEST_URL = "https://oauth2.googleapis.com/token";

    private final GoogleOAuthInfo googleOAuthInfo;

    @Override
    public User generateUserInfo(OAuthTokenResponse oauthToken) {
        HttpHeaders headers = requestUserInfoHeaders(oauthToken);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        GoogleUserResponse googleUserResponse = restTemplate.exchange(
                GOOGLE_USERINFO_REQUEST_URL,
                HttpMethod.GET,
                httpEntity,
                GoogleUserResponse.class
        ).getBody();
        return Objects.requireNonNull(googleUserResponse).toUser();
    }

    @Override
    public OAuthTokenResponse generateAccessToken(String code) {
        HttpEntity<MultiValueMap<String, String>> request = this.generateAccessTokenRequest(code);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                GOOGLE_TOKEN_REQUEST_URL,
                HttpMethod.POST,
                request,
                OAuthTokenResponse.class
        ).getBody();
    }

    @Override
    public boolean checkType(SocialType socialType) {
        return SocialType.GOOGLE.equals(socialType);
    }

    @Override
    protected MultiValueMap<String, String> generateAccessTokenRequestParam(String code) {
        return googleOAuthInfo.generateAccessTokenRequestParam(code);
    }
}
