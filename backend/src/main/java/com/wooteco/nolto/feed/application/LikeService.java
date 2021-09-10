package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.exception.BadRequestException;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Like;
import com.wooteco.nolto.feed.domain.repository.LikeRepository;
import com.wooteco.nolto.notification.application.NotificationEvent;
import com.wooteco.nolto.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final FeedService feedService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void addLike(User user, Long feedId) {
        Feed findFeed = feedService.findEntityById(feedId);
        if (user.isLiked(findFeed)) {
            throw new BadRequestException(ErrorType.ALREADY_LIKED);
        }
        user.addLike(new Like(user, findFeed));
        applicationEventPublisher.publishEvent(NotificationEvent.likeOf(findFeed, user));
    }

    public void deleteLike(User user, Long feedId) {
        Feed findFeed = feedService.findEntityById(feedId);
        Like findLike = findFeed.findLikeBy(user)
                .orElseThrow(() -> new BadRequestException(ErrorType.NOT_LIKED));
        user.delete(findLike);
        likeRepository.delete(findLike);
    }
}
