package com.wooteco.nolto.auth.domain;

import java.util.Arrays;

public enum SocialType {
    GITHUB,
    GOOGLE;

    public static SocialType findBy(String socialTypeName) {
        return Arrays.stream(SocialType.values())
                .filter(socialType -> socialType.name().equalsIgnoreCase(socialTypeName))
                .findAny().orElseThrow(() -> new IllegalArgumentException("지원하지 않는 소셜 로그인입니다."));
    }
}
