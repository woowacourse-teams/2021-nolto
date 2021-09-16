package com.wooteco.nolto.feed.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Feeds {

    private final List<Feed> values;

    public Feeds(List<Feed> values) {
        this.values = values;
    }

    public List<Feed> sortedByLikeCount(int limit) {
        return this.values.stream()
                .sorted((o1, o2) -> o2.likesCount() - o1.likesCount())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Feed> filter(FilterStrategy filterStrategy) {
        return filterStrategy.execute(this.values);
    }
}
