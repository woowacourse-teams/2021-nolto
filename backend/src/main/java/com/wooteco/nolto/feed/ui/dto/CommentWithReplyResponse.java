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
public class CommentWithReplyResponse {

    private Long id;
    private String content;
    private boolean helper;
    private int likes;
    private boolean liked;
    private boolean feedAuthor;
    private LocalDateTime createAt;
    private boolean modified;
    private AuthorResponse author;
    private List<ReplyResponse> replies;

    public static CommentWithReplyResponse of(Comment comment, User user) {
        return new CommentWithReplyResponse(
                comment.getId(),
                comment.getContent(),
                comment.isHelper(),
                comment.likesCount(),
                user.isCommentLiked(comment),
                comment.isFeedAuthor(),
                comment.getCreatedDate(),
                comment.isModified(),
                AuthorResponse.of(comment.getAuthor()),
                ReplyResponse.toList(comment.getReplies(), user)
        );
    }

    public static List<CommentWithReplyResponse> toList(List<Comment> comments, User user) {
        return comments.stream()
                .map(comment -> CommentWithReplyResponse.of(comment, user))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "CommentWithReplyResponse{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", helper=" + helper +
                ", likes=" + likes +
                ", liked=" + liked +
                ", feedAuthor=" + feedAuthor +
                ", createAt=" + createAt +
                ", modified=" + modified +
                ", author=" + author +
                ", replies=" + replies +
                '}';
    }
}
