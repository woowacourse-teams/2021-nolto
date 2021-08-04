package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.BaseEntity;
import com.wooteco.nolto.user.domain.User;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
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
        this.feed = feed;
    }

    public boolean isModified() {
        return getModifiedDate().isAfter(getCreatedDate());
    }

    public boolean isFeedAuthor() {
        return feed.getAuthor().equals(this.author);
    }
}
