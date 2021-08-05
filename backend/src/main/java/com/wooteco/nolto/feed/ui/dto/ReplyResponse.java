package com.wooteco.nolto.feed.ui.dto;

import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ReplyResponse {

    private final Long id;
    private final String content;
    private final int likes;
    private final boolean liked;
    private final boolean feedAuthor;
    private final LocalDateTime createdAt;
    private final boolean modified;
    private final Long commentId;
    private final AuthorResponse author;

    public static ReplyResponse of(Comment reply, boolean liked) {
        return new ReplyResponse(
                reply.getId(),
                reply.getContent(),
                reply.likesCount(),
                liked,
                reply.isFeedAuthor(),
                reply.getCreatedDate(),
                reply.isModified(),
                reply.getParentComment().getId(),
                AuthorResponse.of(reply.getAuthor())
        );
    }

    public static List<ReplyResponse> toList(List<Comment> replies, User user) {
        return replies.stream()
                .map(reply -> ReplyResponse.of(reply, user.isCommentLiked(reply)))
                .collect(Collectors.toList());
    }
}

