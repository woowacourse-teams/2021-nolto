package com.wooteco.nolto.user.ui.dto;

import com.wooteco.nolto.feed.domain.Feed;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FeedHistoryResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String step;
    private final boolean sos;
    private final String thumbnailUrl;

    public FeedHistoryResponse(Long id, String title, String content, String step, boolean sos, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.step = step;
        this.sos = sos;
        this.thumbnailUrl = thumbnailUrl;
    }

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