package com.wooteco.nolto.auth.infrastructure;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

public class CookieUtil {

    public static final String VIEW_KEY = "view";
    public static final String REFRESH_TOKEN_KEY = "refresh_token";

    private CookieUtil() {

    }

    public static void setCookie(HttpServletResponse response,
                                   String key,
                                   String value,
                                   Duration duration) {
        response.setHeader(HttpHeaders.SET_COOKIE, generateCookie(key, value, duration));
    }

    private static String generateCookie(String key, String value, Duration duration) {
        return ResponseCookie.from(key, value)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .maxAge(duration.getSeconds())
                .build()
                .toString();
    }
}
