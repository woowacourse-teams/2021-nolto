package com.wooteco.nolto.user.ui.dto;

import com.wooteco.nolto.feed.domain.Comment;
import lombok.Getter;

import java.util.Objects;

@Getter
public class CommentNotificationResponse {

    private final Long id;
    private final String text;

    public CommentNotificationResponse(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    public static CommentNotificationResponse of(Comment comment) {
        if (Objects.isNull(comment)) {
            return null;
        }
        return new CommentNotificationResponse(comment.getId(), comment.getContent());
    }
}
