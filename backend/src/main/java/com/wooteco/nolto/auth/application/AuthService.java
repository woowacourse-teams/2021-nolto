package com.wooteco.nolto.auth.application;

import com.wooteco.nolto.AuthorizationException;
import com.wooteco.nolto.NotFoundException;
import com.wooteco.nolto.auth.domain.*;
import com.wooteco.nolto.auth.infrastructure.JwtTokenProvider;
import com.wooteco.nolto.auth.ui.dto.OAuthRedirectResponse;
import com.wooteco.nolto.auth.ui.dto.OAuthTokenResponse;
import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Transactional
@Service
@AllArgsConstructor
public class AuthService {

    private final SocialOAuthInfoProvider socialOAuthInfoProvider;
    private final OAuthClientProvider oAuthClientProvider;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public OAuthRedirectResponse requestSocialRedirect(String socialTypeName) {
        SocialType socialType = SocialType.findBy(socialTypeName);
        SocialOAuthInfo socialOauthInfo = socialOAuthInfoProvider.provideSocialOAuthInfoBy(socialType);
        return OAuthRedirectResponse.of(socialOauthInfo);
    }

    public TokenResponse oAuthSignIn(String socialTypeName, String code) {
        SocialType socialType = SocialType.findBy(socialTypeName);
        OAuthClient oAuthClient = oAuthClientProvider.provideOAuthClientBy(socialType);

        OAuthTokenResponse token = oAuthClient.generateAccessToken(code);
        User user = oAuthClient.generateUserInfo(token);
        return createToken(Objects.requireNonNull(user));
    }

    private TokenResponse createToken(User user) {
        Optional<User> userOptional = userRepository.findBySocialIdAndSocialType(user.getSocialId(), user.getSocialType());
        User findUser = userOptional.orElseGet(() -> signUp(user));

        String token = jwtTokenProvider.createToken(String.valueOf(findUser.getId()));
        return new TokenResponse(token);
    }

    private User signUp(User user) {
        return userRepository.save(user);
    }

    public User findUserByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);
        return getFindUser(Long.valueOf(payload));
    }

    private User getFindUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("로그인에 실패하였습니다."));
    }

    public void validateToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException("유효하지 않은 토큰입니다.");
        }
    }
}
