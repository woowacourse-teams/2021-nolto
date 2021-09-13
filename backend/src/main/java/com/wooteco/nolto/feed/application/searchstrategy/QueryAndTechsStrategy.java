package com.wooteco.nolto.feed.application.searchstrategy;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;

import java.util.List;

public class QueryAndTechsStrategy extends SearchStrategy {

    public QueryAndTechsStrategy(FeedRepository feedRepository) {
        super(feedRepository);
    }

    @Override
    public List<Feed> search(String query, String techs) {
        List<Feed> searchFeedByQuery = searchByQuery(query);
        List<Feed> searchFeedByTechs = searchByTechs(techs);
        searchFeedByQuery.retainAll(searchFeedByTechs);
        return searchFeedByQuery;
    }
}
