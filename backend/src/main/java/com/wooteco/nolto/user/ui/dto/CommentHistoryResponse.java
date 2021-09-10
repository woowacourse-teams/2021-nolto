package com.wooteco.nolto.user.ui.dto;

import com.wooteco.nolto.feed.domain.Comment;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentHistoryResponse {
    private final FeedHistoryResponse feed;
    private final String text;

    public CommentHistoryResponse(FeedHistoryResponse feed, String text) {
        this.feed = feed;
        this.text = text;
    }

    public static CommentHistoryResponse of(Comment comment) {
        return new CommentHistoryResponse(FeedHistoryResponse.of(comment.getFeed()), comment.getContent());
    }

    public static List<CommentHistoryResponse> toList(List<Comment> comments) {
        return comments.stream()
                .map(CommentHistoryResponse::of)
                .collect(Collectors.toList());
    }
}
