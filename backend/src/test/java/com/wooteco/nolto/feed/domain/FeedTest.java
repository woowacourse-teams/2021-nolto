package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.exception.BadRequestException;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.user.domain.UserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FeedTest {
    public static final Feed FEED1 = new Feed(
            "title",
            "content",
            Step.PROGRESS,
            true,
            "https://github.com/woowacourse-teams/2021-nolto",
            "https://github.com/woowacourse-teams/2021-nolto",
            "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png").writtenBy(UserTest.USER);

    private Feed feed1;

    @BeforeEach
    void setUp() {
        feed1 = new Feed(
                "title",
                "content",
                Step.PROGRESS,
                true,
                "https://github.com/woowacourse-teams/2021-nolto",
                "https://github.com/woowacourse-teams/2021-nolto",
                "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png");
    }

    @DisplayName("피드에 작성자 추가할 수 있다.")
    @Test
    void writtenBy() {
        // when
        Feed feed = feed1.writtenBy(UserTest.USER);

        // then
        assertThat(feed.getAuthor()).isEqualTo(UserTest.USER);
    }

    @DisplayName("조회수를 증가시킨다.")
    @Test
    void increaseView() {
        // when
        int beforeView = feed1.getViews();
        feed1.increaseView();

        // then
        assertThat(feed1.getViews()).isEqualTo(beforeView + 1);
    }

    @DisplayName("전시중(완료된) 프로젝트가의 배포 URL가 null이거나 공백인 경우 예외가 발생한다.")
    @Test
    void mustHaveDeployUrlWhenCompleteStep() {
        assertThatThrownBy(() ->
                new Feed(
                        "프로젝트 제목",
                        "프로젝트 소개 내용",
                        Step.COMPLETE,
                        false,
                        "www.github.com/woowacourse",
                        "",
                        "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png"))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorType.MISSING_DEPLOY_URL.getMessage());
    }
}
