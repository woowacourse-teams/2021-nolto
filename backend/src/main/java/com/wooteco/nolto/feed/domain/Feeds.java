package com.wooteco.nolto.feed.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Feeds {

    private List<Feed> feeds;

    public Feeds(List<Feed> feeds) {
        this.feeds = feeds;
    }

    public List<Feed> sortedByLikeCount(int limit) {
        return this.feeds.stream()
                .sorted((o1, o2) -> o2.likesCount() - o1.likesCount())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Feed> filter(FilterStrategy filterStrategy) {
        return filterStrategy.execute(this.feeds);
    }
}
