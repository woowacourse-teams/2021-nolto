package com.wooteco.nolto.feed.domain;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class Feeds {

    private List<Feed> feeds;

    public List<Feed> sortedByLikeCount(int limit) {
        return this.feeds.stream()
                .sorted((o1, o2) -> o2.likesCount() - o1.likesCount())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Feed> filter(FilterStrategy filterStrategy) {
        return filterStrategy.execute(this.feeds);
    }

    public Feed findLastFeed() {
        return feeds.get(feeds.size() - 1);
    }

    public List<Feed> getFeeds() {
        return feeds;
    }
}
