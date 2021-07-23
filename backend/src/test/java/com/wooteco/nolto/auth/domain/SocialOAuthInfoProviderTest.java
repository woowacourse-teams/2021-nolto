package com.wooteco.nolto.auth.domain;

import com.wooteco.nolto.auth.infrastructure.oauth.GithubOAuthInfo;
import com.wooteco.nolto.auth.infrastructure.oauth.GoogleOAuthInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {GithubOAuthInfo.class, GoogleOAuthInfo.class, SocialOAuthInfoProvider.class})
class SocialOAuthInfoProviderTest {

    @Autowired
    private SocialOAuthInfoProvider socialOAuthInfoProvider;

    @DisplayName("SocialType.GITHUB 에 맞는 GithubOAuthInfo 객체를 제공해준다.")
    @Test
    void provideGithubOAuth() {
        SocialOAuthInfo socialOAuthInfo = socialOAuthInfoProvider.provideSocialOAuthInfoBy(SocialType.GITHUB);

        assertThat(socialOAuthInfo).isInstanceOf(GithubOAuthInfo.class);
    }

    @DisplayName("SocialType.GOOGLE 에 맞는 GoogleOAuthInfo 객체를 제공해준다.")
    @Test
    void provideGoogleOAuth() {
        SocialOAuthInfo socialOAuthInfo = socialOAuthInfoProvider.provideSocialOAuthInfoBy(SocialType.GOOGLE);

        assertThat(socialOAuthInfo).isInstanceOf(GoogleOAuthInfo.class);
    }
}