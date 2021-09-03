package com.wooteco.nolto;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalTime;

public class ViewHistoryManager {

    private ViewHistoryManager() {
    }

    public static Cookie generateCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        Duration duration = Duration.between(LocalTime.now(), LocalTime.MAX);
        cookie.setMaxAge((int) duration.getSeconds());
        cookie.setHttpOnly(true);
        return cookie;
    }

    public static boolean isAlreadyView(String cookieValue, String feedId) {
        return cookieValue.contains("/" + feedId + "/");
    }

    public static void setCookieByReadHistory(boolean alreadyView, String cookieValue, String feedId, HttpServletResponse response) {
        if (alreadyView) {
            return;
        }
        cookieValue = cookieValue.concat(feedId + "/");
        response.addCookie(ViewHistoryManager.generateCookie("view", cookieValue));
    }
}
