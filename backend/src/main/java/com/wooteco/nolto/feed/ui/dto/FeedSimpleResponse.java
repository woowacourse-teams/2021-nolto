package com.wooteco.nolto.feed.ui.dto;

import com.wooteco.nolto.feed.domain.Feed;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedSimpleResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String step;
    private final boolean sos;
    private final String thumbnailUrl;

    public static FeedSimpleResponse of(Feed feed) {
        return new FeedSimpleResponse(
                feed.getId(),
                feed.getTitle(),
                feed.getContent(),
                feed.getStep().name(),
                feed.isSos(),
                feed.getThumbnailUrl()
        );
    }
}
