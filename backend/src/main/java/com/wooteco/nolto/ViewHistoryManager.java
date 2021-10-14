package com.wooteco.nolto;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalTime;

public class ViewHistoryManager {

    private ViewHistoryManager() {
    }

    public static ResponseCookie generateCookie(String name, String value) {
        Duration duration = Duration.between(LocalTime.now(), LocalTime.MAX);
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .maxAge(duration.getSeconds())
                .build();
    }

    public static boolean isAlreadyView(String cookieValue, String feedId) {
        return cookieValue.contains("/" + feedId + "/");
    }

    public static void setCookieByReadHistory(boolean alreadyView, String cookieValue, String feedId, HttpServletResponse response) {
        if (alreadyView) {
            return;
        }
        cookieValue = cookieValue.concat(feedId + "/");
        response.setHeader(HttpHeaders.SET_COOKIE, ViewHistoryManager.generateCookie("view", cookieValue).toString());
    }
}
