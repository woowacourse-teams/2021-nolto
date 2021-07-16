package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.tech.domain.Tech;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
}
