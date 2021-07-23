package com.wooteco.nolto.auth.infrastructure.oauth;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.auth.infrastructure.oauth.dto.GoogleUserResponse;
import com.wooteco.nolto.auth.ui.dto.OAuthTokenResponse;
import com.wooteco.nolto.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class GoogleClient extends OAuthClientDetail {

    private final static String GOOGLE_USERINFO_REQUEST_URL = "https://openidconnect.googleapis.com/v1/userinfo";
    private final static String GOOGLE_TOKEN_REQUEST_URL = "https://oauth2.googleapis.com/token";

    private final GoogleOAuthInfo googleOAuthInfo;

    @Override
    public User generateUserInfo(OAuthTokenResponse oauthToken) {
        return super.requestUserInfo(oauthToken, GOOGLE_USERINFO_REQUEST_URL, GoogleUserResponse.class);
    }

    @Override
    public OAuthTokenResponse generateAccessToken(String code) {
        return super.requestAccessToken(code, GOOGLE_TOKEN_REQUEST_URL);
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
