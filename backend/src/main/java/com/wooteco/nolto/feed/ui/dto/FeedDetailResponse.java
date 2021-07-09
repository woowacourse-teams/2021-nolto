package com.wooteco.nolto.feed.ui.dto;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.tech.ui.dto.TechResponse;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class FeedDetailResponse {

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

    public static FeedDetailResponse of(Feed feed) {
        return new FeedDetailResponse(feed.getId(), feed.getTitle(), TechResponse.of(feed.getTechs()), feed.getContent(),
                feed.getStep().name(), feed.isSos(), feed.getStorageUrl(), feed.getDeployedUrl(), feed.getThumbnailUrl(),
                feed.getLikes().size(), feed.getViews());
    }

}
