package com.wooteco.nolto.user.domain;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Like;
import com.wooteco.nolto.feed.domain.Step;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class UserTest {
    private static String EMAIL = "email@email.com";
    private static String PASSWORD = "password";
    private static String NICKNAME = "nickname";
    private static String IMAGE = "sample-image.png";

    public static User USER = new User(EMAIL, PASSWORD, NICKNAME, IMAGE);

    @Test
    void checkPassword() {
        // given
        User user = new User(null, EMAIL, PASSWORD, NICKNAME, IMAGE);

        // when then
        assertDoesNotThrow(() -> user.checkPassword(PASSWORD));
    }

    @DisplayName("비밀번호가 일치 하지 않으면 예외가 발생한다.")
    @Test
    void invalidPassword() {
        // given
        User user = new User(null, EMAIL, "passWORD", NICKNAME, IMAGE);

        // when then
        assertThatThrownBy(() -> user.checkPassword(PASSWORD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("로그인에 실패하였습니다.");
    }

    @DisplayName("멤버가 해당 피드에 좋아요를 눌렀는지 검증한다.")
    @Test
    void isLiked() {
        User user = new User(null, EMAIL, "passWORD", NICKNAME, IMAGE);
        Feed feed = new Feed("title", "content", Step.PROGRESS, true, "", "", "");
        feed.writtenBy(user);

        user.getLikes().add(new Like(user, feed));

        assertThat(user.isLiked(feed)).isTrue();
    }

    @DisplayName("멤버가 해당 피드에 좋아요를 안 눌렀는지 검증한다.")
    @Test
    void isNotLiked() {
        User user1 = new User(null, EMAIL, "passWORD", NICKNAME, IMAGE);
        User user2 = new User(null, "woowa@email.com", "passWORD", "amazzi", IMAGE);
        Feed feed = new Feed("title", "content", Step.PROGRESS, true, "", "", "");
        feed.writtenBy(user1);

        user1.getLikes().add(new Like(user1, feed));

        assertThat(user2.isLiked(feed)).isFalse();
    }

    @DisplayName("게스트가 해당 피드에 좋아요를 눌렀는지 검증하면 무조건 false이다.")
    @Test
    void isLikedWithGuest() {
        Feed feed = new Feed("title", "content", Step.PROGRESS, true, "", "", "");

        User guestUser = User.GUEST_USER;

        assertThat(guestUser.isLiked(feed)).isFalse();
    }
}