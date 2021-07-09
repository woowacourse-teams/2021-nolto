package com.wooteco.nolto.feed.ui.dto;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FeedResponse {
    private final FeedAuthorResponse author;
    private final FeedDetailResponse feed;
    private final boolean liked;

    public static FeedResponse of(User author, Feed feed, boolean liked) {
        return new FeedResponse(FeedAuthorResponse.of(author), FeedDetailResponse.of(feed), liked);
    }
}
