package com.wooteco.nolto.feed.ui.dto;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.tech.ui.dto.TechResponse;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class FeedResponse {
    private final AuthorResponse author;
    private final Long id;
    private final String title;
    private final List<TechResponse> techs;
    private final String content;
    private final String step;
    private final boolean sos;
    private final String storageUrl;
    private final String deployedUrl;
    private final String thumbnailUrl;
    private final int likes;
    private final int views;
    private final boolean liked;
    private final LocalDateTime createdDate;

    public static FeedResponse of(User author, Feed feed, boolean liked) {
        return new FeedResponse(AuthorResponse.of(author), feed.getId(), feed.getTitle(), TechResponse.toList(feed.getTechs()),
                feed.getContent(), feed.getStep().name(), feed.isSos(), feed.getStorageUrl(), feed.getDeployedUrl(),
                feed.getThumbnailUrl(), feed.getLikes().size(), feed.getViews(), liked, feed.getCreatedDate());
    }
}
