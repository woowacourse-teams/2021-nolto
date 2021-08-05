package com.wooteco.nolto.feed.ui.dto;

import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponse {

    private Long id;
    private String content;
    private boolean helper;
    private int likes;
    private boolean liked;
    private boolean feedAuthor;
    private LocalDateTime createAt;
    private boolean modified;
    private AuthorResponse author;

    public static CommentResponse create(Long id) {
        return new CommentResponse(
                id,
                "댓글이다" + id,
                false,
                10,
                false,
                false,
                LocalDateTime.now(),
                true,
                new AuthorResponse(1L, "charlie", "https://dksykemwl00pf.cloudfront.net/")
        );
    }

    public static CommentResponse of(Comment comment, User user) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.isHelper(),
                comment.likesCount(),
                user.isCommentLiked(comment),
                comment.isFeedAuthor(),
                comment.getCreatedDate(),
                comment.isModified(),
                AuthorResponse.of(comment.getAuthor())
        );
    }
}
