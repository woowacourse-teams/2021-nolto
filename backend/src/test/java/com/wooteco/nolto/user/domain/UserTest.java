package com.wooteco.nolto.user.domain;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Like;
import com.wooteco.nolto.feed.domain.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.wooteco.nolto.UserFixture.아마찌_생성;
import static com.wooteco.nolto.UserFixture.조엘_생성;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    private User 아마찌;
    private Feed feed;

    @BeforeEach
    void setUp() {
        아마찌 = 아마찌_생성();
        feed = Feed.builder()
                .title("title1")
                .content("content1")
                .step(Step.PROGRESS)
                .isSos(true)
                .storageUrl("https://github.com/woowacourse-teams/2021-nolto")
                .thumbnailUrl("https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png")
                .build();
    }

    @DisplayName("멤버가 해당 피드에 좋아요를 눌렀는지 검증한다.")
    @Test
    void isLiked() {
        feed.writtenBy(아마찌);

        아마찌.getLikes().add(new Like(아마찌, feed));

        assertThat(아마찌.isLiked(feed)).isTrue();
    }

    @DisplayName("멤버가 해당 피드에 좋아요를 안 눌렀는지 검증한다.")
    @Test
    void isNotLiked() {
        User 조엘 = 조엘_생성();
        feed.writtenBy(아마찌);

        아마찌.getLikes().add(new Like(아마찌, feed));

        assertThat(조엘.isLiked(feed)).isFalse();
    }

    @DisplayName("게스트가 해당 피드에 좋아요를 눌렀는지 검증하면 무조건 false이다.")
    @Test
    void isLikedWithGuest() {
        User guestUser = User.GUEST_USER;

        assertThat(guestUser.isLiked(feed)).isFalse();
    }
}