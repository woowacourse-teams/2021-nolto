package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.NotFoundException;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.FeedRepository;
import com.wooteco.nolto.feed.ui.dto.*;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class FeedService {

    private final FeedRepository feedRepository;

    public FeedDetailResponse create(User user, FeedRequest request) {
        Feed feed = request.toEntity().writtenBy(user);
        Feed savedFeed = feedRepository.save(feed);
        return FeedDetailResponse.of(savedFeed);
    }

    public FeedResponse findById(User user, Long feedId) {
        Feed feed = findEntityById(feedId);
        User author = feed.getAuthor();
        boolean liked = user.isLiked(feed);
        return FeedResponse.of(author, feed, liked);
    }

    public Feed findEntityById(Long feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(() -> new NotFoundException("피드를 찾을 수 없습니다."));
    }

    public List<FeedCardResponse> findAll() {
        return feedRepository.findAll()
                .stream()
                .map(FeedCardResponse::of)
                .collect(Collectors.toList());
    }
}
