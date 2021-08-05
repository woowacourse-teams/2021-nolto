package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.BaseEntity;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

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
            feed.addComment(this);
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

    public boolean isFeedAuthor() {
        return feed.getAuthor().sameAs(this.author);
    }

    public void sortReplies() {
        this.replies.sort((o1, o2) -> o2.getCreatedDate().compareTo(o1.getCreatedDate()));
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
