package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.NotFoundException;
import com.wooteco.nolto.exception.UnauthorizedException;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.FeedTech;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.feed.ui.dto.FeedCardResponse;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.image.application.ImageService;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import com.wooteco.nolto.tech.ui.dto.TechResponse;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional
class FeedServiceTest {
    private FeedRequest FEED_REQUEST1 = new FeedRequest("title1", new ArrayList<>(), "content1", "PROGRESS", true,
            "www.github.com/woowacourse", "www.github.com/woowacourse", null);
    private FeedRequest FEED_REQUEST2 = new FeedRequest("title2", new ArrayList<>(), "content2", "PROGRESS", true,
            "www.github.com/woowacourse", "www.github.com/woowacourse", null);
    private FeedRequest FEED_REQUEST3 = new FeedRequest("title3", new ArrayList<>(), "content3", "PROGRESS", true,
            "www.github.com/woowacourse", "www.github.com/woowacourse", null);

    private User user1 = new User("123456L", SocialType.GITHUB, "user1", "mickey.jpg");
    private User user2 = new User("654321L", SocialType.GOOGLE, "user2", "mickey.jpg");

    private Tech techSpring = new Tech("Spring");
    private Tech techJava = new Tech("Java");
    private Tech techReact = new Tech("React");
    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private FeedService feedService;

    @Autowired
    private LikeService likeService;

    @MockBean
    private ImageService imageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TechRepository techRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        given(imageService.upload(any(MultipartFile.class), any())).willReturn("image.jpg");
        userRepository.save(user1);
        userRepository.save(user2);

        Tech savedTech1 = techRepository.save(techSpring);
        Tech savedTech2 = techRepository.save(techJava);
        Tech savedTech3 = techRepository.save(techReact);

        FEED_REQUEST2.setTechs(Arrays.asList(savedTech1.getId(), savedTech2.getId()));
        FEED_REQUEST3.setTechs(Collections.singletonList(savedTech3.getId()));
    }

    @DisplayName("유저가 feed를 작성한다.")
    @Test
    void create() {
        // when
        FEED_REQUEST1.setTechs(Arrays.asList(techSpring.getId()));
        Long feedId1 = feedService.create(user1, FEED_REQUEST1);
        Long feedId2 = feedService.create(user1, FEED_REQUEST2);
        Long feedId3 = feedService.create(user2, FEED_REQUEST3);

        em.flush();
        em.clear();

        Feed savedFeed1 = feedService.findEntityById(feedId1);
        Feed savedFeed2 = feedService.findEntityById(feedId2);
        Feed savedFeed3 = feedService.findEntityById(feedId3);

        // then
        assertThat(feedId1).isNotNull();
        assertThat(feedId2).isNotNull();
        assertThat(feedId3).isNotNull();
        피드_정보가_같은지_조회(FEED_REQUEST1, savedFeed1);
        피드_정보가_같은지_조회(FEED_REQUEST2, savedFeed2);
        피드_정보가_같은지_조회(FEED_REQUEST3, savedFeed3);
    }

    @DisplayName("Feed를 수정한다. (storageUrl, deployUrl, thumbnailUrl을 제외한 나머지만 수정)")
    @Test
    void updateNewTechs() {
        // given
        Long feedId1 = feedService.create(user1, FEED_REQUEST2);
        FeedRequest request = new FeedRequest(
                "수정된 제목",
                Collections.emptyList(),
                "수정된 내용",
                Step.COMPLETE.name(),
                false,
                FEED_REQUEST1.getStorageUrl(),
                FEED_REQUEST1.getDeployedUrl(),
                null
        );

        // when
        feedService.update(user1, feedId1, request);
        em.flush();
        em.clear();

        // then
        FeedResponse updateFeed = feedService.findById(user1, feedId1);
        피드_정보가_같은지_조회(request, updateFeed);
    }

    @DisplayName("Feed를 수정한다2. (storageUrl, deployUrl, thumbnailUrl을 제외한 나머지만 수정)")
    @Test
    void updateNewTechs2() {
        // given
        FEED_REQUEST2.setTechs(Arrays.asList(techSpring.getId(), techJava.getId()));
        Long feedId1 = feedService.create(user1, FEED_REQUEST2);
        FeedRequest request = new FeedRequest(
                "수정된 제목",
                Arrays.asList(techJava.getId(), techReact.getId()),
                "수정된 내용",
                Step.COMPLETE.name(),
                false,
                FEED_REQUEST1.getStorageUrl(),
                FEED_REQUEST1.getDeployedUrl(),
                null
        );
        FeedResponse saveFeed = feedService.findById(user1, feedId1);

        // when
        feedService.update(user1, feedId1, request);
        em.flush();
        em.clear();

        // then
        FeedResponse updateFeed = feedService.findById(user1, feedId1);
        피드_정보가_같은지_조회(request, updateFeed);
    }

    @DisplayName("작성자가 아닐 경우 Feed를 수정할 수 없다.")
    @Test
    void cantUpdateIfNotAuthor() {
        // given
        Long feedId1 = feedService.create(user1, FEED_REQUEST1);
        FeedRequest request = new FeedRequest(
                "수정된 제목",
                Arrays.asList(techSpring.getId(), techJava.getId()),
                "수정된 내용",
                Step.COMPLETE.name(),
                false,
                FEED_REQUEST1.getStorageUrl(),
                FEED_REQUEST1.getDeployedUrl(),
                null
        );

        // when
        // then
        assertThatThrownBy(() -> feedService.update(user2, feedId1, request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(ErrorType.UNAUTHORIZED_UPDATE_FEED.getMessage());
    }

    @DisplayName("Feed를 삭제한다.")
    @Test
    void delete() {
        // given
        FEED_REQUEST1.setTechs(Arrays.asList(techSpring.getId(), techJava.getId()));
        Long feedId = feedService.create(user1, FEED_REQUEST1);
        em.flush();
        em.clear();

        // when
        Feed feed = feedService.findEntityById(feedId);

        List<FeedTech> feedTechs = feed.getFeedTechs();
        feedService.delete(user1, feedId);
        em.flush();

        // then
        assertThatThrownBy(() -> feedService.findEntityById(feedId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorType.FEED_NOT_FOUND.getMessage());
    }

    @DisplayName("작성자가 아닐 경우 Feed를 삭제할 수 없다.")
    @Test
    void cantDeleteIfNotAuthor() {
        // given
        FEED_REQUEST1.setTechs(Arrays.asList(techSpring.getId(), techJava.getId()));
        Long feedId = feedService.create(user1, FEED_REQUEST1);
        em.flush();
        em.clear();

        // when
        Feed feed = feedService.findEntityById(feedId);
        List<FeedTech> feedTechs = feed.getFeedTechs();
//        feedTechs.forEach(feedTech -> feedTechRepository.delete(feedTech));
        em.flush();

        // then
        assertThatThrownBy(() -> feedService.delete(user2, feedId))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(ErrorType.UNAUTHORIZED_DELETE_FEED.getMessage());
    }


    @DisplayName("Feed를 Id로 단일 조회하고 조회수를 1 증가시킨다.")
    @Test
    void findById() {
        // given
        Long feedId1 = feedService.create(user1, FEED_REQUEST1);
        Long feedId2 = feedService.create(user2, FEED_REQUEST2);
        Long feedId3 = feedService.create(user2, FEED_REQUEST3);

        em.flush();
        em.clear();

        // when
        FeedResponse feedResponse1 = feedService.findById(user1, feedId1);
        FeedResponse feedResponse2 = feedService.findById(user2, feedId2);
        FeedResponse feedResponse3 = feedService.findById(user2, feedId3);
        FEED_REQUEST1.toEntityWithThumbnailUrl(null);

        // then
        assertThat(feedResponse1.getId()).isEqualTo(feedId1);
        피드_정보가_같은지_조회(FEED_REQUEST1, feedResponse1);
        assertThat(feedResponse1.getViews()).isEqualTo(1);

        assertThat(feedResponse2.getId()).isEqualTo(feedId2);
        피드_정보가_같은지_조회(FEED_REQUEST2, feedResponse2);
        assertThat(feedResponse2.getViews()).isEqualTo(1);

        assertThat(feedResponse3.getId()).isEqualTo(feedId3);
        피드_정보가_같은지_조회(FEED_REQUEST3, feedResponse3);
        assertThat(feedResponse3.getViews()).isEqualTo(1);
    }

    @DisplayName("존재하지 않는 Feed Id로 조회하면 예외가 발생한다.")
    @Test
    void findByNonExistsId() {
        // when then
        assertThatThrownBy(() -> feedService.findById(user1, Long.MAX_VALUE))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorType.FEED_NOT_FOUND.getMessage());
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
                .hasMessage(ErrorType.FEED_NOT_FOUND.getMessage());
    }


    @DisplayName("좋아요를 누른 피드 조회 시 'liked=true'로 반환한다.")
    @Test
    void checkLikeWhenFindFeed() {
        // given
        Long feedId1 = feedService.create(user1, FEED_REQUEST1);
        likeService.addLike(user2, feedId1);
        em.flush();
        em.clear();

        // when
        FeedResponse feedResponse = feedService.findById(user2, feedId1);

        // then
        assertThat(feedResponse.isLiked()).isTrue();
    }

    @DisplayName("좋아요를 누르지 않은 피드 조회 시 'liked=false'로 반환한다.")
    @Test
    void checkNotLikeWhenFindFeed() {
        // given
        Long feedId1 = feedService.create(user1, FEED_REQUEST1);
        em.flush();
        em.clear();

        // when
        FeedResponse feedResponse = feedService.findById(user2, feedId1);

        // then
        assertThat(feedResponse.isLiked()).isFalse();
    }

    @DisplayName("좋아요를 취소한 이후 피드를 조회하면 liked=false를 반환한다.")
    @Test
    void cancelLike() {
        // given
        Long feedId1 = feedService.create(user1, FEED_REQUEST1);
        likeService.addLike(user2, feedId1);
        em.flush();
        em.clear();

        // when
        likeService.deleteLike(user2, feedId1);
        em.flush();
        em.clear();
        FeedResponse feedResponse = feedService.findById(user2, feedId1);

        // then
        assertThat(feedResponse.isLiked()).isFalse();
    }

    @DisplayName("좋아요를 누른 유저가 삭제되면 해당 피드의 좋아요 데이터도 삭제된다. (user2가 삭제 -> feed1의 user2가 좋아요한 데이터 삭제) - user2가 없으므로 엔티티로 조회")
    @Test
    void cancelLikeWhenDeleteUser() {
        // given
        Long feedId1 = feedService.create(user1, FEED_REQUEST1);
        likeService.addLike(user2, feedId1);
        em.flush();
        em.clear();

        // when
        userRepository.delete(user2);
        em.flush();
        em.clear();
        Feed findFeed = feedService.findEntityById(feedId1);

        // then
        assertThat(findFeed.findLikeBy(user2)).isEqualTo(Optional.empty());
    }

    @DisplayName("좋아요를 누른 피드가 삭제되면 유저의 좋아요 데이터도 삭제된다. (feed1이 삭제 ->  user2의 feed1에 대한 like 데이터 삭제) - feed1이 없으므로 엔티티로 조회")
    @Test
    void cancelLikeWhenDeleteFeed() {
        // given
        Long feedId1 = feedService.create(user1, FEED_REQUEST1);
        likeService.addLike(user2, feedId1);
        em.flush();
        em.clear();

        // when
        Feed findFeed = feedService.findEntityById(feedId1);
        feedService.delete(user1, feedId1);
        em.flush();
        em.clear();

        // then
        user2 = userRepository.findById(this.user2.getId()).get();
        assertThat(user2.isLiked(findFeed)).isFalse();
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

    @DisplayName("좋아요 개수가 같은 경우 최신 Feed를 가져온다.(피드1: 좋아요0개, 피드2: 좋아요2개, 피드3: 좋아요2개)")
    @Test
    void findHotFeedsBySameDate() {
        // given
        Long firstFeedId = feedService.create(user1, FEED_REQUEST1);
        Long secondFeedId = feedService.create(user1, FEED_REQUEST2);
        Long thirdFeedId = feedService.create(user1, FEED_REQUEST3);

        likeService.addLike(user2, secondFeedId);
        likeService.addLike(user2, thirdFeedId);
        likeService.addLike(user1, secondFeedId);
        likeService.addLike(user1, thirdFeedId);
        em.flush();
        em.clear();

        // when
        List<FeedCardResponse> hotFeeds = feedService.findHotFeeds();

        // then
        assertThat(hotFeeds).hasSize(3);
        피드_정보가_같은지_조회(FEED_REQUEST3, hotFeeds.get(0));
        피드_정보가_같은지_조회(FEED_REQUEST2, hotFeeds.get(1));
        피드_정보가_같은지_조회(FEED_REQUEST1, hotFeeds.get(2));
    }

    @DisplayName("모든 피드를 조회한다. (최신 등록된 날짜 순)")
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

    @DisplayName("SOS 피드를 조회한다. (최신 등록된 날짜 순)")
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

    @DisplayName("PROGRESS 피드를 조회한다. (최신 등록된 날짜 순)")
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

    @DisplayName("COMPLETE 피드를 조회한다. (최신 등록된 날짜 순)")
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

    @DisplayName("제목과 내용에 특정 query('content')가 포함된 피드를 검색한다.")
    @Test
    void searchByQuery_content() {
        //given
        Long firstFeedId = feedService.create(user1, FEED_REQUEST1);
        Long secondFeedId = feedService.create(user1, FEED_REQUEST2);
        Long thirdFeedId = feedService.create(user1, FEED_REQUEST3);
        System.out.println("-------------------");
        System.out.println(firstFeedId);
        System.out.println(secondFeedId);
        System.out.println(thirdFeedId);
        em.flush();
        em.clear();
        String query = "content";
        List<Feed> feeds = feedRepository.findAll();
        System.out.println("1++++++++++++++++++++++++");
        System.out.println(feeds);

        //when
        List<FeedCardResponse> searchFeeds = feedService.search(query, "", "all");
        List<Feed> feeds2 = feedRepository.findAll();
        System.out.println("2++++++++++++++++++++++++");
        System.out.println(feeds2);
        System.out.println("3++++++++++++++++");
        System.out.println("searchFeeds: " + searchFeeds);
        List<Long> feedIds = searchFeeds.stream()
                .map(FeedCardResponse::getId)
                .collect(Collectors.toList());

        //then
        assertThat(searchFeeds).hasSize(3);
        assertThat(feedIds).contains(firstFeedId, secondFeedId, thirdFeedId);
    }

    @DisplayName("제목과 내용에 특정 query('tle')가 포함된 피드를 검색한다.")
    @Test
    void searchByQuery_tle1() {
        //given
        Long firstFeedId = feedService.create(user1, FEED_REQUEST1);
        Long secondFeedId = feedService.create(user1, FEED_REQUEST2);
        Long thirdFeedId = feedService.create(user1, FEED_REQUEST3);
        em.flush();
        em.clear();

        String query = "tle1";

        //when
        List<FeedCardResponse> searchFeeds = feedService.search(query, "", "all");
        List<Long> feedIds = searchFeeds.stream()
                .map(FeedCardResponse::getId)
                .collect(Collectors.toList());

        //then
        assertThat(searchFeeds).hasSize(1);
        assertThat(feedIds).contains(firstFeedId);
    }

    @DisplayName("테크 이름들의 나열을 통해, 해당 테크를 사용한 피드를 검색한다.")
    @Test
    void searchByTechs() {
        //given
        FEED_REQUEST1.setTechs(Arrays.asList(techSpring.getId(), techJava.getId()));
        Long firstFeedId = feedService.create(user1, FEED_REQUEST1);

        FEED_REQUEST2.setTechs(Collections.singletonList(techJava.getId()));
        Long secondFeedId = feedService.create(user1, FEED_REQUEST2);

        FEED_REQUEST3.setTechs(Collections.singletonList(techReact.getId()));
        Long thirdFeedId = feedService.create(user1, FEED_REQUEST3);

        em.flush();
        em.clear();

        String techs = "Spring,Java";

        //when
        List<FeedCardResponse> searchFeeds = feedService.search("", techs, "all");
        List<Long> feedIds = searchFeeds.stream()
                .map(FeedCardResponse::getId)
                .collect(Collectors.toList());

        //then
        assertThat(searchFeeds).hasSize(1);
        assertThat(feedIds).contains(firstFeedId);
    }


    @DisplayName("테크 이름들을 사용한 피드들이 없다면 빈 리스트를 반환한다.")
    @Test
    void searchByTechsWithInvalidName() {
        //given
        FEED_REQUEST1.setTechs(Arrays.asList(techSpring.getId(), techJava.getId()));
        Long firstFeedId = feedService.create(user1, FEED_REQUEST1);

        FEED_REQUEST2.setTechs(Collections.singletonList(techJava.getId()));
        Long secondFeedId = feedService.create(user1, FEED_REQUEST2);

        FEED_REQUEST3.setTechs(Collections.singletonList(techReact.getId()));
        Long thirdFeedId = feedService.create(user1, FEED_REQUEST3);

        String techs = "KOLOLO";

        //when
        List<FeedCardResponse> searchFeeds = feedService.search("", techs, "all");

        //then
        assertThat(searchFeeds).hasSize(0);
    }

    @DisplayName("제목과 내용에 특정 query가 포함되어 있고, 검색한 테그명에 포함된 테크를 사용한 피드를 검색한다.")
    @Test
    void searchByQueryAndTechs() {
        //given
        FEED_REQUEST1.setTechs(Arrays.asList(techSpring.getId(), techJava.getId()));
        Long firstFeedId = feedService.create(user1, FEED_REQUEST1);

        FEED_REQUEST2.setTechs(Collections.singletonList(techJava.getId()));
        Long secondFeedId = feedService.create(user1, FEED_REQUEST2);

        FEED_REQUEST3.setTechs(Collections.singletonList(techReact.getId()));
        Long thirdFeedId = feedService.create(user1, FEED_REQUEST3);
        em.flush();
        em.clear();

        String query = "title";
        String techs = "Spring,Java";

        //when
        List<FeedCardResponse> searchFeeds = feedService.search(query, techs, "all");
        List<Long> feedIds = searchFeeds.stream()
                .map(FeedCardResponse::getId)
                .collect(Collectors.toList());

        //then
        assertThat(searchFeeds).hasSize(1);
        assertThat(feedIds).contains(firstFeedId);
    }

    @DisplayName("제목+내용 Query와 기술명 나열 Techs로 검색한 결과에 대해 SOS 필터링하여 받을 수 있다.")
    @Test
    void searchByQueryAndTechsWithSos() {
        //given
        FEED_REQUEST1.setTechs(Arrays.asList(techSpring.getId(), techJava.getId()));
        FEED_REQUEST1.setSos(true);
        Long firstFeedId = feedService.create(user1, FEED_REQUEST1);

        FEED_REQUEST2.setTechs(Arrays.asList(techSpring.getId(), techJava.getId()));
        FEED_REQUEST2.setSos(false);
        Long secondFeedId = feedService.create(user1, FEED_REQUEST2);

        FEED_REQUEST3.setTechs(Collections.singletonList(techReact.getId()));
        FEED_REQUEST3.setSos(false);
        Long thirdFeedId = feedService.create(user1, FEED_REQUEST3);
        em.flush();
        em.clear();

        String query = "title";
        String techs = "Spring,Java";

        //when
        List<FeedCardResponse> searchFeeds = feedService.search(query, techs, "sos");
        List<Long> feedIds = searchFeeds.stream()
                .map(FeedCardResponse::getId)
                .collect(Collectors.toList());

        //then
        assertThat(searchFeeds).hasSize(1);
        assertThat(feedIds).contains(firstFeedId);
    }

    @DisplayName("제목+내용 Query와 기술명 나열 Techs로 검색한 결과에 대해 PROGRESS만 필터링하여 받을 수 있다.")
    @Test
    void searchByQueryAndTechsWithProgress() {
        //given
        FEED_REQUEST1.setTechs(Arrays.asList(techSpring.getId(), techJava.getId()));
        FEED_REQUEST1.setStep("PROGRESS");
        Long firstFeedId = feedService.create(user1, FEED_REQUEST1);

        FEED_REQUEST2.setTechs(Arrays.asList(techSpring.getId(), techJava.getId()));
        FEED_REQUEST2.setStep("PROGRESS");
        Long secondFeedId = feedService.create(user1, FEED_REQUEST2);

        FEED_REQUEST3.setTechs(Arrays.asList(techSpring.getId(), techJava.getId()));
        FEED_REQUEST3.setStep("COMPLETE");
        Long thirdFeedId = feedService.create(user1, FEED_REQUEST3);
        em.flush();
        em.clear();

        String query = "title";
        String techs = "Spring,Java";

        //when
        List<FeedCardResponse> searchFeeds = feedService.search(query, techs, "progress");
        List<Long> feedIds = searchFeeds.stream()
                .map(FeedCardResponse::getId)
                .collect(Collectors.toList());

        //then
        assertThat(searchFeeds).hasSize(2);
        assertThat(feedIds).contains(firstFeedId, secondFeedId);
    }


    @DisplayName("제목+내용 Query와 기술명 나열 Techs로 검색한 결과에 대해 COMPLETE만 필터링하여 받을 수 있다.")
    @Test
    void searchByQueryAndTechsWithComplete() {
        //given
        FEED_REQUEST1.setTechs(Arrays.asList(techSpring.getId(), techJava.getId()));
        FEED_REQUEST1.setStep("COMPLETE");
        Long firstFeedId = feedService.create(user1, FEED_REQUEST1);

        FEED_REQUEST2.setTechs(Arrays.asList(techSpring.getId(), techJava.getId()));
        FEED_REQUEST2.setStep("PROGRESS");
        Long secondFeedId = feedService.create(user1, FEED_REQUEST2);

        FEED_REQUEST3.setTechs(Arrays.asList(techSpring.getId(), techJava.getId()));
        FEED_REQUEST3.setStep("COMPLETE");
        Long thirdFeedId = feedService.create(user1, FEED_REQUEST3);
        em.flush();
        em.clear();

        String query = "title";
        String techs = "Spring,Java";

        //when
        List<FeedCardResponse> searchFeeds = feedService.search(query, techs, "complete");
        List<Long> feedIds = searchFeeds.stream()
                .map(FeedCardResponse::getId)
                .collect(Collectors.toList());

        //then
        assertThat(searchFeeds).hasSize(2);
        assertThat(feedIds).contains(firstFeedId, thirdFeedId);
    }

    private void 피드_정보가_같은지_조회(FeedRequest request, Feed feed) {
        List<Long> techIds = feed.getTechs().stream()
                .map(Tech::getId)
                .collect(Collectors.toList());

        assertThat(request.getTitle()).isEqualTo(feed.getTitle());
        assertThat(request.getTechs()).containsExactlyElementsOf(techIds);
        assertThat(request.getContent()).isEqualTo(feed.getContent());
        assertThat(request.getStep()).isEqualTo(feed.getStep().name());
        assertThat(request.isSos()).isEqualTo(feed.isSos());
        assertThat(request.getStorageUrl()).isEqualTo(feed.getStorageUrl());
        assertThat(request.getDeployedUrl()).isEqualTo(feed.getDeployedUrl());
    }

    private void 피드_정보가_같은지_조회(FeedRequest request, FeedResponse response) {
        List<Long> techIds = response.getTechs().stream()
                .map(TechResponse::getId)
                .collect(Collectors.toList());

        assertThat(request.getTitle()).isEqualTo(response.getTitle());
        assertThat(request.getTechs()).containsExactlyElementsOf(techIds);
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

        System.out.println();
        System.out.println();
    }
}