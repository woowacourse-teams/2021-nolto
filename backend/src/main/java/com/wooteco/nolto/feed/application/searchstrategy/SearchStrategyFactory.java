package com.wooteco.nolto.feed.application.searchstrategy;

import com.wooteco.nolto.feed.application.FeedTechService;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;

import java.util.Arrays;
import java.util.function.BiFunction;

public enum SearchStrategyFactory {
    NONE(true, true, NoneStrategy::new),
    QUERY_ONLY(false, true, QueryOnlyStrategy::new),
    TECHS_ONLY(true, false, TechsOnlyStrategy::new),
    QUERY_AND_TECHS(false, false, QueryAndTechsStrategy::new);

    private final boolean isQueryEmpty;
    private final boolean isTechsEmpty;
    private final BiFunction<FeedRepository, FeedTechService, SearchStrategy> findSearchStrategy;

    SearchStrategyFactory(boolean isQueryEmpty, boolean isTechsEmpty,
                          BiFunction<FeedRepository, FeedTechService, SearchStrategy> findSearchStrategy) {
        this.isQueryEmpty = isQueryEmpty;
        this.isTechsEmpty = isTechsEmpty;
        this.findSearchStrategy = findSearchStrategy;
    }

    public static SearchStrategyFactory of(String query, String techs) {
        return Arrays.stream(values())
                .filter(searchStrategyFactory -> searchStrategyFactory.isQueryEmpty == query.isEmpty())
                .filter(searchStrategyFactory -> searchStrategyFactory.isTechsEmpty == techs.isEmpty())
                .findFirst()
                .orElse(NONE);
    }

    public SearchStrategy findStrategy(FeedRepository feedRepository, FeedTechService feedTechService) {
        return findSearchStrategy.apply(feedRepository, feedTechService);
    }
}
