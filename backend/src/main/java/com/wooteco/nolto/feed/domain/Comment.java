package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.BaseEntity;
import com.wooteco.nolto.exception.BadRequestException;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.UnauthorizedException;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Entity
@AllArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean helper;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommentLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

    public Comment() {
    }

    public Comment(Long id, String content, boolean helper) {
        this.id = id;
        this.content = content;
        this.helper = helper;
    }

    public Comment(String content, boolean helper) {
        this.content = content;
        this.helper = helper;
    }

    public static Comment createReply(String content, boolean helper) {
        if (helper) {
            throw new BadRequestException(ErrorType.REPLY_NOT_SUPPORTED_HELPER);
        }
        return new Comment(content, false);
    }

    public int likesCount() {
        return likes.size();
    }

    public void update(String content, boolean helper) {
        this.content = content;
        if (this.isReply() && helper) {
            throw new BadRequestException(ErrorType.REPLY_NOT_SUPPORTED_HELPER);
        }
        this.helper = helper;
    }

    public void addReply(Comment reply) {
        this.replies.add(reply);
        reply.parentComment = this;
    }

    public Comment writtenBy(User user, Feed feed) {
        this.author = user;
        this.feed = feed;
        feed.addComment(this);
        user.addComment(this);
        return this;
    }

    public void setFeed(Feed feed) {
        if (Objects.isNull(this.feed)) {
            this.feed = feed;
        }
    }

    public void addCommentLike(CommentLike commentLike) {
        this.likes.add(commentLike);
    }

    public Optional<CommentLike> findLikeBy(User user) {
        return this.likes.stream()
                .filter(commentLike -> commentLike.sameAs(user))
                .findAny();
    }

    public boolean isModified() {
        return getModifiedDate().isAfter(getCreatedDate());
    }

    public boolean isFeedAuthor() {
        return this.author.sameAs(feed.getAuthor());
    }

    public void addParentComment(Comment comment) {
        this.parentComment = comment;
        comment.addReply(this);
    }

    public void checkAuthority(User user, ErrorType errorType) {
        if (!this.author.sameAs(user)) {
            throw new UnauthorizedException(errorType);
        }
    }

    public boolean changedToHelper(boolean helper) {
        return !this.helper && this.helper != helper;
    }

    public void delete() {
        if (isComment()) {
            deleteReplies();
        }
        this.parentComment = null;
        this.author = null;
        this.feed.deleteComment(this);
        this.feed = null;
        deleteLikes();
    }

    private void deleteReplies() {
        for (Comment reply : replies) {
            reply.getAuthor().deleteComment(reply);
        }
        this.replies.clear();
    }

    private void deleteLikes() {
        for (CommentLike like : this.likes) {
            like.deleteByComment();
        }
        this.likes.clear();
    }

    public boolean isComment() {
        return Objects.isNull(parentComment);
    }

    public boolean isReply() {
        return Objects.nonNull(this.parentComment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
