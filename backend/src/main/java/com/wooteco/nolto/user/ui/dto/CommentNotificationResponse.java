package com.wooteco.nolto.user.ui.dto;

import com.wooteco.nolto.feed.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class CommentNotificationResponse {

    private final Long id;
    private final String text;

    public static CommentNotificationResponse of(Comment comment) {
        if (Objects.isNull(comment)) {
            return null;
        }
        return new CommentNotificationResponse(comment.getId(), comment.getContent());
    }
}
