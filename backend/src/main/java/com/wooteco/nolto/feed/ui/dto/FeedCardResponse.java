package com.wooteco.nolto.feed.ui.dto;

import com.wooteco.nolto.feed.domain.Feed;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FeedCardResponse {
    private final AuthorResponse author;
    private final Long id;
    private final String title;
    private final String content;
    private final String step;
    private final boolean sos;
    private final String thumbnailUrl;

    public FeedCardResponse(AuthorResponse author, Long id, String title, String content, String step, boolean sos, String thumbnailUrl) {
        this.author = author;
        this.id = id;
        this.title = title;
        this.content = content;
        this.step = step;
        this.sos = sos;
        this.thumbnailUrl = thumbnailUrl;
    }

    public static FeedCardResponse of(Feed feed) {
        return new FeedCardResponse(
                AuthorResponse.of(feed.getAuthor()),
                feed.getId(),
                feed.getTitle(),
                feed.getContent(),
                feed.getStep().name(),
                feed.isSos(),
                feed.getThumbnailUrl());
    }

    public static List<FeedCardResponse> toList(Collection<Feed> feed) {
        return feed.stream()
                .map(FeedCardResponse::of)
                .collect(Collectors.toList());
    }
}
