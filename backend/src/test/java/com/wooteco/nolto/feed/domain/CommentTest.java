package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.wooteco.nolto.UserFixture.ID_있는_유저_생성;
import static org.assertj.core.api.Assertions.assertThat;

class CommentTest {

    private Comment comment1;
    private Comment comment2;

    @BeforeEach
    void setUp() {
        User user1 = ID_있는_유저_생성(1L);
        User user2 = ID_있는_유저_생성(2L);

        Feed feed = Feed.builder()
                .title("title")
                .content("content")
                .step(Step.PROGRESS)
                .isSos(true)
                .storageUrl("https://github.com/woowacourse-teams/2021-nolto")
                .deployedUrl("https://github.com/woowacourse-teams/2021-nolto")
                .thumbnailUrl("https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png")
                .build()
                .writtenBy(user1);
        comment1 = new Comment("첫 댓글", false).writtenBy(user1, feed);
        comment2 = new Comment("두 번째 댓글", false).writtenBy(user2, feed);
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