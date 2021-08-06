package com.wooteco.nolto.notification.application;

import com.wooteco.nolto.notification.domain.Notification;
import com.wooteco.nolto.notification.domain.NotificationRepository;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void save(NotificationEvent notificationEvent) {
        Notification notification = notificationEvent.toEntity();
        notificationRepository.save(notification);
    }

    public List<Notification> findAllByUser(User user) {
        return notificationRepository.findAllByListener(user);
    }
}
