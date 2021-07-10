package com.wooteco.nolto.feed.ui.dto;

import com.wooteco.nolto.feed.domain.Feed;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedCardResponse {
    private final FeedAuthorResponse author;
    private final FeedSimpleResponse feed;

    public static FeedCardResponse of(Feed feed) {
        return new FeedCardResponse(FeedAuthorResponse.of(feed.getAuthor()), FeedSimpleResponse.of(feed));
    }
}
