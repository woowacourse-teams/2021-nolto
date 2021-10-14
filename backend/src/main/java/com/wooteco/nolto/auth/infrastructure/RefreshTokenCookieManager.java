package com.wooteco.nolto.auth.infrastructure;

import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

@Slf4j
public class RefreshTokenCookieManager {

    private RefreshTokenCookieManager() {

    }

    public static void setRefreshToken(HttpServletResponse response, TokenResponse tokenResponse) {
        CookieUtil.setCookie(
                response,
                CookieUtil.REFRESH_TOKEN_KEY,
                tokenResponse.getRefreshToken(),
                Duration.ofSeconds(tokenResponse.getExpiredIn()));
    }
}
