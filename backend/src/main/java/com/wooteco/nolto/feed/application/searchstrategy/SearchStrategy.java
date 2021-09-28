package com.wooteco.nolto.feed.application.searchstrategy;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import org.springframework.data.domain.Pageable;

import java.util.EnumSet;
import java.util.List;

public abstract class SearchStrategy {

    protected static final String TECH_SEARCH_DELIMITER = ",";

    protected final FeedRepository feedRepository;

    protected SearchStrategy(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    public abstract List<Feed> searchWithCondition(String query, String techs, boolean help, long nextFeedId, EnumSet<Step> steps, Pageable pageable);
}
