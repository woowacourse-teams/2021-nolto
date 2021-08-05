package com.wooteco.nolto.image.application;

import java.util.Arrays;

public enum ImageKind {
    FEED("nolto-default-thumbnail.png"),
    USER("nolto-default-thumbnail.png");

    private final String defaultName;

    ImageKind(String defaultName) {
        this.defaultName = defaultName;
    }

    public static boolean isDefault(String name) {
        return Arrays.stream(values()).anyMatch(v -> v.defaultName.equals(name));
    }

    public String defaultName() {
        return defaultName;
    }
}
