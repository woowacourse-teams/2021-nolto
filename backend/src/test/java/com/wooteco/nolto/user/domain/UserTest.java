package com.wooteco.nolto.user.domain;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Like;
import com.wooteco.nolto.feed.domain.Step;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    private static String SOCIAL_ID = "123456L";
    private static SocialType GITHUB_SOCIAL_TYPE = SocialType.GITHUB;
    private static String NICKNAME = "nickname";
    private static String IMAGE = "sample-image.png";

    public static User USER = new User(SOCIAL_ID, GITHUB_SOCIAL_TYPE, NICKNAME, IMAGE);

    @DisplayName("멤버가 해당 피드에 좋아요를 눌렀는지 검증한다.")
    @Test
    void isLiked() {
        User user = new User( SOCIAL_ID, SocialType.GITHUB, NICKNAME, IMAGE);
        Feed feed = new Feed("title", "content", Step.PROGRESS, true, "", "", "");
        feed.writtenBy(user);

        user.getLikes().add(new Like(user, feed));

        assertThat(user.isLiked(feed)).isTrue();
    }

    @DisplayName("멤버가 해당 피드에 좋아요를 안 눌렀는지 검증한다.")
    @Test
    void isNotLiked() {
        User user1 = new User( SOCIAL_ID, SocialType.GITHUB, NICKNAME, IMAGE);
        User user2 = new User("98765L", SocialType.GOOGLE, "amazzi", IMAGE);
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