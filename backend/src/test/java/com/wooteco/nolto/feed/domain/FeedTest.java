package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.user.domain.UserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class FeedTest {
    private Feed feed1;

    @BeforeEach
    void setUp() {
        feed1 = new Feed("title", "content", Step.PROGRESS, true,
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

    @Test
    void increaseView() {
        // when
        int beforeView = feed1.getViews();
        feed1.increaseView();

        // then
        assertThat(feed1.getViews()).isEqualTo(beforeView + 1);
    }
}
