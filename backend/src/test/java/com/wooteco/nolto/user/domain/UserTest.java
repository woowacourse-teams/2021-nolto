package com.wooteco.nolto.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class UserTest {
    private static String EMAIL = "email@email.com";
    private static String PASSWORD = "password";
    private static String NICKNAME = "nickname";

    public static User USER = new User(null, EMAIL, PASSWORD, NICKNAME, new ArrayList<>(), new ArrayList<>());

    @Test
    void checkPassword() {
        // given
        User user = new User(null, EMAIL, PASSWORD, NICKNAME, Collections.emptyList(), Collections.emptyList());

        // when then
        assertDoesNotThrow(() -> user.checkPassword(PASSWORD));
    }

    @DisplayName("비밀번호가 일치 하지 않으면 예외가 발생한다.")
    @Test
    void invalidPassword() {
        // given
        User user = new User(null, EMAIL, "passWORD", NICKNAME, Collections.emptyList(), Collections.emptyList());

        // when then
        assertThatThrownBy(() -> user.checkPassword(PASSWORD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("로그인에 실패하였습니다.");
    }
}