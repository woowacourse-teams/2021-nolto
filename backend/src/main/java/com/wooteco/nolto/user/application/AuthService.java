package com.wooteco.nolto.user.application;

import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import com.wooteco.nolto.user.infrastructure.JwtTokenProvider;
import com.wooteco.nolto.user.ui.dto.TokenRequest;
import com.wooteco.nolto.user.ui.dto.TokenResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;

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
}
