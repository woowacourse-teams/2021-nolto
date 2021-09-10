package com.wooteco.nolto.feed.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecentRequestParamsTest {

    private RecentRequestParams recentRequestParams;

    @BeforeEach
    void setUp() {
        recentRequestParams = new RecentRequestParams();
    }

    @Test
    void getStep() {
        recentRequestParams.setStep("progress");
        assertThat(recentRequestParams.getStep()).isEqualToIgnoringCase(Step.PROGRESS.name());

        recentRequestParams.setStep("complete");
        assertThat(recentRequestParams.getStep()).isEqualToIgnoringCase(Step.COMPLETE.name());
    }

    @Test
    void getHelp() {
        // default
        assertThat(recentRequestParams.getHelp()).isFalse();

        recentRequestParams.setHelp("true");
        assertThat(recentRequestParams.getHelp()).isTrue();

        recentRequestParams.setHelp("false");
        assertThat(recentRequestParams.getHelp()).isFalse();
    }

    @Test
    void getNextFeedId() {
        // default
        assertThat(recentRequestParams.getNextFeedId()).isEqualTo(10000000);

        recentRequestParams.setNextFeedId("2");
        assertThat(recentRequestParams.getNextFeedId()).isEqualTo(2);
    }

    @Test
    void getCountPerPage() {
        // default
        assertThat(recentRequestParams.getCountPerPage()).isEqualTo(15);

        recentRequestParams.setCountPerPage("13");
        assertThat(recentRequestParams.getCountPerPage()).isEqualTo(13);
    }
}