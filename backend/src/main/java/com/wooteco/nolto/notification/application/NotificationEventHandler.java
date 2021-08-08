package com.wooteco.nolto.notification.application;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor
public class NotificationEventHandler {

    private final NotificationService notificationService;

    @TransactionalEventListener
    public void saveNotification(NotificationEvent notificationEvent) {
        notificationService.save(notificationEvent);
    }

    @EventListener
    public void deleteNotification(NotificationFeedDeleteEvent notificationFeedDeleteEvent) {
        notificationService.deleteNotificationRelatedToFeed(notificationFeedDeleteEvent);
    }

    @EventListener
    public void deleteNotification(NotificationCommentDeleteEvent notificationCommentDeleteEvent) {
        notificationService.deleteNotificationRelatedToComment(notificationCommentDeleteEvent);
    }
}
