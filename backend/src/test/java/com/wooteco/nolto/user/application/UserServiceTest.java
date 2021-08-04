package com.wooteco.nolto.user.application;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import com.wooteco.nolto.user.ui.dto.NicknameValidationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public static final String 존재하는_닉네임 = "존재하는 닉네임";
    public static final String 존재하지않는_닉네임 = "존재하지않는 닉네임";

    private User 존재하는_유저 = new User(
            "1",
            SocialType.GITHUB,
            존재하는_닉네임,
            "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png");

    @BeforeEach
    void setUp() {
        userRepository.save(존재하는_유저);
    }

    @DisplayName("닉네임의 중복 여부를 검사한다.")
    @Test
    void validateDuplicated() {
        // when
        NicknameValidationResponse 사용불가능한_닉네임 = userService.validateDuplicated(존재하는_닉네임);
        NicknameValidationResponse 사용가능한_닉네임 = userService.validateDuplicated(존재하지않는_닉네임);

        // then
        assertThat(사용불가능한_닉네임).extracting("isUsable").isEqualTo(false);
        assertThat(사용가능한_닉네임).extracting("isUsable").isEqualTo(true);
    }
}