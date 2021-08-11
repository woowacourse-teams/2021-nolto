package com.wooteco.nolto.auth.application;

import com.wooteco.nolto.auth.domain.OAuthClientProvider;
import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.auth.infrastructure.oauth.GithubClient;
import com.wooteco.nolto.auth.infrastructure.oauth.GoogleClient;
import com.wooteco.nolto.auth.ui.dto.OAuthRedirectResponse;
import com.wooteco.nolto.auth.ui.dto.OAuthTokenResponse;
import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import com.wooteco.nolto.exception.BadRequestException;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@Transactional
@SpringBootTest
class AuthServiceTest {

    private static final OAuthTokenResponse OAUTH_TOKEN_RESPONSE1 =
            new OAuthTokenResponse("Bearer", "read:user", "access_token");
    private static final OAuthTokenResponse OAUTH_TOKEN_RESPONSE2 =
            new OAuthTokenResponse("Bearer", "read:user", "access_token");
    public static final String USER_NICKNAME = "user";
    private static final User USER1 = new User("socialId1", SocialType.GITHUB, USER_NICKNAME, "imageUrl");
    private static final User USER2 = new User("socialId2", SocialType.GITHUB, USER_NICKNAME, "imageUrl");

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private OAuthClientProvider oAuthClientProvider;

    @MockBean
    private GithubClient githubClient;

    @MockBean
    private GoogleClient googleClient;

    @DisplayName("깃허브 로그인의 code를 얻기위한 파라미터들을 요청한다.")
    @Test
    void requestGithubRedirect() {
        OAuthRedirectResponse response = authService.requestSocialRedirect("github");

        assertThat(response.getClient_id()).isNotNull();
        assertThat(response.getScope()).isNotNull();
        assertThat(response.getRedirect_uri()).isNotNull();
    }

    @DisplayName("구글 로그인의 code를 얻기위한 파라미터들을 요청한다.")
    @Test
    void requestGoogleRedirect() {
        OAuthRedirectResponse response = authService.requestSocialRedirect("google");

        assertThat(response.getClient_id()).isNotNull();
        assertThat(response.getScope()).isNotNull();
        assertThat(response.getRedirect_uri()).isNotNull();
        assertThat(response.getResponse_type()).isNotNull();
    }

    @DisplayName("지원하지 않는 소셜 로그인 서비스로 code를 얻기위한 파라미터들을 요청하면 예외가 발생한다.")
    @Test
    void requestNaverRedirect() {
        assertThatThrownBy(() -> authService.requestSocialRedirect("naver"))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorType.NOT_SUPPORTED_SOCIAL_LOGIN.getMessage());
    }

    @DisplayName("깃허브 로그인으로 로그인에 성공하면 토큰을 반환해준다.")
    @Test
    void githubSignIn() {
        given(oAuthClientProvider.provideOAuthClientBy(SocialType.GITHUB)).willReturn(githubClient);
        given(githubClient.generateAccessToken("code")).willReturn(OAUTH_TOKEN_RESPONSE1);
        given(githubClient.generateUserInfo(OAUTH_TOKEN_RESPONSE1)).willReturn(USER1);

        TokenResponse tokenResponse = authService.oAuthSignIn("github", "code");

        assertThat(tokenResponse).isNotNull();
    }

    @DisplayName("구글 로그인으로 로그인에 성공하면 토큰을 반환해준다.")
    @Test
    void googleSignIn() {
        given(oAuthClientProvider.provideOAuthClientBy(SocialType.GOOGLE)).willReturn(githubClient);
        given(githubClient.generateAccessToken("code")).willReturn(OAUTH_TOKEN_RESPONSE1);
        given(githubClient.generateUserInfo(OAUTH_TOKEN_RESPONSE1)).willReturn(USER1);

        TokenResponse tokenResponse = authService.oAuthSignIn("google", "code");

        assertThat(tokenResponse).isNotNull();
    }

    @DisplayName("code 요청값이 null이거나 빈값이면 예외가 발생한다.")
    @Test
    void socialSignInCode() {
        given(oAuthClientProvider.provideOAuthClientBy(SocialType.GOOGLE)).willReturn(githubClient);
        given(githubClient.generateAccessToken("code")).willReturn(OAUTH_TOKEN_RESPONSE1);
        given(githubClient.generateUserInfo(OAUTH_TOKEN_RESPONSE1)).willReturn(USER1);

        assertThatThrownBy(() -> authService.oAuthSignIn("google", null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorType.INVALID_OAUTH_CODE.getMessage());

        assertThatThrownBy(() -> authService.oAuthSignIn("google", ""))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorType.INVALID_OAUTH_CODE.getMessage());
    }

    @DisplayName("지원하지 않는 소셜 타입으로 로그인을 요청하면 예외가 발생한다.")
    @Test
    void unSupportedSocialSignIn() {
        given(oAuthClientProvider.provideOAuthClientBy(SocialType.GITHUB)).willReturn(githubClient);
        given(githubClient.generateAccessToken("code")).willReturn(OAUTH_TOKEN_RESPONSE1);
        given(githubClient.generateUserInfo(OAUTH_TOKEN_RESPONSE1)).willReturn(USER1);

        assertThatThrownBy(() -> authService.oAuthSignIn("naver", "code"))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorType.NOT_SUPPORTED_SOCIAL_LOGIN.getMessage());
    }

    @DisplayName("중복된 닉네임이 있을 경우 원래 닉네임에 식별자를 붙여 가입한다.")
    @Test
    void changeForUniqueNickname() {
        // given
        given(oAuthClientProvider.provideOAuthClientBy(SocialType.GOOGLE)).willReturn(githubClient);
        given(githubClient.generateAccessToken("code1")).willReturn(OAUTH_TOKEN_RESPONSE1);
        given(githubClient.generateAccessToken("code2")).willReturn(OAUTH_TOKEN_RESPONSE2);
        given(githubClient.generateUserInfo(OAUTH_TOKEN_RESPONSE1)).willReturn(USER1);
        given(githubClient.generateUserInfo(OAUTH_TOKEN_RESPONSE2)).willReturn(USER2);
        User existNickNameUser = new User("socialId3", SocialType.GITHUB, USER_NICKNAME, "imageUrl");
        userRepository.save(existNickNameUser);

        authService.oAuthSignIn("google", "code1");
        authService.oAuthSignIn("google", "code2");

        boolean 맨처음_저장된_중복_닉네임_존재여부 = userRepository.existsByNickName(USER_NICKNAME);
        boolean 두번째_저장된_중복_닉네임_존재여부 = userRepository.existsByNickName(USER_NICKNAME + "(1)");
        boolean 세번째_저장된_중복_닉네임_존재여부 = userRepository.existsByNickName(USER_NICKNAME + "(2)");

        assertThat(맨처음_저장된_중복_닉네임_존재여부).isTrue();
        assertThat(두번째_저장된_중복_닉네임_존재여부).isTrue();
        assertThat(세번째_저장된_중복_닉네임_존재여부).isTrue();
    }
}