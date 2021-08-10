package com.wooteco.nolto.notification.application;

import com.wooteco.nolto.exception.BadRequestException;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.NotFoundException;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.notification.domain.Notification;
import com.wooteco.nolto.notification.domain.NotificationRepository;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(NotificationEvent notificationEvent) {
        Notification notification = notificationEvent.toEntity();
        if (notificationEvent.validatePublisher()) {
            notificationRepository.save(notification);
        }
    }

    public List<Notification> findAllByUser(User user) {
        return notificationRepository.findAllByListener(user);
    }

    public void delete(User user, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException(ErrorType.NOTIFICATION_NOT_FOUND));

        if (!notification.isListener(user)) {
            throw new BadRequestException(ErrorType.UNAUTHORIZED_DELETE_NOTIFICATION);
        }

        notificationRepository.delete(notification);
    }

    public void deleteAll(User user) {
        notificationRepository.deleteAllByListener(user);
    }

    public long findNotificationCount(User user) {
        return notificationRepository.countByListener(user);
    }

    public void deleteNotificationRelatedToFeed(NotificationFeedDeleteEvent notificationFeedDeleteEvent) {
        Feed feed = notificationFeedDeleteEvent.getFeed();
        notificationRepository.deleteAllByFeed(feed);
    }

    public void deleteNotificationRelatedToComment(NotificationCommentDeleteEvent notificationCommentDeleteEvent) {
        Comment comment = notificationCommentDeleteEvent.getComment();
        notificationRepository.deleteAllByComment(comment);
    }
}
