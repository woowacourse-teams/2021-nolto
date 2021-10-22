package com.wooteco.nolto.feed.ui.dto;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.tech.ui.dto.TechResponse;
import com.wooteco.nolto.user.domain.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public FeedResponse(AuthorResponse author, Long id, String title, List<TechResponse> techs, String content, String step, boolean sos, String storageUrl, String deployedUrl, String thumbnailUrl, int likes, int views, boolean liked, LocalDateTime createdDate) {
        this.author = author;
        this.id = id;
        this.title = title;
        this.techs = techs;
        this.content = content;
        this.step = step;
        this.sos = sos;
        this.storageUrl = storageUrl;
        this.deployedUrl = deployedUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.likes = likes;
        this.views = views;
        this.liked = liked;
        this.createdDate = createdDate;
    }

    public static FeedResponse of(User author, Feed feed, boolean liked) {
        return new FeedResponse(AuthorResponse.of(author), feed.getId(), feed.getTitle(), TechResponse.toList(feed.getTechs()),
                feed.getContent(), feed.getStep().name(), feed.isSos(), feed.getStorageUrl(), feed.getDeployedUrl(),
                feed.getThumbnailUrl(), feed.getLikes().size(), feed.getViews(), liked, feed.getCreatedDate());
    }

    public static List<FeedResponse> toList(List<Feed> feeds) {
        return feeds.stream()
                .map(feed -> of(feed.getAuthor(), feed, false))
                .collect(Collectors.toList());
    }
}
