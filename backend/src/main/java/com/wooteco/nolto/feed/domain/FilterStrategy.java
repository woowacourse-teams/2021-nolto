package com.wooteco.nolto.feed.domain;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum FilterStrategy {
    ALL(feed -> true),
    SOS(Feed::isSos),
    PROGRESS(feed -> feed.getStep().equals(Step.PROGRESS)),
    COMPLETE(feed -> feed.getStep().equals(Step.COMPLETE));

    private Function<Feed, Boolean> function;

    FilterStrategy(Function<Feed, Boolean> function) {
        this.function = function;
    }

    public static FilterStrategy of(String value) {
        return Arrays.stream(FilterStrategy.values())
                .filter(filterStrategy -> filterStrategy.name().equalsIgnoreCase(value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 피드의 필터링 값입니다."));
    }

    public List<Feed> execute(List<Feed> feedCards) {
        return feedCards.stream()
                .filter(feed -> this.function.apply(feed))
                .collect(Collectors.toList());
    }
}
