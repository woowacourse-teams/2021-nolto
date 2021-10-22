package com.wooteco.nolto.feed.ui.dto;

import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.user.domain.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentResponse {

    private final Long id;
    private final String content;
    private final boolean helper;
    private final int likes;
    private final boolean liked;
    private final boolean feedAuthor;
    private final LocalDateTime createdAt;
    private final boolean modified;
    private final AuthorResponse author;

    public CommentResponse(Long id, String content, boolean helper, int likes, boolean liked, boolean feedAuthor, LocalDateTime createdAt, boolean modified, AuthorResponse author) {
        this.id = id;
        this.content = content;
        this.helper = helper;
        this.likes = likes;
        this.liked = liked;
        this.feedAuthor = feedAuthor;
        this.createdAt = createdAt;
        this.modified = modified;
        this.author = author;
    }

    public static CommentResponse of(Comment comment, boolean isCommentLiked) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.isHelper(),
                comment.likesCount(),
                isCommentLiked,
                comment.isFeedAuthor(),
                comment.getCreatedDate(),
                comment.isModified(),
                AuthorResponse.of(comment.getAuthor())
        );
    }

    public static CommentResponse of(Comment comment) {
        return of(comment, false);
    }

    public static List<CommentResponse> toList(List<Comment> comments, User user) {
        return comments.stream()
                .map(comment -> CommentResponse.of(comment, user.isCommentLiked(comment)))
                .collect(Collectors.toList());
    }

    public static List<CommentResponse> toList(List<Comment> comments) {
        return comments.stream()
                .map(comment -> CommentResponse.of(comment, false))
                .collect(Collectors.toList());
    }
}
