package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.NotFoundException;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Feeds;
import com.wooteco.nolto.feed.domain.FilterStrategy;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.feed.ui.dto.FeedCardResponse;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.image.application.ImageService;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@AllArgsConstructor
@Service
public class FeedService {

    private final ImageService imageService;
    private final FeedRepository feedRepository;
    private final FeedTechService feedTechService;

    public Long create(User user, FeedRequest request) {
        String thumbnailUrl = imageService.upload(request.getThumbnailImage());
        Feed feed = request.toEntityWithThumbnailUrl(thumbnailUrl).writtenBy(user);
        Feed savedFeed = feedRepository.save(feed);
        feedTechService.save(savedFeed, request.getTechs());
        return savedFeed.getId();
    }

    public void update(User user, Long feedId, FeedRequest request) {
        Feed findFeed = findEntityById(feedId);
        findFeed.setTitle(request.getTitle());
        findFeed.setContent(request.getContent());
        findFeed.setStep(Step.of(request.getStep()));
        findFeed.setSos(request.isSos());
        findFeed.setStorageUrl(request.getStorageUrl());
        findFeed.setDeployedUrl(request.getDeployedUrl());

        String updateThumbnailUrl = imageService.update(findFeed.getThumbnailUrl(), request.getThumbnailImage());
        findFeed.setThumbnailUrl(updateThumbnailUrl);

        feedTechService.update(findFeed, request.getTechs());
    }

    public FeedResponse findById(User user, Long feedId) {
        Feed feed = findEntityById(feedId);
        User author = feed.getAuthor();
        boolean liked = user.isLiked(feed);
        feed.increaseView();
        return FeedResponse.of(author, feed, liked);
    }

    public Feed findEntityById(Long feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(() -> new NotFoundException("피드를 찾을 수 없습니다."));
    }

    public List<FeedCardResponse> findHotFeeds() {
        Feeds feeds = new Feeds(feedRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate")));
        return FeedCardResponse.toList(feeds.sortedByLikeCount(10));
    }

    public List<FeedCardResponse> findAll(String filter) {
        FilterStrategy strategy = FilterStrategy.of(filter);
        Feeds feeds = new Feeds(feedRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate")));
        return FeedCardResponse.toList(feeds.filter(strategy));
    }
}
