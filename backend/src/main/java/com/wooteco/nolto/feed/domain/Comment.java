package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.BaseEntity;
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

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<CommentLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.REMOVE)
    private List<Comment> replies = new ArrayList<>();

    public Comment() {
    }

    public Comment(String content, boolean helper) {
        this.content = content;
        this.helper = helper;
    }

    public int likesCount() {
        return likes.size();
    }

    public void update(String content, boolean helper) {
        this.content = content;
        this.helper = helper;
    }

    public void addReply(Comment reply) {
        this.replies.add(reply);
        reply.parentComment = this;
        feed.addComment(reply);
    }

    public Comment writtenBy(User user) {
        this.author = user;
        user.getComments().add(this);
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

    public void sortReplies() {
        this.replies.sort((o1, o2) -> o2.getCreatedDate().compareTo(o1.getCreatedDate()));
    }

    public void changeContent(String content) {
        this.content = content;
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
