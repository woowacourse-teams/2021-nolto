package com.wooteco.nolto.auth.domain;

import com.wooteco.nolto.exception.BadRequestException;
import com.wooteco.nolto.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class SocialOAuthInfoProvider {

    private final List<SocialOAuthInfo> socialOAuthInfos;

    private Map<SocialType, SocialOAuthInfo> socialOAuthInfoMap;

    @PostConstruct
    private void initialize() {
        socialOAuthInfoMap = new EnumMap<>(SocialType.class);
        for (SocialType socialType : SocialType.values()) {
            socialOAuthInfoMap.put(socialType, findSocialOAuthInfo(socialType));
        }
    }

    private SocialOAuthInfo findSocialOAuthInfo(SocialType socialType) {
        return socialOAuthInfos.stream()
                .filter(socialOAuth -> socialOAuth.checkType(socialType))
                .findAny().orElseThrow(() -> new BadRequestException(ErrorType.NOT_SUPPORTED_SOCIAL_LOGIN));
    }

    public SocialOAuthInfo provideSocialOAuthInfoBy(SocialType socialType) {
        return socialOAuthInfoMap.get(socialType);
    }
}
