package com.wooteco.nolto;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Step;

public class FeedFixture {

    public static final String DEFAULT_IMAGE = "nolto-default-thumbnail.png";
    public static final String DEFAULT_IMAGE_URL = "https://dksykemwl00pf.cloudfront.net/" + DEFAULT_IMAGE;
    private static final String DEFAULT_URL = "https://github.com/woowacourse-teams/2021-nolto";
    private static final String TITLE = "놀러오세요 토이 프로젝트";
    private static final String CONTENT = "부담없이 자랑하는 작고 소중한 내 프로젝트";

    private FeedFixture() {
    }

    public static Feed 진행중_단계의_피드_생성() {
        return 진행중_단계의_피드_생성("진행중인 " + TITLE, CONTENT);
    }

    public static Feed 진행중_단계의_피드_생성(String title, String content) {
        return Feed.builder()
                .title(title)
                .content(content)
                .step(Step.PROGRESS)
                .isSos(false)
                .storageUrl(DEFAULT_URL)
                .thumbnailUrl(DEFAULT_IMAGE_URL)
                .build();
    }

    public static Feed 진행중_단계의_SOS_피드_생성() {
        return 진행중_단계의_SOS_피드_생성("진행중인 " + TITLE, CONTENT);
    }

    public static Feed 진행중_단계의_SOS_피드_생성(String title, String content) {
        return Feed.builder()
                .title(title)
                .content(content)
                .step(Step.PROGRESS)
                .isSos(true)
                .storageUrl(DEFAULT_URL)
                .thumbnailUrl(DEFAULT_IMAGE_URL)
                .views(1)
                .build();
    }

    public static Feed 전시중_단계의_피드_생성() {
        return 전시중_단계의_피드_생성("전시중인 " + TITLE, CONTENT);
    }

    public static Feed 전시중_단계의_피드_생성(String title, String content) {
        return Feed.builder()
                .title(title)
                .content(content)
                .step(Step.COMPLETE)
                .isSos(false)
                .storageUrl(DEFAULT_URL)
                .deployedUrl(DEFAULT_URL)
                .thumbnailUrl(DEFAULT_IMAGE_URL)
                .build();
    }
}
