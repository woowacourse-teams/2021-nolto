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
public class CommentResponse {

    private Long id;
    private String content;
    private boolean helper;
    private int likes;
    private boolean liked;
    private boolean feedAuthor;
    private LocalDateTime createdAt;
    private boolean modified;
    private AuthorResponse author;

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

    public static List<CommentResponse> toList(List<Comment> comments, User user) {
        return comments.stream()
                .map(comment -> CommentResponse.of(comment, user.isCommentLiked(comment)))
                .collect(Collectors.toList());
    }
}
