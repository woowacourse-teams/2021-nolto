package com.wooteco.nolto.feed.application.searchstrategy;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public abstract class SearchStrategy {

    private static final String TECH_SEARCH_DELIMITER = ",";

    protected final FeedRepository feedRepository;

    protected SearchStrategy(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    protected List<Feed> searchByQuery(String query) {
        return feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query, query);
    }

    protected List<Feed> searchByTechs(String techs) {
        List<String> techNames = Arrays.asList(techs.split(TECH_SEARCH_DELIMITER));
        return feedRepository.findByTechs(techNames);
    }

    public abstract List<Feed> search(String query, String techs);

    public abstract List<Feed> searchWithCondition(String query, String techs, boolean help, long nextFeedId, EnumSet<Step> steps, Pageable pageable);
}
