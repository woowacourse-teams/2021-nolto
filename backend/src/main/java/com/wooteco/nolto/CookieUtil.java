package com.wooteco.nolto;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class CookieUtil {

    private CookieUtil() {
    }

    public static Cookie generateCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(86400);
        cookie.setHttpOnly(true);
        return cookie;
    }

    public static Optional<Cookie> findCookieByName(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (Objects.nonNull(cookies)) {
            return Arrays.stream(request.getCookies())
                    .filter(x -> cookieName.equals(x.getName()))
                    .findAny();
        }
        return Optional.empty();
    }
}
