package com.wooteco.nolto.user.application;

import com.wooteco.nolto.user.domain.UserRepository;
import com.wooteco.nolto.user.ui.dto.NicknameValidationResponse;
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
}
