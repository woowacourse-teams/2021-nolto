package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.NotFoundException;
import com.wooteco.nolto.exception.UnauthorizedException;
import com.wooteco.nolto.feed.application.searchstrategy.SearchStrategy;
import com.wooteco.nolto.feed.application.searchstrategy.SearchStrategyFactory;
import com.wooteco.nolto.feed.domain.*;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.feed.domain.repository.FeedTechRepository;
import com.wooteco.nolto.feed.ui.dto.FeedCardPaginationResponse;
import com.wooteco.nolto.feed.ui.dto.FeedCardResponse;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.image.application.ImageKind;
import com.wooteco.nolto.image.application.ImageService;
import com.wooteco.nolto.notification.application.NotificationFeedDeleteEvent;
import com.wooteco.nolto.tech.domain.TechRepository;
import com.wooteco.nolto.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.EnumSet;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class FeedService {

    private static final int NEXT_FEED_COUNT = 1;

    private final ImageService imageService;
    private final FeedRepository feedRepository;
    private final TechRepository techRepository;
    private final FeedTechRepository feedTechRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public Long create(User user, FeedRequest request) {
        String thumbnailUrl = imageService.upload(request.getThumbnailImage(), ImageKind.FEED);

        Feed feed = request.toEntityWithThumbnailUrl(thumbnailUrl).writtenBy(user);
        feed.changeTechs(techRepository.findAllById(request.getTechs()));

        Feed savedFeed = feedRepository.save(feed);
        return savedFeed.getId();
    }

    public void update(User user, Long feedId, FeedRequest request) {
        Feed findFeed = user.findMyFeed(feedId);
        List<FeedTech> feedTechs = findFeed.getFeedTechs();
        feedTechRepository.deleteAll(feedTechs);
        feedTechs.clear();
        updateFeed(request, findFeed);
    }

    private void updateFeed(FeedRequest request, Feed findFeed) {
        findFeed.update(
                request.getTitle(),
                request.getContent(),
                Step.of(request.getStep()),
                request.isSos(),
                request.getStorageUrl(),
                request.getDeployedUrl()
        );

        updateThumbnailIfImageExist(request, findFeed);
        findFeed.changeTechs(techRepository.findAllById(request.getTechs()));
    }

    private void updateThumbnailIfImageExist(FeedRequest request, Feed findFeed) {
        if (imageService.isEmpty(request.getThumbnailImage())) return;

        String updateThumbnailUrl = imageService.update(findFeed.getThumbnailUrl(), request.getThumbnailImage(), ImageKind.FEED);
        findFeed.changeThumbnailUrl(updateThumbnailUrl);
    }

    public FeedResponse viewFeed(User user, Long feedId, boolean alreadyView) {
        Feed feed = findEntityById(feedId);
        User author = feed.getAuthor();
        boolean liked = user.isLiked(feed);
        feed.increaseView(alreadyView);
        return FeedResponse.of(author, feed, liked);
    }

    public Feed findEntityById(Long feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(() -> new NotFoundException(ErrorType.FEED_NOT_FOUND));
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

    public void delete(User user, Long feedId) {
        Feed findFeed = findEntityById(feedId);
        if (findFeed.notSameAuthor(user)) {
            throw new UnauthorizedException(ErrorType.UNAUTHORIZED_DELETE_FEED);
        }
        applicationEventPublisher.publishEvent(new NotificationFeedDeleteEvent(findFeed));
        feedRepository.delete(findFeed);
    }

    public FeedCardPaginationResponse findRecentFeeds(String step, boolean help, long nextFeedId, int countPerPage) {
        EnumSet<Step> steps = Step.asEnumSet(step);
        Pageable pageable = PageRequest.of(0, countPerPage + NEXT_FEED_COUNT);
        List<Feed> findFeeds = findRecentFeedsWithCondition(help, nextFeedId, steps, pageable);
        return generateFeedCardPaginationResponse(countPerPage, findFeeds);
    }

    private List<Feed> findRecentFeedsWithCondition(boolean help, long nextFeedId, EnumSet<Step> steps, Pageable pageable) {
        if (help) {
            return feedRepository.findWithHelp(steps, true, nextFeedId, pageable);
        }
        return feedRepository.findWithoutHelp(steps, nextFeedId, pageable);
    }

    private FeedCardPaginationResponse generateFeedCardPaginationResponse(int countPerPage, List<Feed> findFeeds) {
        if (findFeeds.size() == countPerPage + NEXT_FEED_COUNT) {
            Feed nextFeed = findFeeds.get(countPerPage);
            findFeeds.remove(nextFeed);
            return FeedCardPaginationResponse.of(findFeeds, nextFeed);
        }
        return FeedCardPaginationResponse.of(findFeeds, null);
    }

    public FeedCardPaginationResponse search(String query, String techs, String step, boolean help, long nextFeedId, int countPerPage) {
        EnumSet<Step> steps = Step.asEnumSet(step);
        Pageable pageable = PageRequest.of(0, countPerPage + NEXT_FEED_COUNT);
        SearchStrategy searchStrategy = SearchStrategyFactory.of(query, techs).findStrategy();
        List<Feed> findFeeds = searchStrategy.searchWithCondition(query, techs, help, nextFeedId, steps, pageable);
        return generateFeedCardPaginationResponse(countPerPage, findFeeds);
    }
}
