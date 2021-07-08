package com.wooteco.nolto.feed.domain;

import java.util.Arrays;

public enum Step {
    PROGRESS(false),
    COMPLETE(true);

    boolean hasDeployedUrl;

    Step(boolean hasDeployedUrl) {
        this.hasDeployedUrl = hasDeployedUrl;
    }

    public static Step of(String value) {
        return Arrays.stream(values())
                .filter(step -> step.name().equalsIgnoreCase(value))
                .findAny().orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Step 입니다."));
    }
}
