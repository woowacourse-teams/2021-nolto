package com.wooteco.nolto.auth.domain;

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
                .findAny().orElseThrow(() -> new IllegalArgumentException("지원하지 않는 소셜 로그인 입니다."));
    }

    public SocialOAuthInfo provideSocialOAuthInfoBy(SocialType socialType) {
        return socialOAuthInfoMap.get(socialType);
    }
}
