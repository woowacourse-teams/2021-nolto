package com.wooteco.nolto.feed.application.searchstrategy;

import com.wooteco.nolto.feed.application.FeedTechService;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class SearchStrategy {

    private static final String TECH_SEARCH_DELIMITER = ",";

    private final FeedRepository feedRepository;
    private final FeedTechService feedTechService;

    public SearchStrategy(FeedRepository feedRepository, FeedTechService feedTechService) {
        this.feedRepository = feedRepository;
        this.feedTechService = feedTechService;
    }

    protected Set<Feed> searchByQuery(String query) {
        return feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query, query);
    }

    protected Set<Feed> searchByTechs(String techs) {
        List<String> techNames = Arrays.asList(techs.split(TECH_SEARCH_DELIMITER));
        return feedTechService.findFeedUsingTech(techNames);
    }

    public abstract Set<Feed> search(String query, String techs);
}
