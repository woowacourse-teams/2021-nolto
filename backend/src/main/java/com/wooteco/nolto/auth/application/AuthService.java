package com.wooteco.nolto.auth.application;

import com.wooteco.nolto.auth.domain.*;
import com.wooteco.nolto.auth.infrastructure.JwtTokenProvider;
import com.wooteco.nolto.auth.infrastructure.RedisRepository;
import com.wooteco.nolto.auth.ui.dto.*;
import com.wooteco.nolto.exception.BadRequestException;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.NotFoundException;
import com.wooteco.nolto.exception.UnauthorizedException;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class AuthService {

    public static final String IDENTIFIER = "(%d)";

    private final SocialOAuthInfoProvider socialOAuthInfoProvider;
    private final OAuthClientProvider oAuthClientProvider;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisRepository redisRepository;

    public OAuthRedirectResponse requestSocialRedirect(String socialTypeName) {
        SocialType socialType = SocialType.findBy(socialTypeName);
        SocialOAuthInfo socialOauthInfo = socialOAuthInfoProvider.provideSocialOAuthInfoBy(socialType);
        return OAuthRedirectResponse.of(socialOauthInfo);
    }

    public AllTokenResponse oAuthSignIn(String socialTypeName, String code, String clientIP) {
        this.validateCode(code);
        SocialType socialType = SocialType.findBy(socialTypeName);
        OAuthClient oAuthClient = oAuthClientProvider.provideOAuthClientBy(socialType);
        OAuthTokenResponse token = oAuthClient.generateAccessToken(code);
        User user = oAuthClient.generateUserInfo(token);
        return createToken(Objects.requireNonNull(user), clientIP);
    }

    private void validateCode(String code) {
        if (Objects.isNull(code) || code.isEmpty()) {
            throw new BadRequestException(ErrorType.INVALID_OAUTH_CODE);
        }
    }

    private AllTokenResponse createToken(User user, String clientIP) {
        User findUser = userRepository.findBySocialIdAndSocialType(
                        user.getSocialId(),
                        user.getSocialType()
                ).orElseGet(() -> signUp(user));
        return getTokenResponse(findUser.getId(), clientIP);
    }

    private AllTokenResponse getTokenResponse(long userId, String clientIP) {
        TokenResponse accessToken = jwtTokenProvider.createToken(String.valueOf(userId));
        TokenResponse refreshToken = jwtTokenProvider.createRefreshToken(UUID.randomUUID().toString());
        redisRepository.set(refreshToken.getValue(), clientIP, String.valueOf(userId), refreshToken.getExpiredIn());
        return new AllTokenResponse(accessToken, refreshToken);
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

    public AllTokenResponse refreshToken(RefreshTokenRequest request) {
        if (!redisRepository.exist(request.getRefreshToken())) {
            log.info("redis doesn't have the refresh token.");
            throw new BadRequestException(ErrorType.INVALID_TOKEN);
        }

        if (!redisRepository.leftPop(request.getRefreshToken()).equals(request.getClientIP())) {
            log.info("invalid request client ip for refresh token. request client : " + request.getClientIP());
            throw new UnauthorizedException(ErrorType.INVALID_CLIENT);
        }

        String userId = redisRepository.leftPop(request.getRefreshToken());
        return getTokenResponse(Long.parseLong(userId), request.getClientIP());
    }
}
