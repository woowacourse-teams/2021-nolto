package com.wooteco.nolto.feed.application.searchstrategy;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

public enum SearchStrategyFactory {
    NONE(true, true),
    QUERY_ONLY(false, true),
    TECHS_ONLY(true, false),
    QUERY_AND_TECHS(false, false);

    private final boolean isQueryEmpty;
    private final boolean isTechsEmpty;
    private SearchStrategy searchStrategy;

    SearchStrategyFactory(boolean isQueryEmpty, boolean isTechsEmpty) {
        this.isQueryEmpty = isQueryEmpty;
        this.isTechsEmpty = isTechsEmpty;
    }

    public static SearchStrategyFactory of(String query, String techs) {
        return Arrays.stream(values())
                .filter(searchStrategyFactory -> searchStrategyFactory.isQueryEmpty == query.isEmpty())
                .filter(searchStrategyFactory -> searchStrategyFactory.isTechsEmpty == techs.isEmpty())
                .findFirst()
                .orElse(NONE);
    }

    private void setSearchStrategy(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public SearchStrategy findStrategy() {
        return this.searchStrategy;
    }

    @Component
    @AllArgsConstructor
    public static class StrategyInjector {
        private NoneStrategy noneStrategy;
        private QueryOnlyStrategy queryOnlyStrategy;
        private TechsOnlyStrategy techsOnlyStrategy;
        private QueryAndTechsStrategy queryAndTechsStrategy;

        @PostConstruct
        public void inject() {
            NONE.setSearchStrategy(noneStrategy);
            QUERY_ONLY.setSearchStrategy(queryOnlyStrategy);
            TECHS_ONLY.setSearchStrategy(techsOnlyStrategy);
            QUERY_AND_TECHS.setSearchStrategy(queryAndTechsStrategy);
        }
    }
}
