package com.wooteco.nolto.util.cookie;

import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import com.wooteco.nolto.util.cookie.CookieUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

@Slf4j
public class RefreshTokenCookieManager {

    public static final String REFRESH_TOKEN_KEY = "refresh_token";

    private RefreshTokenCookieManager() {
    }

    public static void setRefreshToken(HttpServletResponse response, TokenResponse tokenResponse) {
        CookieUtil.setCookie(
                response,
                REFRESH_TOKEN_KEY,
                tokenResponse.getRefreshToken(),
                Duration.ofSeconds(tokenResponse.getExpiredIn()));
    }
}
