package com.wooteco.nolto.notification.application;

import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.notification.domain.Notification;
import com.wooteco.nolto.notification.domain.NotificationType;
import com.wooteco.nolto.user.domain.User;
import lombok.Getter;

@Getter
public class NotificationEvent {
    private final User listener;
    private final Feed feed;
    private final Comment comment;
    private final User publisher;
    private final NotificationType notificationType;

    public NotificationEvent(User listener, Feed feed, Comment comment, User publisher, NotificationType notificationType) {
        this.listener = listener;
        this.feed = feed;
        this.comment = comment;
        this.publisher = publisher;
        this.notificationType = notificationType;
    }

    public Notification toEntity() {
        return new Notification(listener, feed, comment, publisher, notificationType);
    }

    public static NotificationEvent commentOf(Feed feed, Comment comment, boolean isHelper) {
        return new NotificationEvent(feed.getAuthor(), feed, comment, comment.getAuthor(), NotificationType.findCommentBy(isHelper));
    }

    public static NotificationEvent replyOf(Comment comment, Comment reply) {
        return new NotificationEvent(comment.getAuthor(), comment.getFeed(), comment, reply.getAuthor(), NotificationType.REPLY);
    }

    public static NotificationEvent likeOf(Feed feed, User publisher) {
        return new NotificationEvent(feed.getAuthor(), feed, null, publisher, NotificationType.LIKE);
    }

    public boolean validatePublisher() {
        return !publisher.sameAs(listener);
    }
}
