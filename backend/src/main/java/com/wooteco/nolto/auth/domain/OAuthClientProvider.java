package com.wooteco.nolto.auth.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class OAuthClientProvider {

    private final List<OAuthClient> oAuthClients;

    private Map<SocialType, OAuthClient> oAuthClientMap;

    @PostConstruct
    private void initialize() {
        oAuthClientMap = new EnumMap<>(SocialType.class);
        for (SocialType socialType : SocialType.values()) {
            oAuthClientMap.put(socialType, findOAuthClient(socialType));
        }
    }

    private OAuthClient findOAuthClient(SocialType socialType) {
        return oAuthClients.stream()
                .filter(oAuthClient -> oAuthClient.checkType(socialType))
                .findAny().orElseThrow(() -> new IllegalArgumentException("지원하지 않는 소셜 로그인 입니다."));
    }

    public OAuthClient provideOAuthClientBy(SocialType socialType) {
        return oAuthClientMap.get(socialType);
    }
}
