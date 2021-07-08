package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.FeedRepository;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class FeedService {

    private final FeedRepository feedRepository;

    public FeedResponse create(User user, FeedRequest request) {
        Feed feed = request.toEntity().writtenBy(user);
        Feed savedFeed = feedRepository.save(feed);
        return FeedResponse.of(savedFeed);
    }
}
