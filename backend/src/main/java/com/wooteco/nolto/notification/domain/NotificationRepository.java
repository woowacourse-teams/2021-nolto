package com.wooteco.nolto.notification.domain;

import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByListener(User listener);

    void deleteAllByListener(User listener);

    long countByListener(User user);

    void deleteAllByFeed(Feed feed);

    void deleteAllByComment(Comment comment);
}
