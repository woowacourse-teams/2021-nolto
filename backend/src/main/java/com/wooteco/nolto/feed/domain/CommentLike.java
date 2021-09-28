package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_comment_likes_to_user"), nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_comment_likes_to_comment"), nullable = false)
    private Comment comment;

    public CommentLike(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }

    public boolean hasComment(Comment comment) {
        return this.comment.equals(comment);
    }

    public boolean sameAs(User user) {
        return this.user.sameAs(user);
    }

    public void deleteByComment() {
        this.user.delete(this);
        this.user = null;
        this.comment = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentLike that = (CommentLike) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
