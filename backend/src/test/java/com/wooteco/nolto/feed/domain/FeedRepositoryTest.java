package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FeedRepositoryTest {
    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    private Feed feed1;
    private Feed feed2;
    private Feed feed3;

    @BeforeEach
    void setUp() {
        user1 = new User("123456L", "github", "아마찌", "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png");
        user2 = new User("654321L", "google", "지그", "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png");

        userRepository.save(user1);
        userRepository.save(user2);

        feed1 = new Feed("프로젝트 제목1", "프로젝트 내용1", Step.PROGRESS, true,
                "www.github.com/woowacourse", "", "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png");
        feed2 = new Feed("title2", "content2", Step.PROGRESS, false,
                "www.github.com/woowacourse", "", "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png");
        feed3 = new Feed("title3", "content3", Step.COMPLETE, false,
                "www.github.com/woowacourse", "www.github.com/woowacourse", "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png");
    }

    @DisplayName("토이 프로젝트 글을 의미하는 Feed를 저장한다.")
    @Test
    void save() {
        // given
        feed1.writtenBy(user1);
        feed2.writtenBy(user2);
        feed3.writtenBy(user2);

        // when
        Feed savedFeed1 = feedRepository.save(feed1);
        Feed savedFeed2 = feedRepository.save(feed2);
        Feed savedFeed3 = feedRepository.save(feed3);

        // then
        assertThat(savedFeed1.getId()).isNotNull();
        checkSame(feed1, savedFeed1);

        assertThat(savedFeed2.getId()).isNotNull();
        checkSame(feed2, savedFeed2);

        assertThat(savedFeed3.getId()).isNotNull();
        checkSame(feed3, savedFeed3);
    }

    private void checkSame(Feed feed1, Feed feed2) {
        assertThat(feed1.getTitle()).isEqualTo(feed2.getTitle());
        assertThat(feed1.getContent()).isEqualTo(feed2.getContent());
        assertThat(feed1.getStep()).isEqualTo(feed2.getStep());
        assertThat(feed1.isSos()).isEqualTo(feed2.isSos());
        assertThat(feed1.getStorageUrl()).isEqualTo(feed2.getStorageUrl());
        assertThat(feed1.getDeployedUrl()).isEqualTo(feed2.getDeployedUrl());
        assertThat(feed1.getThumbnailUrl()).isEqualTo(feed2.getThumbnailUrl());
        assertThat(feed1.getViews()).isEqualTo(feed2.getViews());
        assertThat(feed1.getLikes()).isEqualTo(feed2.getLikes());
    }
}