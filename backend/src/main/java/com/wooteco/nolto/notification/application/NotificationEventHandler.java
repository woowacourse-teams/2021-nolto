package com.wooteco.nolto.notification.application;

import lombok.AllArgsConstructor;
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
}
