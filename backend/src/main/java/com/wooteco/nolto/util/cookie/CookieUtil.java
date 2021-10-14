package com.wooteco.nolto.util.cookie;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

public class CookieUtil {

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
