package com.wooteco.nolto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

@Component
public class ViewHistoryManager {

    @Value("application.rendering.server.ip")
    private String renderingServerIp;

    public ResponseCookie generateCookie(String name, String value) {
        Duration duration = Duration.between(LocalTime.now(), LocalTime.MAX);
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .maxAge(duration.getSeconds())
                .build();
    }

    public boolean isAlreadyView(String ipAddress, String cookieValue, String feedId) {
        if (Objects.nonNull(ipAddress) && ipAddress.equals(renderingServerIp)) {
            return true;
        }
        return cookieValue.contains("/" + feedId + "/");
    }

    public void setCookieByReadHistory(boolean alreadyView, String cookieValue, String feedId, HttpServletResponse response) {
        if (alreadyView) {
            return;
        }
        cookieValue = cookieValue.concat(feedId + "/");
        response.setHeader(HttpHeaders.SET_COOKIE, generateCookie("view", cookieValue).toString());
    }
}
