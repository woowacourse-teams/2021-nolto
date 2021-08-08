package com.wooteco.nolto.notification.application;

import com.wooteco.nolto.feed.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationCommentDeleteEvent {
    private final Comment comment;
}
