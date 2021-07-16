package com.wooteco.nolto.auth.application;

import com.wooteco.nolto.AuthorizationException;
import com.wooteco.nolto.NotFoundException;
import com.wooteco.nolto.auth.infrastructure.JwtTokenProvider;
import com.wooteco.nolto.auth.ui.dto.TokenRequest;
import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse login(TokenRequest tokenRequest) {
        User findUser = getFindUser(tokenRequest.getEmail());

        findUser.checkPassword(tokenRequest.getPassword());
        String token = jwtTokenProvider.createToken(findUser.getEmail());
        return new TokenResponse(token);
    }

    public User findUserByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);
        return getFindUser(payload);
    }

    private User getFindUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("로그인에 실패하였습니다."));
    }

    public void validateToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException("유효하지 않은 토큰입니다.");
        }
    }
}
