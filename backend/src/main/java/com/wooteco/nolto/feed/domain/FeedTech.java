package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.tech.domain.Tech;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
public class FeedTech {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Tech tech;

    public FeedTech(Feed feed, Tech tech) {
        this.feed = feed;
        this.tech = tech;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedTech feedTech = (FeedTech) o;
        return Objects.equals(id, feedTech.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
