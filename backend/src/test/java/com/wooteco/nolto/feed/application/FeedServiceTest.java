package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.NoltoApplication;
import com.wooteco.nolto.NotFoundException;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.ui.dto.FeedCardResponse;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.image.application.ImageService;
import com.wooteco.nolto.tech.ui.dto.TechResponse;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class FeedServiceTest {
    private FeedRequest FEED_REQUEST1 = new FeedRequest("title1", new ArrayList<>(), "content", "PROGRESS", true,
            "storageUrl", "deployUrl", null);
    private FeedRequest FEED_REQUEST2 = new FeedRequest("title2", new ArrayList<>(), "content", "PROGRESS", true,
            "storageUrl", "deployUrl", null);
    private FeedRequest FEED_REQUEST3 = new FeedRequest("title3", new ArrayList<>(), "content", "PROGRESS", true,
            "storageUrl", "deployUrl", null);

    private User user1 = new User(null, "email@email.com", "password", "user1", "mickey.jpg");
    private User user2 = new User(null, "email2@email.com", "password", "user2", "mickey.jpg");

    @Autowired
    private FeedService feedService;

    @Autowired
    private LikeService likeService;

    @MockBean
    private ImageService imageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        given(imageService.upload(any(MultipartFile.class))).willReturn("image.jpg");
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @DisplayName("유저가 feed를 작성한다.")
    @Test
    void create() {
        // when
        Long feedId = feedService.create(user1, FEED_REQUEST1);

        // then
        assertThat(feedId).isNotNull();
    }

    @DisplayName("Feed를 Id로 조회하고 조회수를 1 증가시킨다.")
    @Test
    void findById() {
        // given
        Long feedId = feedService.create(user1, FEED_REQUEST1);

        // when
        FeedResponse feedResponse = feedService.findById(user1, feedId);
        FEED_REQUEST1.toEntityWithThumbnailUrl(null);

        // then
        assertThat(feedResponse.getId()).isEqualTo(feedId);
        피드_정보가_같은지_조회(FEED_REQUEST1, feedResponse);
        assertThat(feedResponse.getViews()).isEqualTo(1);
    }

    @DisplayName("존재하지 않는 Feed Id로 조회하면 예외가 발생한다.")
    @Test
    void findByNonExistsId() {
        // when then
        assertThatThrownBy(() -> feedService.findById(user1, Long.MAX_VALUE))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("피드를 찾을 수 없습니다.");
    }

    @DisplayName("Feed Id로 entity 객체를 가져올 수 있다.")
    @Test
    void findEntityById() {
        // given
        Long feedId = feedService.create(user1, FEED_REQUEST1);

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
        Long firstFeedId = feedService.create(user1, FEED_REQUEST1);
        Long secondFeedId = feedService.create(user1, FEED_REQUEST2);
        Long thirdFeedId = feedService.create(user1, FEED_REQUEST3);

        likeService.addLike(user2, secondFeedId);
        likeService.addLike(user2, thirdFeedId);
        likeService.addLike(user1, secondFeedId);
        em.flush();
        em.clear();

        // when
        List<FeedCardResponse> hotFeeds = feedService.findHotFeeds();

        // then
        assertThat(hotFeeds).hasSize(3);
        피드_정보가_같은지_조회(FEED_REQUEST2, hotFeeds.get(0));
        피드_정보가_같은지_조회(FEED_REQUEST3, hotFeeds.get(1));
        피드_정보가_같은지_조회(FEED_REQUEST1, hotFeeds.get(2));
    }

    @DisplayName("모든 피드를 조회한다. (최신 등록된 id 순)")
    @Test
    void findAllWithAllFilter() {
        // given
        String defaultFilter = "all";
        feedService.create(user1, FEED_REQUEST1);
        feedService.create(user1, FEED_REQUEST2);
        feedService.create(user1, FEED_REQUEST3);

        // when
        List<FeedCardResponse> allFeeds = feedService.findAll(defaultFilter);

        // then
        assertThat(allFeeds.size()).isEqualTo(3);
        피드_정보가_같은지_조회(FEED_REQUEST1, allFeeds.get(2));
        피드_정보가_같은지_조회(FEED_REQUEST2, allFeeds.get(1));
        피드_정보가_같은지_조회(FEED_REQUEST3, allFeeds.get(0));
    }

    @DisplayName("SOS 피드를 조회한다. (최신 등록된 id 순)")
    @Test
    void findAllWithFilterSOS() {
        // given
        FEED_REQUEST1.setSos(true);
        FEED_REQUEST2.setSos(false);
        FEED_REQUEST3.setSos(true);

        String defaultFilter = "sos";
        feedService.create(user1, FEED_REQUEST1);
        feedService.create(user1, FEED_REQUEST2);
        feedService.create(user1, FEED_REQUEST3);

        // when
        List<FeedCardResponse> allFeeds = feedService.findAll(defaultFilter);

        // then
        assertThat(allFeeds.size()).isEqualTo(2);
        피드_정보가_같은지_조회(FEED_REQUEST1, allFeeds.get(1));
        피드_정보가_같은지_조회(FEED_REQUEST3, allFeeds.get(0));
    }

    @DisplayName("PROGRESS 피드를 조회한다. (최신 등록된 id 순)")
    @Test
    void findAllWithFilterProgress() {
        // given
        FEED_REQUEST1.setStep(Step.COMPLETE.name());
        FEED_REQUEST2.setStep(Step.PROGRESS.name());
        FEED_REQUEST3.setStep(Step.PROGRESS.name());

        feedService.create(user1, FEED_REQUEST1);
        feedService.create(user1, FEED_REQUEST2);
        feedService.create(user1, FEED_REQUEST3);

        // when
        List<FeedCardResponse> allFeeds = feedService.findAll(Step.PROGRESS.name());

        // then
        assertThat(allFeeds.size()).isEqualTo(2);
        피드_정보가_같은지_조회(FEED_REQUEST3, allFeeds.get(0));
        피드_정보가_같은지_조회(FEED_REQUEST2, allFeeds.get(1));
    }

    @DisplayName("COMPLETE 피드를 조회한다. (최신 등록된 id 순)")
    @Test
    void findAllWithFilterComplete() {
        // given
        FEED_REQUEST1.setStep(Step.COMPLETE.name());
        FEED_REQUEST2.setStep(Step.COMPLETE.name());
        FEED_REQUEST3.setStep(Step.PROGRESS.name());

        feedService.create(user1, FEED_REQUEST1);
        feedService.create(user1, FEED_REQUEST2);
        feedService.create(user1, FEED_REQUEST3);

        // when
        List<FeedCardResponse> allFeeds = feedService.findAll(Step.COMPLETE.name());

        // then
        피드_정보가_같은지_조회(FEED_REQUEST2, allFeeds.get(0));
        피드_정보가_같은지_조회(FEED_REQUEST1, allFeeds.get(1));
    }

    private void 피드_정보가_같은지_조회(FeedRequest request, Feed feed) {
        assertThat(request.getTitle()).isEqualTo(feed.getTitle());
        assertThat(request.getTech()).containsExactlyElementsOf(feed.getTechs());
        assertThat(request.getContent()).isEqualTo(feed.getContent());
        assertThat(request.getStep()).isEqualTo(feed.getStep().name());
        assertThat(request.isSos()).isEqualTo(feed.isSos());
        assertThat(request.getStorageUrl()).isEqualTo(feed.getStorageUrl());
        assertThat(request.getDeployedUrl()).isEqualTo(feed.getDeployedUrl());
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
    }

    private void 피드_정보가_같은지_조회(FeedRequest request, FeedCardResponse response) {
        assertThat(request.getTitle()).isEqualTo(response.getTitle());
        assertThat(request.getContent()).isEqualTo(response.getContent());
        assertThat(request.getStep()).isEqualTo(response.getStep());
        assertThat(request.isSos()).isEqualTo(response.isSos());
    }
}