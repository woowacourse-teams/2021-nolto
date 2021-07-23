package com.wooteco.nolto.auth.domain;

import com.wooteco.nolto.auth.infrastructure.oauth.GithubClient;
import com.wooteco.nolto.auth.infrastructure.oauth.GithubOAuthInfo;
import com.wooteco.nolto.auth.infrastructure.oauth.GoogleClient;
import com.wooteco.nolto.auth.infrastructure.oauth.GoogleOAuthInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {GithubOAuthInfo.class, GoogleOAuthInfo.class, GithubClient.class, GoogleClient.class, OAuthClientProvider.class})
class OAuthClientProviderTest {

    @Autowired
    private OAuthClientProvider oAuthClientProvider;

    @DisplayName("SocialType.GITHUB 로 GithubClient 객체를 제공해준다.")
    @Test
    void provideGithubClientBy() {
        // when
        OAuthClient oAuthClient = oAuthClientProvider.provideOAuthClientBy(SocialType.GITHUB);

        // then
        assertThat(oAuthClient).isInstanceOf(GithubClient.class);
    }

    @DisplayName("SocialType.GOOGLE 로 GoogleClient 객체를 제공해준다.")
    @Test
    void provideGoogleClientBy() {
        // when
        OAuthClient oAuthClient = oAuthClientProvider.provideOAuthClientBy(SocialType.GOOGLE);

        // then
        assertThat(oAuthClient).isInstanceOf(GoogleClient.class);
    }
}