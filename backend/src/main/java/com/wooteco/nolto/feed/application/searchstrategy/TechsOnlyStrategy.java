package com.wooteco.nolto.feed.application.searchstrategy;

import com.wooteco.nolto.feed.application.FeedTechService;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;

import java.util.Set;

public class TechsOnlyStrategy extends SearchStrategy {

    public TechsOnlyStrategy(FeedRepository feedRepository, FeedTechService feedTechService) {
        super(feedRepository, feedTechService);
    }

    @Override
    public Set<Feed> search(String query, String techs) {
        return searchByTechs(techs);
    }
}
