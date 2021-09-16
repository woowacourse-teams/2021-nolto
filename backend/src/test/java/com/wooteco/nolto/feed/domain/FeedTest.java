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

    private Feed feed1;

    @BeforeEach
    void setUp() {
        feed1 = Feed.builder()
                .title("아마찌의 개쩌는 지하철 미션")
                .content("난 너무 잘해")
                .step(Step.PROGRESS)
                .isSos(true)
                .storageUrl("https://github.com/woowacourse-teams/2021-nolto")
                .deployedUrl("https://github.com/woowacourse-teams/2021-nolto")
                .thumbnailUrl("https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png")
                .build();
    }

    @DisplayName("피드에 작성자 추가할 수 있다.")
    @Test
    void writtenBy() {
        // when
        Feed feed = feed1.writtenBy(UserTest.USER);

        // then
        assertThat(feed.getAuthor()).isEqualTo(UserTest.USER);
    }

    @DisplayName("조회하지 않은 경우 조회수를 증가시킨다.")
    @Test
    void increaseView() {
        // when
        int beforeView = feed1.getViews();
        feed1.increaseView(false);

        // then
        assertThat(feed1.getViews()).isEqualTo(beforeView + 1);
    }

    @DisplayName("이미 조회했을 시 조회수를 증가시키지 않는다.")
    @Test
    void notIncreaseView() {
        // when
        int beforeView = feed1.getViews();
        feed1.increaseView(true);

        // then
        assertThat(feed1.getViews()).isEqualTo(beforeView);
    }

    @DisplayName("전시중(완료된) 프로젝트가의 배포 URL가 null이거나 공백인 경우 예외가 발생한다.")
    @Test
    void mustHaveDeployUrlWhenCompleteStep() {
        assertThatThrownBy(() ->
                Feed.builder()
                        .title("아마찌의 개쩌는 지하철 미션")
                        .content("난 너무 잘해")
                        .step(Step.COMPLETE)
                        .isSos(true)
                        .storageUrl("https://github.com/woowacourse-teams/2021-nolto")
                        .thumbnailUrl("https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png")
                        .build())
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorType.MISSING_DEPLOY_URL.getMessage());
    }
}
