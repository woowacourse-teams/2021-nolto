package com.wooteco.nolto.feed.ui.dto;

import com.wooteco.nolto.feed.domain.Comment;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class CommentAndReplyResponse {

    private final CommentResponse comment;
    private final List<CommentResponse> replies;

    public CommentAndReplyResponse(CommentResponse comment, List<CommentResponse> replies) {
        this.comment = comment;
        this.replies = replies;
    }

    public static CommentAndReplyResponse of(Comment comment, List<Comment> replies) {
        return new CommentAndReplyResponse(CommentResponse.of(comment), CommentResponse.toList(replies));
    }

    public static List<CommentAndReplyResponse> toList(Map<Comment, List<Comment>> commentAndReplies) {
        return commentAndReplies.entrySet()
                .stream()
                .map(comment -> CommentAndReplyResponse.of(comment.getKey(), comment.getValue()))
                .collect(Collectors.toList());
    }
}
