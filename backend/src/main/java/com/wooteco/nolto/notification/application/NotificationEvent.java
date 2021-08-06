package com.wooteco.nolto.notification.application;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.notification.domain.Notification;
import com.wooteco.nolto.notification.domain.NotificationType;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotificationEvent {
    private final Feed feed;
    private final User publisher;
    private final NotificationType notificationType;

    public Notification toEntity() {
        return new Notification(feed.getAuthor(), feed, publisher, notificationType);
    }

    public static NotificationEvent commentOf(Feed feed, User publisher, boolean isHelper) {
        return new NotificationEvent(feed, publisher, NotificationType.findCommentBy(isHelper));
    }
}
