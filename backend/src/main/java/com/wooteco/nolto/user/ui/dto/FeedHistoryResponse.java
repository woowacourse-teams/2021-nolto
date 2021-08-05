package com.wooteco.nolto.user.ui.dto;

import com.wooteco.nolto.feed.domain.Feed;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class FeedHistoryResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String step;
    private final boolean sos;
    private final String thumbnailUrl;

    public static FeedHistoryResponse of(Feed feed) {
        return new FeedHistoryResponse(
                feed.getId(),
                feed.getTitle(),
                feed.getContent(),
                feed.getStep().name(),
                feed.isSos(),
                feed.getThumbnailUrl());
    }

    public static List<FeedHistoryResponse> toList(List<Feed> feeds) {
        return feeds.stream()
                .map(FeedHistoryResponse::of)
                .collect(Collectors.toList());
    }
}