package com.wooteco.nolto.feed.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecentParamsTest {

    private RecentParams recentParams;

    @BeforeEach
    void setUp() {
        recentParams = new RecentParams();
    }

    @Test
    void getStep() {
        recentParams.setStep("progress");
        assertThat(recentParams.getStep()).isEqualToIgnoringCase(Step.PROGRESS.name());

        recentParams.setStep("complete");
        assertThat(recentParams.getStep()).isEqualToIgnoringCase(Step.COMPLETE.name());
    }

    @Test
    void getHelp() {
        // default
        assertThat(recentParams.getHelp()).isFalse();

        recentParams.setHelp("true");
        assertThat(recentParams.getHelp()).isTrue();

        recentParams.setHelp("false");
        assertThat(recentParams.getHelp()).isFalse();
    }

    @Test
    void getNextFeedId() {
        // default
        assertThat(recentParams.getNextFeedId()).isEqualTo(10000000);

        recentParams.setNextFeedId("2");
        assertThat(recentParams.getNextFeedId()).isEqualTo(2);
    }

    @Test
    void getCountPerPage() {
        // default
        assertThat(recentParams.getCountPerPage()).isEqualTo(15);

        recentParams.setCountPerPage("13");
        assertThat(recentParams.getCountPerPage()).isEqualTo(13);
    }
}