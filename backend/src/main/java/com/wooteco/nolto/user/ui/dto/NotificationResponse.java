package com.wooteco.nolto.user.ui.dto;

import com.wooteco.nolto.feed.ui.dto.AuthorResponse;
import com.wooteco.nolto.notification.domain.Notification;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class NotificationResponse {

    private final Long id;
    private final AuthorResponse user;
    private final FeedNotificationResponse feed;
    private final CommentNotificationResponse comment;
    private final String type;

    public NotificationResponse(Long id, AuthorResponse user, FeedNotificationResponse feed, CommentNotificationResponse comment, String type) {
        this.id = id;
        this.user = user;
        this.feed = feed;
        this.comment = comment;
        this.type = type;
    }

    public static List<NotificationResponse> toList(List<Notification> notifications) {
        return notifications.stream()
                .map(NotificationResponse::of)
                .collect(Collectors.toList());
    }

    private static NotificationResponse of(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                AuthorResponse.of(notification.getPublisher()),
                FeedNotificationResponse.of(notification.getFeed()),
                CommentNotificationResponse.of(notification.getComment()),
                notification.getNotificationType().name()
        );
    }
}
