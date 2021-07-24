package com.wooteco.nolto.auth.domain;

import com.wooteco.nolto.exception.BadRequestException;
import com.wooteco.nolto.exception.ErrorType;

import java.util.Arrays;

public enum SocialType {
    GITHUB,
    GOOGLE;

    public static SocialType findBy(String socialTypeName) {
        return Arrays.stream(SocialType.values())
                .filter(socialType -> socialType.name().equalsIgnoreCase(socialTypeName))
                .findAny().orElseThrow(() -> new BadRequestException(ErrorType.NOT_SUPPORTED_SOCIAL_LOGIN));
    }
}
