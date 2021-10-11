package com.wooteco.nolto.util.cookie;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalTime;

public class ViewHistoryManager {

    public static final String VIEW_KEY = "view";

    private ViewHistoryManager() {
    }

    public static boolean isAlreadyView(String cookieValue, String feedId) {
        return cookieValue.contains("/" + feedId + "/");
    }

    public static void setCookieByReadHistory(boolean alreadyView, String cookieValue, String feedId, HttpServletResponse response) {
        if (alreadyView) {
            return;
        }
        cookieValue = cookieValue.concat(feedId + "/");
        Duration duration = Duration.between(LocalTime.now(), LocalTime.MAX);
        CookieUtil.setCookie(response, VIEW_KEY, cookieValue, duration);
    }
}
