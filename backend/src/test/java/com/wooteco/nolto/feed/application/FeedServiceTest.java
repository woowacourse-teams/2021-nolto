package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.admin.ui.dto.CommentsByFeedResponse;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.NotFoundException;
import com.wooteco.nolto.exception.UnauthorizedException;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.repository.CommentRepository;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.feed.ui.dto.*;
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

import static com.wooteco.nolto.FeedFixture.진행중_단계의_피드_생성;
import static com.wooteco.nolto.TechFixture.*;
import static com.wooteco.nolto.UserFixture.조엘_생성;
import static com.wooteco.nolto.UserFixture.찰리_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class FeedServiceTest {
    private FeedRequest EMPTY_TECH_FEED_REQUEST = new FeedRequest("title1", new ArrayList<>(), "content1", "PROGRESS", true,
            "www.github.com/woowacourse", "www.github.com/woowacourse", null);
    private FeedRequest SPRING_JAVA_FEED_REQUEST = new FeedRequest("title2", new ArrayList<>(), "content2", "PROGRESS", true,
            "www.github.com/woowacourse", "www.github.com/woowacourse", null);
    private FeedRequest REACT_FEED_REQUEST = new FeedRequest("title3", new ArrayList<>(), "content3", "PROGRESS", true,
            "www.github.com/woowacourse", "www.github.com/woowacourse", null);
    private FeedRequest SPRING_REACT_FEED_REQUEST = new FeedRequest("title3", new ArrayList<>(), "content3", "PROGRESS", true,
            "www.github.com/woowacourse", "www.github.com/woowacourse", null);

    private User 찰리 = 찰리_생성();
    private User 조엘 = 조엘_생성();

    private Tech 자바 = 자바_생성();
    private Tech 스프링 = 스프링_생성();
    private Tech 리액트 = 리액트_생성();

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
        userRepository.save(찰리);
        userRepository.save(조엘);

        techRepository.save(스프링);
        techRepository.save(자바);
        techRepository.save(리액트);

        SPRING_JAVA_FEED_REQUEST.setTechs(Arrays.asList(스프링.getId(), 자바.getId()));
        REACT_FEED_REQUEST.setTechs(Collections.singletonList(리액트.getId()));
        SPRING_REACT_FEED_REQUEST.setTechs(Arrays.asList(스프링.getId(), 리액트.getId()));
    }

    @DisplayName("유저가 feed를 작성한다.")
    @Test
    void create() {
        // when
        EMPTY_TECH_FEED_REQUEST.setTechs(Arrays.asList(스프링.getId()));
        Long feedId1 = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);
        Long feedId2 = feedService.create(찰리, SPRING_JAVA_FEED_REQUEST);
        Long feedId3 = feedService.create(조엘, REACT_FEED_REQUEST);

        em.flush();
        em.clear();

        Feed savedFeed1 = feedService.findEntityById(feedId1);
        Feed savedFeed2 = feedService.findEntityById(feedId2);
        Feed savedFeed3 = feedService.findEntityById(feedId3);

        // then
        assertThat(feedId1).isNotNull();
        assertThat(feedId2).isNotNull();
        assertThat(feedId3).isNotNull();
        피드_정보가_같은지_조회(EMPTY_TECH_FEED_REQUEST, savedFeed1);
        피드_정보가_같은지_조회(SPRING_JAVA_FEED_REQUEST, savedFeed2);
        피드_정보가_같은지_조회(REACT_FEED_REQUEST, savedFeed3);
    }

    @DisplayName("Feed를 수정한다 - 2개의 tech에서 모든 tech 제거")
    @Test
    void updateNewTechs() {
        // given
        Long feedId1 = feedService.create(찰리, SPRING_JAVA_FEED_REQUEST);
        FeedRequest request = EMPTY_TECH_FEED_REQUEST;

        // when
        feedService.update(찰리, feedId1, request);
        em.flush();
        em.clear();

        // then
        FeedResponse updateFeed = feedService.viewFeed(찰리, feedId1, true);
        피드_정보가_같은지_조회(request, updateFeed);
    }

    @DisplayName("Feed를 수정한다2 - tech들 중 하나만 변경")
    @Test
    void updateNewTechs2() {
        // given
        Long feedId1 = feedService.create(찰리, SPRING_JAVA_FEED_REQUEST);
        FeedRequest request = SPRING_REACT_FEED_REQUEST;
        feedService.viewFeed(찰리, feedId1, true);

        // when
        feedService.update(찰리, feedId1, request);
        em.flush();
        em.clear();

        // then
        FeedResponse updateFeed = feedService.viewFeed(찰리, feedId1, true);
        피드_정보가_같은지_조회(request, updateFeed);
    }

    @DisplayName("작성자가 아닐 경우 Feed를 수정할 수 없다.")
    @Test
    void cantUpdateIfNotAuthor() {
        // given
        Long feedId1 = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);
        FeedRequest request = SPRING_JAVA_FEED_REQUEST;

        // when
        // then
        assertThatThrownBy(() -> feedService.update(조엘, feedId1, request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(ErrorType.UNAUTHORIZED_UPDATE_FEED.getMessage());
    }

    @DisplayName("Feed를 삭제한다.")
    @Test
    void delete() {
        // given
        EMPTY_TECH_FEED_REQUEST.setTechs(Arrays.asList(스프링.getId(), 자바.getId()));
        Long feedId = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);
        em.flush();
        em.clear();

        // when
        Feed feed = feedService.findEntityById(feedId);

        feedService.delete(찰리, feedId);
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
        EMPTY_TECH_FEED_REQUEST.setTechs(Arrays.asList(스프링.getId(), 자바.getId()));
        Long feedId = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);
        em.flush();
        em.clear();

        // when
        Feed feed = feedService.findEntityById(feedId);
        em.flush();

        // then
        assertThatThrownBy(() -> feedService.delete(조엘, feedId))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(ErrorType.UNAUTHORIZED_DELETE_FEED.getMessage());
    }


    @DisplayName("Feed를 Id로 단일 조회하고 조회수를 1 증가시킨다.")
    @Test
    void findById() {
        // given
        Long feedId1 = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);
        Long feedId2 = feedService.create(조엘, SPRING_JAVA_FEED_REQUEST);
        Long feedId3 = feedService.create(조엘, REACT_FEED_REQUEST);

        em.flush();
        em.clear();

        // when
        FeedResponse feedResponse1 = feedService.viewFeed(찰리, feedId1, false);
        FeedResponse feedResponse2 = feedService.viewFeed(조엘, feedId2, false);
        FeedResponse feedResponse3 = feedService.viewFeed(조엘, feedId3, false);
        EMPTY_TECH_FEED_REQUEST.toEntityWithThumbnailUrl(null);

        // then
        assertThat(feedResponse1.getId()).isEqualTo(feedId1);
        피드_정보가_같은지_조회(EMPTY_TECH_FEED_REQUEST, feedResponse1);
        assertThat(feedResponse1.getViews()).isEqualTo(1);

        assertThat(feedResponse2.getId()).isEqualTo(feedId2);
        피드_정보가_같은지_조회(SPRING_JAVA_FEED_REQUEST, feedResponse2);
        assertThat(feedResponse2.getViews()).isEqualTo(1);

        assertThat(feedResponse3.getId()).isEqualTo(feedId3);
        피드_정보가_같은지_조회(REACT_FEED_REQUEST, feedResponse3);
        assertThat(feedResponse3.getViews()).isEqualTo(1);
    }

    @DisplayName("존재하지 않는 Feed Id로 조회하면 예외가 발생한다.")
    @Test
    void findByNonExistsId() {
        // when then
        assertThatThrownBy(() -> feedService.viewFeed(찰리, Long.MAX_VALUE, true))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorType.FEED_NOT_FOUND.getMessage());
    }

    @DisplayName("Feed Id로 entity 객체를 가져올 수 있다.")
    @Test
    void findEntityById() {
        // given
        Long feedId = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);

        // when
        Feed feedEntity = feedService.findEntityById(feedId);

        // then
        assertThat(feedEntity.getId()).isEqualTo(feedId);
        피드_정보가_같은지_조회(EMPTY_TECH_FEED_REQUEST, feedEntity);
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
        Long feedId1 = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);
        likeService.addLike(조엘, feedId1);
        em.flush();
        em.clear();

        // when
        FeedResponse feedResponse = feedService.viewFeed(조엘, feedId1, true);

        // then
        assertThat(feedResponse.isLiked()).isTrue();
    }

    @DisplayName("좋아요를 누르지 않은 피드 조회 시 'liked=false'로 반환한다.")
    @Test
    void checkNotLikeWhenFindFeed() {
        // given
        Long feedId1 = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);
        em.flush();
        em.clear();

        // when
        FeedResponse feedResponse = feedService.viewFeed(조엘, feedId1, true);

        // then
        assertThat(feedResponse.isLiked()).isFalse();
    }

    @DisplayName("좋아요를 취소한 이후 피드를 조회하면 liked=false를 반환한다.")
    @Test
    void cancelLike() {
        // given
        Long feedId1 = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);
        likeService.addLike(조엘, feedId1);
        em.flush();
        em.clear();

        // when
        likeService.deleteLike(조엘, feedId1);
        em.flush();
        em.clear();
        FeedResponse feedResponse = feedService.viewFeed(조엘, feedId1, true);

        // then
        assertThat(feedResponse.isLiked()).isFalse();
    }

    @DisplayName("좋아요를 누른 유저가 삭제되면 해당 피드의 좋아요 데이터도 삭제된다. (user2가 삭제 -> feed1의 user2가 좋아요한 데이터 삭제) - user2가 없으므로 엔티티로 조회")
    @Test
    void cancelLikeWhenDeleteUser() {
        // given
        Long feedId1 = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);
        likeService.addLike(조엘, feedId1);
        em.flush();
        em.clear();

        // when
        userRepository.delete(조엘);
        em.flush();
        em.clear();
        Feed findFeed = feedService.findEntityById(feedId1);

        // then
        assertThat(findFeed.findLikeBy(조엘)).isEmpty();
    }

    @DisplayName("좋아요를 누른 피드가 삭제되면 유저의 좋아요 데이터도 삭제된다. (feed1이 삭제 ->  user2의 feed1에 대한 like 데이터 삭제) - feed1이 없으므로 엔티티로 조회")
    @Test
    void cancelLikeWhenDeleteFeed() {
        // given
        Long feedId1 = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);
        likeService.addLike(조엘, feedId1);
        em.flush();
        em.clear();

        // when
        Feed findFeed = feedService.findEntityById(feedId1);
        feedService.delete(찰리, feedId1);
        em.flush();
        em.clear();

        // then
        조엘 = userRepository.findById(this.조엘.getId()).get();
        assertThat(조엘.isLiked(findFeed)).isFalse();
    }

    @DisplayName("좋아요 개수가 높은 인기 Feed들을 가져온다.")
    @Test
    void findHotFeeds() {
        // given
        Long firstFeedId = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);
        Long secondFeedId = feedService.create(찰리, SPRING_JAVA_FEED_REQUEST);
        Long thirdFeedId = feedService.create(찰리, REACT_FEED_REQUEST);

        likeService.addLike(조엘, secondFeedId);
        likeService.addLike(조엘, thirdFeedId);
        likeService.addLike(찰리, secondFeedId);
        em.flush();
        em.clear();

        // when
        List<FeedCardResponse> hotFeeds = feedService.findHotFeeds();

        // then
        assertThat(hotFeeds).hasSize(3);
        피드_정보가_같은지_조회(SPRING_JAVA_FEED_REQUEST, hotFeeds.get(0));
        피드_정보가_같은지_조회(REACT_FEED_REQUEST, hotFeeds.get(1));
        피드_정보가_같은지_조회(EMPTY_TECH_FEED_REQUEST, hotFeeds.get(2));
    }

    @DisplayName("좋아요 개수가 같은 경우 최신 Feed를 가져온다.(피드1: 좋아요0개, 피드2: 좋아요2개, 피드3: 좋아요2개)")
    @Test
    void findHotFeedsBySameDate() throws InterruptedException {
        // given
        Long firstFeedId = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);
        Long secondFeedId = feedService.create(찰리, SPRING_JAVA_FEED_REQUEST);
        Long thirdFeedId = feedService.create(찰리, REACT_FEED_REQUEST);

        likeService.addLike(조엘, secondFeedId);
        likeService.addLike(조엘, thirdFeedId);
        likeService.addLike(찰리, secondFeedId);
        likeService.addLike(찰리, thirdFeedId);
        em.flush();
        em.clear();

        // when
        List<FeedCardResponse> hotFeeds = feedService.findHotFeeds();

        // then
        assertThat(hotFeeds).hasSize(3);
        피드_정보가_같은지_조회(REACT_FEED_REQUEST, hotFeeds.get(0));
        피드_정보가_같은지_조회(SPRING_JAVA_FEED_REQUEST, hotFeeds.get(1));
        피드_정보가_같은지_조회(EMPTY_TECH_FEED_REQUEST, hotFeeds.get(2));
    }

    @DisplayName("제목과 내용에 특정 query('content')가 포함된 피드를 검색한다. [step=상관 없음, help=상관 없음, nextFeedId=10000L, countPerPage=15]")
    @Test
    void searchByQuery_content() {
        //given
        Long firstFeedId = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);
        Long secondFeedId = feedService.create(찰리, SPRING_JAVA_FEED_REQUEST);
        Long thirdFeedId = feedService.create(찰리, REACT_FEED_REQUEST);
        em.flush();
        em.clear();
        String query = "content";

        //when
        FeedCardPaginationResponse searchFeeds = feedService.search(query, "", "all", false, 10000L, 15);
        List<Long> feedIds = searchFeeds.getFeeds()
                .stream()
                .map(FeedCardResponse::getId)
                .collect(Collectors.toList());

        //then
        assertThat(searchFeeds.getFeeds()).hasSize(3);
        assertThat(feedIds).contains(firstFeedId, secondFeedId, thirdFeedId);
        assertThat(searchFeeds.getNextFeedId()).isNull();
    }

    @DisplayName("제목과 내용에 특정 query('tle')가 포함된 피드를 검색한다. [step=상관 없음, help=상관 없음, nextFeedId=10000L, countPerPage=15]")
    @Test
    void searchByQuery_tle1() {
        //given
        Long firstFeedId = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);
        Long secondFeedId = feedService.create(찰리, SPRING_JAVA_FEED_REQUEST);
        Long thirdFeedId = feedService.create(찰리, REACT_FEED_REQUEST);
        em.flush();
        em.clear();

        String query = "tle1";

        //when
        FeedCardPaginationResponse searchFeeds = feedService.search(query, "", "all", false, 10000L, 15);
        List<Long> feedIds = searchFeeds.getFeeds()
                .stream()
                .map(FeedCardResponse::getId)
                .collect(Collectors.toList());

        //then
        assertThat(searchFeeds.getFeeds()).hasSize(1);
        assertThat(feedIds).contains(firstFeedId);
        assertThat(searchFeeds.getNextFeedId()).isNull();
    }

    @DisplayName("테크 이름들의 나열을 통해, 해당 테크들 중 하나라도 사용한 피드를 검색한다. [step=상관 없음, help=상관 없음, nextFeedId=10000L, countPerPage=15]")
    @Test
    void searchByTechs() {
        //given
        EMPTY_TECH_FEED_REQUEST.setTechs(Arrays.asList(스프링.getId(), 자바.getId()));
        Long firstFeedId = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);

        SPRING_JAVA_FEED_REQUEST.setTechs(Collections.singletonList(자바.getId()));
        Long secondFeedId = feedService.create(찰리, SPRING_JAVA_FEED_REQUEST);

        REACT_FEED_REQUEST.setTechs(Collections.singletonList(리액트.getId()));
        Long thirdFeedId = feedService.create(찰리, REACT_FEED_REQUEST);

        em.flush();
        em.clear();

        String techs = "Spring,Java";

        //when
        FeedCardPaginationResponse searchFeeds = feedService.search("", techs, "all", false, 10000L, 15);
        List<Long> feedIds = searchFeeds.getFeeds()
                .stream()
                .map(FeedCardResponse::getId)
                .collect(Collectors.toList());

        //then
        assertThat(searchFeeds.getFeeds()).hasSize(2);
        assertThat(feedIds).contains(firstFeedId, secondFeedId);
        assertThat(searchFeeds.getNextFeedId()).isNull();
    }


    @DisplayName("테크 이름들을 사용한 피드들이 없다면 빈 리스트를 반환한다. [step=상관 없음, help=상관 없음, nextFeedId=10000L, countPerPage=15]")
    @Test
    void searchByTechsWithInvalidName() {
        //given
        EMPTY_TECH_FEED_REQUEST.setTechs(Arrays.asList(스프링.getId(), 자바.getId()));
        Long firstFeedId = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);

        SPRING_JAVA_FEED_REQUEST.setTechs(Collections.singletonList(자바.getId()));
        Long secondFeedId = feedService.create(찰리, SPRING_JAVA_FEED_REQUEST);

        REACT_FEED_REQUEST.setTechs(Collections.singletonList(리액트.getId()));
        Long thirdFeedId = feedService.create(찰리, REACT_FEED_REQUEST);

        String techs = "KOLOLO";

        //when
        FeedCardPaginationResponse searchFeeds = feedService.search("", techs, "all", false, 10000L, 15);

        //then
        assertThat(searchFeeds.getFeeds()).isEmpty();
        assertThat(searchFeeds.getNextFeedId()).isNull();
    }

    @DisplayName("제목과 내용에 특정 query가 포함되어 있고, 검색한 테그명에 포함된 테크들 중 하나라도 사용한 피드를 검색한다. [step=상관 없음, help=상관 없음, nextFeedId=10000L, countPerPage=15]")
    @Test
    void searchByQueryAndTechs() {
        //given
        EMPTY_TECH_FEED_REQUEST.setTechs(Arrays.asList(스프링.getId(), 자바.getId()));
        Long firstFeedId = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);

        SPRING_JAVA_FEED_REQUEST.setTechs(Collections.singletonList(자바.getId()));
        Long secondFeedId = feedService.create(찰리, SPRING_JAVA_FEED_REQUEST);

        REACT_FEED_REQUEST.setTechs(Collections.singletonList(리액트.getId()));
        Long thirdFeedId = feedService.create(찰리, REACT_FEED_REQUEST);
        em.flush();
        em.clear();

        String query = "title";
        String techs = "Spring,Java";

        //when
        FeedCardPaginationResponse searchFeeds = feedService.search(query, techs, "all", false, 10000L, 15);
        List<Long> feedIds = searchFeeds.getFeeds()
                .stream()
                .map(FeedCardResponse::getId)
                .collect(Collectors.toList());

        //then
        assertThat(searchFeeds.getFeeds()).hasSize(2);
        assertThat(feedIds).contains(firstFeedId, secondFeedId);
        assertThat(searchFeeds.getNextFeedId()).isNull();
    }

    @DisplayName("제목+내용 Query와 기술명 나열 Techs로 검색한 결과에 대해 SOS 필터링하여 받을 수 있다. [step=상관 없음, help=true, nextFeedId=10000L, countPerPage=15]")
    @Test
    void searchByQueryAndTechsWithSos() {
        //given
        EMPTY_TECH_FEED_REQUEST.setTechs(Arrays.asList(스프링.getId(), 자바.getId()));
        EMPTY_TECH_FEED_REQUEST.setSos(true);
        Long firstFeedId = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);

        SPRING_JAVA_FEED_REQUEST.setTechs(Arrays.asList(스프링.getId(), 자바.getId()));
        SPRING_JAVA_FEED_REQUEST.setSos(false);
        Long secondFeedId = feedService.create(찰리, SPRING_JAVA_FEED_REQUEST);

        REACT_FEED_REQUEST.setTechs(Collections.singletonList(리액트.getId()));
        REACT_FEED_REQUEST.setSos(false);
        Long thirdFeedId = feedService.create(찰리, REACT_FEED_REQUEST);
        em.flush();
        em.clear();

        String query = "title";
        String techs = "Spring,Java";

        //when
        FeedCardPaginationResponse searchFeeds = feedService.search(query, techs, "all", true, 10000L, 15);
        List<Long> feedIds = searchFeeds.getFeeds()
                .stream()
                .map(FeedCardResponse::getId)
                .collect(Collectors.toList());

        //then
        assertThat(searchFeeds.getFeeds()).hasSize(1);
        assertThat(feedIds).contains(firstFeedId);
        assertThat(searchFeeds.getNextFeedId()).isNull();
    }

    @DisplayName("제목+내용 Query와 기술명 나열 Techs로 검색한 결과에 대해 PROGRESS만 필터링하여 받을 수 있다. [step=PROGRESS, help=상관 없음, nextFeedId=10000L, countPerPage=15]")
    @Test
    void searchByQueryAndTechsWithProgress() {
        //given
        EMPTY_TECH_FEED_REQUEST.setTechs(Arrays.asList(스프링.getId(), 자바.getId()));
        EMPTY_TECH_FEED_REQUEST.setStep("PROGRESS");
        Long firstFeedId = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);

        SPRING_JAVA_FEED_REQUEST.setTechs(Arrays.asList(스프링.getId(), 자바.getId()));
        SPRING_JAVA_FEED_REQUEST.setStep("PROGRESS");
        Long secondFeedId = feedService.create(찰리, SPRING_JAVA_FEED_REQUEST);

        REACT_FEED_REQUEST.setTechs(Arrays.asList(스프링.getId(), 자바.getId()));
        REACT_FEED_REQUEST.setStep("COMPLETE");
        Long thirdFeedId = feedService.create(찰리, REACT_FEED_REQUEST);
        em.flush();
        em.clear();

        String query = "title";
        String techs = "Spring,Java";

        //when
        FeedCardPaginationResponse searchFeeds = feedService.search(query, techs, "progress", true, 10000L, 15);
        List<Long> feedIds = searchFeeds.getFeeds()
                .stream()
                .map(FeedCardResponse::getId)
                .collect(Collectors.toList());

        //then
        assertThat(searchFeeds.getFeeds()).hasSize(2);
        assertThat(feedIds).contains(firstFeedId, secondFeedId);
        assertThat(searchFeeds.getNextFeedId()).isNull();
    }


    @DisplayName("제목+내용 Query와 기술명 나열 Techs로 검색한 결과에 대해 COMPLETE만 필터링하여 받을 수 있다. [step=COMPLETE, help=상관 없음, nextFeedId=10000L, countPerPage=15]")
    @Test
    void searchByQueryAndTechsWithComplete() {
        //given
        EMPTY_TECH_FEED_REQUEST.setTechs(Arrays.asList(스프링.getId(), 자바.getId()));
        EMPTY_TECH_FEED_REQUEST.setStep("COMPLETE");
        Long firstFeedId = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);

        SPRING_JAVA_FEED_REQUEST.setTechs(Arrays.asList(스프링.getId(), 자바.getId()));
        SPRING_JAVA_FEED_REQUEST.setStep("PROGRESS");
        Long secondFeedId = feedService.create(찰리, SPRING_JAVA_FEED_REQUEST);

        REACT_FEED_REQUEST.setTechs(Arrays.asList(스프링.getId(), 자바.getId()));
        REACT_FEED_REQUEST.setStep("COMPLETE");
        Long thirdFeedId = feedService.create(찰리, REACT_FEED_REQUEST);
        em.flush();
        em.clear();

        String query = "title";
        String techs = "Spring,Java";

        //when
        FeedCardPaginationResponse searchFeeds = feedService.search(query, techs, "complete", true, 10000L, 15);
        List<Long> feedIds = searchFeeds.getFeeds()
                .stream()
                .map(FeedCardResponse::getId)
                .collect(Collectors.toList());

        //then
        assertThat(searchFeeds.getFeeds()).hasSize(2);
        assertThat(feedIds).contains(firstFeedId, thirdFeedId);
        assertThat(searchFeeds.getNextFeedId()).isNull();
    }

    @DisplayName("step과 help를 고려한 페이지네이션을 지원한다. (step=all, help=null, lastFeedId=Long.MAX_VALUE countPerPage=2")
    @Test
    void stepAllHelpNull() {
        // given
        EMPTY_TECH_FEED_REQUEST.setStep("PROGRESS");
        Long firstFeedId = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);
        SPRING_JAVA_FEED_REQUEST.setStep("PROGRESS");
        Long secondFeedId = feedService.create(찰리, SPRING_JAVA_FEED_REQUEST);
        REACT_FEED_REQUEST.setStep("COMPLETE");
        Long thirdFeedId = feedService.create(찰리, REACT_FEED_REQUEST);
        em.flush();
        em.clear();

        // when
        FeedCardPaginationResponse responses = feedService.findRecentFeeds("all", false, Long.MAX_VALUE, 2);

        // then
        assertThat(responses.getFeeds()).hasSize(2);
        assertThat(responses.getFeeds().get(0).getTitle()).isEqualTo(REACT_FEED_REQUEST.getTitle());
        assertThat(responses.getFeeds().get(1).getTitle()).isEqualTo(SPRING_JAVA_FEED_REQUEST.getTitle());
        assertThat(responses.getNextFeedId()).isEqualTo(firstFeedId);
    }

    @DisplayName("step과 help를 고려한 페이지네이션을 지원한다. (step=progress, help=null, lastFeedId=Long.MAX_VALUE countPerPage=2")
    @Test
    void stepProgressHelpNull() {
        // given
        EMPTY_TECH_FEED_REQUEST.setStep("PROGRESS");
        Long firstFeedId = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);
        SPRING_JAVA_FEED_REQUEST.setStep("PROGRESS");
        Long secondFeedId = feedService.create(찰리, SPRING_JAVA_FEED_REQUEST);
        REACT_FEED_REQUEST.setStep("COMPLETE");
        Long thirdFeedId = feedService.create(찰리, REACT_FEED_REQUEST);
        em.flush();
        em.clear();

        // when
        FeedCardPaginationResponse responses = feedService.findRecentFeeds("progress", false, Long.MAX_VALUE, 2);

        // then
        assertThat(responses.getFeeds()).hasSize(2);
        assertThat(responses.getFeeds().get(0).getTitle()).isEqualTo(SPRING_JAVA_FEED_REQUEST.getTitle());
        assertThat(responses.getFeeds().get(1).getTitle()).isEqualTo(EMPTY_TECH_FEED_REQUEST.getTitle());
        assertThat(responses.getNextFeedId()).isNull();
    }

    @DisplayName("step과 help를 고려한 페이지네이션을 지원한다. (step=complete, help=null, lastFeedId=Long.MAX_VALUE countPerPage=2")
    @Test
    void stepCompleteHelpNull() {
        // given
        EMPTY_TECH_FEED_REQUEST.setStep("PROGRESS");
        Long firstFeedId = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);
        SPRING_JAVA_FEED_REQUEST.setStep("PROGRESS");
        Long secondFeedId = feedService.create(찰리, SPRING_JAVA_FEED_REQUEST);
        REACT_FEED_REQUEST.setStep("COMPLETE");
        Long thirdFeedId = feedService.create(찰리, REACT_FEED_REQUEST);
        em.flush();
        em.clear();

        // when
        FeedCardPaginationResponse responses = feedService.findRecentFeeds("complete", false, Long.MAX_VALUE, 2);

        // then
        assertThat(responses.getFeeds()).hasSize(1);
        assertThat(responses.getFeeds().get(0).getTitle()).isEqualTo(REACT_FEED_REQUEST.getTitle());
        assertThat(responses.getNextFeedId()).isNull();
    }

    @DisplayName("step과 help를 고려한 페이지네이션을 지원한다. (step=all, help=true, lastFeedId=Long.MAX_VALUE countPerPage=3")
    @Test
    void stepAllHelpTrue() {
        // given
        EMPTY_TECH_FEED_REQUEST.setStep("PROGRESS");
        EMPTY_TECH_FEED_REQUEST.setSos(true);
        Long firstFeedId = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);
        SPRING_JAVA_FEED_REQUEST.setStep("PROGRESS");
        SPRING_JAVA_FEED_REQUEST.setSos(false);
        Long secondFeedId = feedService.create(찰리, SPRING_JAVA_FEED_REQUEST);
        REACT_FEED_REQUEST.setStep("COMPLETE");
        REACT_FEED_REQUEST.setSos(true);
        Long thirdFeedId = feedService.create(찰리, REACT_FEED_REQUEST);
        em.flush();
        em.clear();

        // when
        FeedCardPaginationResponse responses = feedService.findRecentFeeds("all", true, Long.MAX_VALUE, 3);

        // then
        assertThat(responses.getFeeds()).hasSize(2);
        assertThat(responses.getFeeds().get(0).getTitle()).isEqualTo(REACT_FEED_REQUEST.getTitle());
        assertThat(responses.getFeeds().get(1).getTitle()).isEqualTo(EMPTY_TECH_FEED_REQUEST.getTitle());
        assertThat(responses.getNextFeedId()).isNull();
    }

    @DisplayName("step과 help를 고려한 페이지네이션을 지원한다. (step=all, help=true, lastFeedId=thirdFeedId countPerPage=2")
    @Test
    void stepAllHelpTrue2() {
        // given
        EMPTY_TECH_FEED_REQUEST.setStep("PROGRESS");
        EMPTY_TECH_FEED_REQUEST.setSos(true);
        Long firstFeedId = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);
        SPRING_JAVA_FEED_REQUEST.setStep("PROGRESS");
        SPRING_JAVA_FEED_REQUEST.setSos(false);
        Long secondFeedId = feedService.create(찰리, SPRING_JAVA_FEED_REQUEST);
        REACT_FEED_REQUEST.setStep("COMPLETE");
        REACT_FEED_REQUEST.setSos(true);
        Long thirdFeedId = feedService.create(찰리, REACT_FEED_REQUEST);
        em.flush();
        em.clear();

        // when
        FeedCardPaginationResponse responses = feedService.findRecentFeeds("all", true, thirdFeedId, 3);

        // then
        assertThat(responses.getFeeds()).hasSize(2);
        assertThat(responses.getFeeds().get(0).getTitle()).isEqualTo(REACT_FEED_REQUEST.getTitle());
        assertThat(responses.getFeeds().get(1).getTitle()).isEqualTo(EMPTY_TECH_FEED_REQUEST.getTitle());
        assertThat(responses.getNextFeedId()).isNull();
    }

    @DisplayName("step과 help를 고려한 페이지네이션을 지원한다. (step=all, help=false, lastFeedId=thirdFeedId countPerPage=2")
    @Test
    void stepAllHelpFalse() {
        // given
        EMPTY_TECH_FEED_REQUEST.setStep("PROGRESS");
        EMPTY_TECH_FEED_REQUEST.setSos(true);
        Long firstFeedId = feedService.create(찰리, EMPTY_TECH_FEED_REQUEST);
        SPRING_JAVA_FEED_REQUEST.setStep("PROGRESS");
        SPRING_JAVA_FEED_REQUEST.setSos(false);
        Long secondFeedId = feedService.create(찰리, SPRING_JAVA_FEED_REQUEST);
        REACT_FEED_REQUEST.setStep("COMPLETE");
        REACT_FEED_REQUEST.setSos(true);
        Long thirdFeedId = feedService.create(찰리, REACT_FEED_REQUEST);
        em.flush();
        em.clear();

        // when
        FeedCardPaginationResponse responses = feedService.findRecentFeeds("all", false, thirdFeedId, 2);

        // then
        assertThat(responses.getFeeds()).hasSize(2);
        assertThat(responses.getFeeds().get(0).getTitle()).isEqualTo(REACT_FEED_REQUEST.getTitle());
        assertThat(responses.getFeeds().get(1).getTitle()).isEqualTo(SPRING_JAVA_FEED_REQUEST.getTitle());
        assertThat(responses.getNextFeedId()).isEqualTo(firstFeedId);
    }
/*
    @DisplayName("어드민 사용자는 피드를 수정할 수 있다.")
    @Test
    void updateFeedAsAdmin() {
        // given
        Long feedId1 = feedService.create(찰리, SPRING_JAVA_FEED_REQUEST);
        FeedRequest request = EMPTY_TECH_FEED_REQUEST;

        // when
        feedService.updateFeedAsAdmin(User.ADMIN_USER, feedId1, request);
        em.flush();
        em.clear();

        // then
        FeedResponse updateFeed = feedService.viewFeed(찰리, feedId1, true);
        피드_정보가_같은지_조회(request, updateFeed);
    }
*/
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
    }
}
