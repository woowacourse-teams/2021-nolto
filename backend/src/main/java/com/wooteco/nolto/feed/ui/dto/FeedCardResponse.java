package com.wooteco.nolto.feed.ui.dto;

import com.wooteco.nolto.feed.domain.Feed;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class FeedCardResponse {
    private final FeedAuthorResponse author;
    private final FeedSimpleResponse feed;

    public static FeedCardResponse of(Feed feed) {
        return new FeedCardResponse(FeedAuthorResponse.of(feed.getAuthor()), FeedSimpleResponse.of(feed));
    }

    public static List<FeedCardResponse> toList(List<Feed> feed) {
        return feed.stream()
                .map(FeedCardResponse::of)
                .collect(Collectors.toList());
    }
}
