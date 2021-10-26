package com.wooteco.nolto.admin.ui.dto;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.ui.dto.CommentAndReplyResponse;
import com.wooteco.nolto.feed.ui.dto.FeedCardResponse;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentsByFeedResponse {

    private final FeedCardResponse feed;
    private final List<CommentAndReplyResponse> comments;

    public CommentsByFeedResponse(FeedCardResponse feed, List<CommentAndReplyResponse> comments) {
        this.feed = feed;
        this.comments = comments;
    }

    public static CommentsByFeedResponse of(Feed feed) {
        return new CommentsByFeedResponse(
                FeedCardResponse.of(feed),
                CommentAndReplyResponse.toList(feed.mapByCommentAndReplies())
        );
    }

    public static List<CommentsByFeedResponse> toList(List<Feed> feeds) {
        return feeds.stream()
                .map(CommentsByFeedResponse::of)
                .collect(Collectors.toList());
    }
}
