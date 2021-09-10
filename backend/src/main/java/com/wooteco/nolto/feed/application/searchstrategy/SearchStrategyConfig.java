package com.wooteco.nolto.feed.application.searchstrategy;

import com.wooteco.nolto.feed.application.FeedTechService;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SearchStrategyConfig {

    private final FeedRepository feedRepository;
    private final FeedTechService feedTechService;

    @Bean
    public NoneStrategy createNoneStrategy() {
        return new NoneStrategy(feedRepository, feedTechService);
    }

    @Bean
    public QueryOnlyStrategy createQueryOnlyStrategy() {
        return new QueryOnlyStrategy(feedRepository, feedTechService);
    }

    @Bean
    public TechsOnlyStrategy createTechsOnlyStrategy() {
        return new TechsOnlyStrategy(feedRepository, feedTechService);
    }

    @Bean
    public QueryAndTechsStrategy createQueryAndTechStrategy() {
        return new QueryAndTechsStrategy(feedRepository, feedTechService);
    }
}
