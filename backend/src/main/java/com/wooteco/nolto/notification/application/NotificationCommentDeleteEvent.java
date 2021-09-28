package com.wooteco.nolto.notification.application;

import com.wooteco.nolto.feed.domain.Comment;
import lombok.Getter;

@Getter
public class NotificationCommentDeleteEvent {

    private final Comment comment;

    public NotificationCommentDeleteEvent(Comment comment) {
        this.comment = comment;
    }
}
