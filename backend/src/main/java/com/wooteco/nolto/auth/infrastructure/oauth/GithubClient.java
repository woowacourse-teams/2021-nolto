package com.wooteco.nolto.auth.infrastructure.oauth;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.auth.infrastructure.oauth.dto.GithubUserResponse;
import com.wooteco.nolto.auth.ui.dto.OAuthTokenResponse;
import com.wooteco.nolto.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class GithubClient extends OAuthClientDetail {

    private static final String GITHUB_USERINFO_REQUEST_URL = "https://api.github.com/user";
    private static final String GITHUB_TOKEN_REQUEST_URL = "https://github.com/login/oauth/access_token";

    private final GithubOAuthInfo githubOAuthInfo;

    @Override
    public User generateUserInfo(OAuthTokenResponse oauthToken) {
        return super.requestUserInfo(oauthToken, GITHUB_USERINFO_REQUEST_URL, GithubUserResponse.class);
    }

    @Override
    public OAuthTokenResponse generateAccessToken(String code) {
        return super.requestAccessToken(code, GITHUB_TOKEN_REQUEST_URL);
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
