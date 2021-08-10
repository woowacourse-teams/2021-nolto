package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Table(name = "Likes")
@Entity
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_likes_to_user"), nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_likes_to_feed"), nullable = false)
    private Feed feed;

    public Like(User user, Feed feed) {
        this.user = user;
        this.feed = feed;
    }

    public boolean hasFeed(Feed feed) {
        return this.feed.equals(feed);
    }

    public boolean sameAs(User user) {
        return this.user.sameAs(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Like like = (Like) o;
        return Objects.equals(id, like.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
