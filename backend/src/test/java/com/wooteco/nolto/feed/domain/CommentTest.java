package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTest {

    public static final Comment COMMENT1 = new Comment("첫 댓글", false).writtenBy(UserTest.USER);

    private Comment comment1;
    private Comment comment2;

    @BeforeEach
    void setUp() {
        User user1 = new User(1L, "SOCIAL_ID", SocialType.GITHUB, "NICKNAME", "IMAGE");
        User user2 = new User(2L, "SOCIAL_ID2", SocialType.GITHUB, "NICKNAME2", "IMAGE2");
        Feed feed = new Feed(
                "title",
                "content",
                Step.PROGRESS,
                true,
                "https://github.com/woowacourse-teams/2021-nolto",
                "https://github.com/woowacourse-teams/2021-nolto",
                "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png").writtenBy(user1);
        comment1 = new Comment("첫 댓글", false).writtenBy(user1);
        comment1.setFeed(feed);
        comment2 = new Comment("두 번째 댓글", false).writtenBy(user2);
        comment2.setFeed(feed);
    }

    @DisplayName("피드의 작성자와 댓글의 작성자가 같은지 확인한다.")
    @Test
    void isFeedAuthor() {
        // when
        boolean result1 = comment1.isFeedAuthor();
        boolean result2 = comment2.isFeedAuthor();

        // then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }
}