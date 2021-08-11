package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.exception.BadRequestException;
import com.wooteco.nolto.exception.ErrorType;

import java.util.Arrays;

public enum Step {
    PROGRESS,
    COMPLETE;

    public static Step of(String value) {
        return Arrays.stream(values())
                .filter(step -> step.name().equalsIgnoreCase(value))
                .findAny().orElseThrow(() -> new BadRequestException(ErrorType.NOT_SUPPORTED_STEP));
    }
}
