package com.wooteco.nolto.user.application;

import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import com.wooteco.nolto.user.ui.dto.NicknameValidationResponse;
import com.wooteco.nolto.user.ui.dto.ProfileResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public NicknameValidationResponse validateDuplicated(String nickname) {
        boolean isExistNickname = userRepository.existsByNickName(nickname);
        return new NicknameValidationResponse(!isExistNickname);
    }

    public ProfileResponse findProfile(User user) {
        // TODO user의 notifications 수 구하는 로직 필요, user도 가지고 있으면 Service 레이어까지 오지 않을 수 있음
        return ProfileResponse.of(user, 0);
    }
}
