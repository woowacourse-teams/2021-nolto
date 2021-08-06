package com.wooteco.nolto.notification.application;

import com.wooteco.nolto.notification.domain.Notification;
import com.wooteco.nolto.notification.domain.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void save(NotificationEvent notificationEvent) {
        Notification notification = notificationEvent.toEntity();
        notificationRepository.save(notification);
    }
}
