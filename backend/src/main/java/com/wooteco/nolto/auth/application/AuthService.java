package com.wooteco.nolto.auth.application;

import com.wooteco.nolto.AuthorizationException;
import com.wooteco.nolto.NotFoundException;
import com.wooteco.nolto.auth.infrastructure.JwtTokenProvider;
import com.wooteco.nolto.auth.ui.dto.GithubUserResponse;
import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse loginGithub(GithubUserResponse githubUser) {
        Optional<User> findUser = userRepository.findBySocialIdAndSocialType(githubUser.getId(), "github");
        User user = findUser.orElse(signUp(githubUser));

        String token = jwtTokenProvider.createToken(String.valueOf(user.getId()));
        return new TokenResponse(token);
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

    public User signUp(GithubUserResponse githubUser) {
        User user = new User(githubUser.getId(), "github", githubUser.getName(), githubUser.getAvatar_url());
        return userRepository.save(user);
    }
}
