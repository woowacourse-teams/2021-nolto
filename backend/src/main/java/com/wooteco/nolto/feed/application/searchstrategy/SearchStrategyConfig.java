package com.wooteco.nolto.feed.application.searchstrategy;

import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SearchStrategyConfig {

    private final FeedRepository feedRepository;

    @Bean
    public NoneStrategy createNoneStrategy() {
        return new NoneStrategy(feedRepository);
    }

    @Bean
    public QueryOnlyStrategy createQueryOnlyStrategy() {
        return new QueryOnlyStrategy(feedRepository);
    }

    @Bean
    public TechsOnlyStrategy createTechsOnlyStrategy() {
        return new TechsOnlyStrategy(feedRepository);
    }

    @Bean
    public QueryAndTechsStrategy createQueryAndTechStrategy() {
        return new QueryAndTechsStrategy(feedRepository);
    }
}
