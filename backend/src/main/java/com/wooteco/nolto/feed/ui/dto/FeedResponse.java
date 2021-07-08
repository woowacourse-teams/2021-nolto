package com.wooteco.nolto.feed.ui.dto;

import com.wooteco.nolto.feed.domain.Feed;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedResponse {

    private final Long id;

    public static FeedResponse of(Feed feed) {
        return new FeedResponse(feed.getId());
    }
}
