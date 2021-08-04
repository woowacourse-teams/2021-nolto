package com.wooteco.nolto.auth.application;

import com.wooteco.nolto.auth.domain.*;
import com.wooteco.nolto.auth.infrastructure.JwtTokenProvider;
import com.wooteco.nolto.auth.ui.dto.OAuthRedirectResponse;
import com.wooteco.nolto.auth.ui.dto.OAuthTokenResponse;
import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import com.wooteco.nolto.exception.BadRequestException;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.NotFoundException;
import com.wooteco.nolto.exception.UnauthorizedException;
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

    public static final String IDENTIFIER = "(%d)";

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
        this.validateCode(code);
        SocialType socialType = SocialType.findBy(socialTypeName);
        OAuthClient oAuthClient = oAuthClientProvider.provideOAuthClientBy(socialType);
        OAuthTokenResponse token = oAuthClient.generateAccessToken(code);
        User user = oAuthClient.generateUserInfo(token);
        return createToken(Objects.requireNonNull(user));
    }

    private void validateCode(String code) {
        if (Objects.isNull(code) || code.isEmpty()) {
            throw new BadRequestException(ErrorType.INVALID_OAUTH_CODE);
        }
    }

    private TokenResponse createToken(User user) {
        Optional<User> userOptional = userRepository.findBySocialIdAndSocialType(user.getSocialId(), user.getSocialType());
        User findUser = userOptional.orElseGet(() -> signUp(user));

        String token = jwtTokenProvider.createToken(String.valueOf(findUser.getId()));
        return new TokenResponse(token);
    }

    private User signUp(User user) {
        changeForUniqueNickname(user);
        return userRepository.save(user);
    }

    private void changeForUniqueNickname(User user) {
        int identifier = 0;
        String originNickName = user.getNickName();
        String targetNickName = originNickName;
        while (userRepository.existsByNickName(targetNickName)) {
            targetNickName = originNickName + String.format(IDENTIFIER, ++identifier);
        }
        user.changeNickName(targetNickName);
    }

    public User findUserByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);
        return getFindUser(Long.valueOf(payload));
    }

    private User getFindUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorType.USER_NOT_FOUND));
    }

    public void validateToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new UnauthorizedException(ErrorType.INVALID_TOKEN);
        }
    }
}
