package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class FeedTest {

    public static Feed FEED = new Feed(new ArrayList<>(), "title", "content", Step.PROGRESS, true, "", "", "");

    private Feed feed1;

    @BeforeEach
    void setUp() {
        feed1 = new Feed(new ArrayList<>(), "title", "content", Step.PROGRESS, true,
                "", "", "");
    }

    @DisplayName("피드에 작성자 추가할 수 있다.")
    @Test
    void writtenBy() {
        // when
        Feed feed = feed1.writtenBy(UserTest.USER);

        // then
        assertThat(feed.getAuthor()).isEqualTo(UserTest.USER);
    }

    @DisplayName("해당 피드에 사용자가 좋아요를 눌렀는지 검증할 수 있다.")
    @Test
    void likedByUser() {
        Feed feed = feed1.writtenBy(UserTest.USER);

        User likedUser = new User(1L, "EMAIL", "PASSWORD", "JOEL");
        feed.getLikes().add(new Like(likedUser, feed));

        assertThat(feed.isLikedByUser(likedUser)).isTrue();
    }


    @DisplayName("해당 피드에 사용자가 좋아요를 누르지 않았는지 검증할 수 있다.")
    @Test
    void notLikedByUser() {
        Feed feed = feed1.writtenBy(UserTest.USER);

        User likedUser = new User(1L, "EMAIL", "PASSWORD", "JOEL");
        feed.getLikes().add(new Like(likedUser, feed));

        User notLikedUser = new User(2L, "EMAIL", "PASSWORD", "POMO");
        assertThat(feed.isLikedByUser(notLikedUser)).isFalse();
    }
}
