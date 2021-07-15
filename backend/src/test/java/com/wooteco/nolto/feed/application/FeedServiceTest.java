package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.NotFoundException;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.ui.dto.FeedCardResponse;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.tech.ui.dto.TechResponse;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class FeedServiceTest {
    private FeedRequest FEED_REQUEST1 = new FeedRequest("title1", new ArrayList<>(), "content", "PROGRESS", true,
            "storageUrl", "deployUrl", "feed_thumbnail.jpg");
    private FeedRequest FEED_REQUEST2 = new FeedRequest("title2", new ArrayList<>(), "content", "PROGRESS", true,
            "storageUrl", "deployUrl", "feed_thumbnail.jpg");
    private FeedRequest FEED_REQUEST3 = new FeedRequest("title3", new ArrayList<>(), "content", "PROGRESS", true,
            "storageUrl", "deployUrl", "feed_thumbnail.jpg");
    ;
    public static User USER = new User(null, "email@email.com", "password", "nickname", "mickey.jpg");

    @Autowired
    private FeedService feedService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(USER);
    }

    @DisplayName("유저가 feed를 작성한다.")
    @Test
    void create() {
        // when
        Long feedId = feedService.create(USER, FEED_REQUEST1);

        // then
        assertThat(feedId).isNotNull();
    }

    @DisplayName("Feed를 Id로 조회하고 조회수를 1 증가시킨다.")
    @Test
    void findById() {
        // given
        Long feedId = feedService.create(USER, FEED_REQUEST1);

        // when
        FeedResponse feedResponse = feedService.findById(USER, feedId);
        FEED_REQUEST1.toEntity();

        // then
        assertThat(feedResponse.getId()).isEqualTo(feedId);
        피드_정보가_같은지_조회(FEED_REQUEST1, feedResponse);
        assertThat(feedResponse.getViews()).isEqualTo(1);
    }

    @DisplayName("존재하지 않는 Feed Id로 조회하면 예외가 발생한다.")
    @Test
    void findByNonExistsId() {
        // when then
        assertThatThrownBy(() -> feedService.findById(USER, Long.MAX_VALUE))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("피드를 찾을 수 없습니다.");
    }

    @DisplayName("Feed Id로 entity 객체를 가져올 수 있다.")
    @Test
    void findEntityById() {
        // given
        Long feedId = feedService.create(USER, FEED_REQUEST1);

        // when
        Feed feedEntity = feedService.findEntityById(feedId);

        // then
        assertThat(feedEntity.getId()).isEqualTo(feedId);
        피드_정보가_같은지_조회(FEED_REQUEST1, feedEntity);
    }

    @DisplayName("존재하지 않는 Feed Id로 entity 객체를 조회하면 예외가 발생한다.")
    @Test
    void findEntityByNonExistsId() {
        // when
        assertThatThrownBy(() -> feedService.findEntityById(Long.MAX_VALUE))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("피드를 찾을 수 없습니다.");
    }

    @DisplayName("좋아요 개수가 높은 인기 Feed들을 가져온다.")
    @Test
    void findHotFeeds() {
        // given
        feedService.create(USER, FEED_REQUEST1);
        feedService.create(USER, FEED_REQUEST2);
        Long feedId = feedService.create(USER, FEED_REQUEST3);
        likeService.addLike(USER, feedId);

        // when
        List<FeedCardResponse> hotFeeds = feedService.findHotFeeds();

        // then
        hotFeeds.forEach(feedCardResponse -> System.out.println(feedCardResponse.getTitle()));
        assertThat(hotFeeds).hasSize(3);
    }

    @Test
    void findAll() {
    }

    private void 피드_정보가_같은지_조회(FeedRequest request, Feed feed) {
        assertThat(request.getTitle()).isEqualTo(feed.getTitle());
        assertThat(request.getTech()).containsExactlyElementsOf(feed.getTechs());
        assertThat(request.getContent()).isEqualTo(feed.getContent());
        assertThat(request.getStep()).isEqualTo(feed.getStep().name());
        assertThat(request.isSos()).isEqualTo(feed.isSos());
        assertThat(request.getStorageUrl()).isEqualTo(feed.getStorageUrl());
        assertThat(request.getDeployedUrl()).isEqualTo(feed.getDeployedUrl());
        assertThat(request.getThumbnailUrl()).isEqualTo(feed.getThumbnailUrl());
    }

    private void 피드_정보가_같은지_조회(FeedRequest request, FeedResponse response) {
        List<TechResponse> fromRequestTechs = TechResponse.toList(request.getTech());

        assertThat(request.getTitle()).isEqualTo(response.getTitle());
        assertThat(fromRequestTechs).containsExactlyElementsOf(response.getTechs());
        assertThat(request.getContent()).isEqualTo(response.getContent());
        assertThat(request.getStep()).isEqualTo(response.getStep());
        assertThat(request.isSos()).isEqualTo(response.isSos());
        assertThat(request.getStorageUrl()).isEqualTo(response.getStorageUrl());
        assertThat(request.getDeployedUrl()).isEqualTo(response.getDeployedUrl());
        assertThat(request.getThumbnailUrl()).isEqualTo(response.getThumbnailUrl());
    }
}