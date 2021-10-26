package com.wooteco.nolto.acceptance;

import com.wooteco.nolto.auth.ui.dto.AllTokenResponse;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.dto.ExceptionResponse;
import com.wooteco.nolto.feed.ui.dto.FeedCardPaginationResponse;
import com.wooteco.nolto.feed.ui.dto.FeedCardResponse;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.ui.dto.TechResponse;
import com.wooteco.nolto.user.domain.User;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.wooteco.nolto.FeedFixture.DEFAULT_IMAGE;
import static com.wooteco.nolto.TechFixture.*;
import static com.wooteco.nolto.UserFixture.*;
import static com.wooteco.nolto.exception.ErrorType.ALREADY_LIKED;
import static com.wooteco.nolto.exception.ErrorType.NOT_LIKED;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("피드 관련 기능")
class FeedAcceptanceTest extends AcceptanceTest {

    protected static final FeedRequest 진행중_단계의_피드_요청 = new FeedRequest("title1", new ArrayList<>(), "content1", "PROGRESS", false,
            "www.github.com/woowacourse", null, null);
    protected static final FeedRequest 전시중_단계의_피드_요청 = new FeedRequest("title2", new ArrayList<>(), "content2", "COMPLETE", false,
            "www.github.com/woowacourse", "www.github.com/woowacourse", null);
    protected static final FeedRequest 진행중_단계의_SOS_피드_요청 = new FeedRequest("title3", new ArrayList<>(), "content3", "PROGRESS", true,
            "www.github.com/woowacourse", "www.github.com/woowacourse", null);
    protected static final FeedRequest 전시중_단계의_SOS_피드_요청 = new FeedRequest("title4", new ArrayList<>(), "content4", "COMPLETE", true,
            "www.github.com/woowacourse", "www.github.com/woowacourse", null);
    protected static final File THUMBNAIL_IMAGE = new File(new File("").getAbsolutePath() + "/src/test/resources/static/" + DEFAULT_IMAGE);

    private String 멤버의_토큰;

    private User 좋아요_1개_누를_유저;
    private User 좋아요_2개_누를_유저;
    private User 좋아요_3개_누를_유저;

    private Long 진행중_좋아요3개_1번째_피드_ID;
    private Long 전시중_좋아요2개_2번째_피드_ID;
    private Long 진행중_SOS_좋아요1개_3번째_피드_ID;
    private Long 전시중_SOS_좋아요0개_4번째_피드_ID;

    private final Tech 자바 = 자바_생성();
    private final Tech 스프링 = 스프링_생성();
    private final Tech 리액트 = 리액트_생성();

    @BeforeEach
    void setUpOnFeedAcceptance() {
        super.setUp();
        techRepository.saveAll(Arrays.asList(자바, 스프링, 리액트));

        멤버의_토큰 = 가입된_유저의_토큰을_받는다().getAccessToken().getValue();

        진행중_좋아요3개_1번째_피드_ID = 피드_업로드되어_있음(진행중_단계의_피드_요청);
        전시중_좋아요2개_2번째_피드_ID = 피드_업로드되어_있음(전시중_단계의_피드_요청);
        진행중_SOS_좋아요1개_3번째_피드_ID = 피드_업로드되어_있음(진행중_단계의_SOS_피드_요청);
        전시중_SOS_좋아요0개_4번째_피드_ID = 피드_업로드되어_있음(전시중_단계의_SOS_피드_요청);

        좋아요_1개_누를_유저 = 회원_등록되어_있음(아마찌_생성());
        좋아요_2개_누를_유저 = 회원_등록되어_있음(조엘_생성());
        좋아요_3개_누를_유저 = 회원_등록되어_있음(포모_생성());
    }

    @AfterEach
    void clearOnFeedAcceptance() {
        super.clear();
    }

    @DisplayName("멤버가 피드를 작성한다. (이미지 : 기본 썸네일)")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> response = 피드_작성_요청(진행중_단계의_피드_요청, 멤버의_토큰);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("게스트가 피드를 조회한다.")
    @Test
    void findByIdWithGuest() {
        // given
        Long 업로드되어_있는_피드_ID = 피드_업로드되어_있음(전시중_단계의_피드_요청);
        // when
        ExtractableResponse<Response> response = 피드_조회_요청(업로드되어_있는_피드_ID);

        // then
        피드_정보가_같은지_조회(response, 전시중_단계의_피드_요청);
    }

    @DisplayName("이미 읽은 피드를 조회할 경우, 24시간 이내에서는 조회수가 증가하지 않는다.")
    @Test
    void viewWithCookie() {
        // given
        Long 업로드되어_있는_피드_ID = 피드_업로드되어_있음(전시중_단계의_피드_요청);
        // when
        Cookie 업로드되어_있는_피드_쿠키 = new Cookie.Builder("view", "/" + String.valueOf(업로드되어_있는_피드_ID) + "/").build();
        ExtractableResponse<Response> 아직_조회하지_않은_응답 = 피드_조회_요청(업로드되어_있는_피드_ID);
        ExtractableResponse<Response> 이미_조회한_응답 = 피드_조회_요청(업로드되어_있는_피드_ID, 업로드되어_있는_피드_쿠키);

        // then
        FeedResponse 아직_조회하지_않은_피드_응답 = 아직_조회하지_않은_응답.as(FeedResponse.class);
        FeedResponse 이미_조회한_피드_응답 = 이미_조회한_응답.as(FeedResponse.class);
        assertThat(아직_조회하지_않은_피드_응답.getViews()).isEqualTo(이미_조회한_피드_응답.getViews());
    }

    @DisplayName("멤버가 피드를 조회한다.")
    @Test
    void findByIdWithMember() {
        // given
        Long 업로드되어_있는_피드_ID = 피드_업로드되어_있음(전시중_단계의_피드_요청);

        // when
        ExtractableResponse<Response> response = 피드_조회_요청(업로드되어_있는_피드_ID, 멤버의_토큰);

        // then
        피드_정보가_같은지_조회(response, 전시중_단계의_피드_요청);
    }

    @DisplayName("멤버가 자신의 피드를 수정한다.")
    @Test
    void update() {
        // given
        Long 업로드되어_있는_피드_ID = 피드_업로드되어_있음(전시중_단계의_피드_요청);

        // when
        ExtractableResponse<Response> updateResponse = 피드_수정_요청(멤버의_토큰, 업로드되어_있는_피드_ID, 전시중_단계의_SOS_피드_요청);

        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("멤버가 자신의 피드를 삭제한다.")
    @Test
    void delete() {
        // given
        Long 업로드되어_있는_피드_ID = 피드_업로드되어_있음(전시중_단계의_피드_요청);

        // when
        ExtractableResponse<Response> updateResponse = 피드_삭제_요청(멤버의_토큰, 업로드되어_있는_피드_ID);

        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    @DisplayName("멤버가 좋아요를 누른다.")
    @Test
    void addLike() {
        // given
        Long 업로드되어_있는_피드_ID = 피드_업로드되어_있음(전시중_단계의_피드_요청);

        // when
        ExtractableResponse<Response> likeResponse = 좋아요_요청(멤버의_토큰, 업로드되어_있는_피드_ID);

        // then
        assertThat(likeResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("이미 좋아요를 누른 멤버가 좋아요를 다시 누르면 예외가 발생한다.")
    @Test
    void canNotAddLike() {
        // given
        Long 업로드되어_있는_피드_ID = 피드_업로드되어_있음(전시중_단계의_피드_요청);
        좋아요_요청(멤버의_토큰, 업로드되어_있는_피드_ID);

        // when
        ExtractableResponse<Response> response = 좋아요_요청(멤버의_토큰, 업로드되어_있는_피드_ID);

        // then
        좋아요_예외_발생(response, ALREADY_LIKED);
    }

    @DisplayName("멤버가 좋아요를 취소한다.")
    @Test
    void deleteLike() {
        // given
        Long 업로드되어_있는_피드_ID = 피드_업로드되어_있음(전시중_단계의_피드_요청);
        좋아요_요청(멤버의_토큰, 업로드되어_있는_피드_ID);

        // when
        ExtractableResponse<Response> likeResponse = 좋아요_취소_요청(멤버의_토큰, 업로드되어_있는_피드_ID);

        // then
        assertThat(likeResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("좋아요를 누르지 않은 멤버가 좋아요를 취소하면 예외가 발생한다.")
    @Test
    void canNotDeleteLike() {
        // given
        Long 업로드되어_있는_피드_ID = 피드_업로드되어_있음(전시중_단계의_피드_요청);

        // when
        ExtractableResponse<Response> response = 좋아요_취소_요청(멤버의_토큰, 업로드되어_있는_피드_ID);

        // then
        좋아요_예외_발생(response, NOT_LIKED);
    }

    @DisplayName("멤버가 좋아요를 취소하고 다시 좋아요를 누른다.")
    @Test
    void deleteLikeAndAgain() {
        // given
        Long 업로드되어_있는_피드_ID = 피드_업로드되어_있음(전시중_단계의_피드_요청);
        좋아요_요청(멤버의_토큰, 업로드되어_있는_피드_ID);
        좋아요_취소_요청(멤버의_토큰, 업로드되어_있는_피드_ID);

        // when
        ExtractableResponse<Response> likeResponse = 좋아요_요청(멤버의_토큰, 업로드되어_있는_피드_ID);

        // then
        assertThat(likeResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("인기순으로 피드를 조회한다.")
    @Test
    void hotResponse() {
        // given
        AllTokenResponse 좋아요_1개_누를_유저_토큰 = 유저의_토큰을_받는다(좋아요_1개_누를_유저);
        AllTokenResponse 좋아요_2개_누를_유저_토큰 = 유저의_토큰을_받는다(좋아요_2개_누를_유저);
        AllTokenResponse 좋아요_3개_누를_유저_토큰 = 유저의_토큰을_받는다(좋아요_3개_누를_유저);
        좋아요_요청(좋아요_1개_누를_유저_토큰.getAccessToken().getValue(), 진행중_좋아요3개_1번째_피드_ID);
        좋아요_요청(좋아요_2개_누를_유저_토큰.getAccessToken().getValue(), 진행중_좋아요3개_1번째_피드_ID);
        좋아요_요청(좋아요_3개_누를_유저_토큰.getAccessToken().getValue(), 진행중_좋아요3개_1번째_피드_ID);
        좋아요_요청(좋아요_2개_누를_유저_토큰.getAccessToken().getValue(), 전시중_좋아요2개_2번째_피드_ID);
        좋아요_요청(좋아요_3개_누를_유저_토큰.getAccessToken().getValue(), 전시중_좋아요2개_2번째_피드_ID);
        좋아요_요청(좋아요_3개_누를_유저_토큰.getAccessToken().getValue(), 진행중_SOS_좋아요1개_3번째_피드_ID);

        // when
        ExtractableResponse<Response> response = 인기순_피드_목록_조회_요청();

        // then
        피드_목록_조회_응답됨(response);
        피드_목록_포함됨(response, Arrays.asList(
                진행중_좋아요3개_1번째_피드_ID, 전시중_좋아요2개_2번째_피드_ID,
                진행중_SOS_좋아요1개_3번째_피드_ID, 전시중_SOS_좋아요0개_4번째_피드_ID)
        );
    }

    @DisplayName("최신 피드 조회 시 필터가 없어도 잘 작동하는 지 확인한다.")
    @Test
    void allRecentResponse_no_filter() {
        // given
        String ALL = "";

        // when
        ExtractableResponse<Response> ALL_필터링값_응답 = 최신순_피드_목록_조회_요청(ALL);

        // then
        피드_목록_조회_응답됨(ALL_필터링값_응답);
        페이지네이션_피드_목록_포함됨(ALL_필터링값_응답,
                Arrays.asList(전시중_SOS_좋아요0개_4번째_피드_ID, 진행중_SOS_좋아요1개_3번째_피드_ID, 전시중_좋아요2개_2번째_피드_ID, 진행중_좋아요3개_1번째_피드_ID),
                null);
    }

    @DisplayName("최신 피드 조회 시 help 필터가 잘 작동하는 지 확인한다.")
    @Test
    void allRecentResponse_help_filter() {
        // given
        String HELP = "help=true";

        // when
        ExtractableResponse<Response> SOS_필터링값_응답 = 최신순_피드_목록_조회_요청(HELP);

        // then
        피드_목록_조회_응답됨(SOS_필터링값_응답);
        페이지네이션_피드_목록_포함됨(SOS_필터링값_응답,
                Arrays.asList(전시중_SOS_좋아요0개_4번째_피드_ID, 진행중_SOS_좋아요1개_3번째_피드_ID),
                null);
    }

    @DisplayName("최신 피드 조회 시 nextFeedId 필터가 잘 작동하는 지 확인한다.")
    @Test
    void allRecentResponse_nextFeedId_filter() {
        // given
        String NEXT_FEED_ID = "nextFeedId=3";

        // when
        ExtractableResponse<Response> NEXT_FEED_ID_필터링값_응답 = 최신순_피드_목록_조회_요청(NEXT_FEED_ID);

        // then
        피드_목록_조회_응답됨(NEXT_FEED_ID_필터링값_응답);
        페이지네이션_피드_목록_포함됨(NEXT_FEED_ID_필터링값_응답,
                Arrays.asList(진행중_SOS_좋아요1개_3번째_피드_ID, 전시중_좋아요2개_2번째_피드_ID, 진행중_좋아요3개_1번째_피드_ID),
                null);
    }

    @DisplayName("최신 피드 조회 시 step 필터가 잘 작동하는 지 확인한다.")
    @Test
    void allRecentResponse_step_filter() {
        // given
        String PROGRESS = "step=progress";
        String COMPLETE = "step=complete";

        // when
        ExtractableResponse<Response> PROGRESS_필터링값_응답 = 최신순_피드_목록_조회_요청(PROGRESS);
        ExtractableResponse<Response> COMPLETE_필터링값_응답 = 최신순_피드_목록_조회_요청(COMPLETE);

        // then
        피드_목록_조회_응답됨(PROGRESS_필터링값_응답);
        피드_목록_조회_응답됨(COMPLETE_필터링값_응답);
        페이지네이션_피드_목록_포함됨(PROGRESS_필터링값_응답,
                Arrays.asList(진행중_SOS_좋아요1개_3번째_피드_ID, 진행중_좋아요3개_1번째_피드_ID),
                null);
        페이지네이션_피드_목록_포함됨(COMPLETE_필터링값_응답,
                Arrays.asList(전시중_SOS_좋아요0개_4번째_피드_ID, 전시중_좋아요2개_2번째_피드_ID),
                null);
    }

    @DisplayName("최신 피드 조회 시 countPerPage 필터가 잘 작동하는 지 확인한다.")
    @Test
    void allRecentResponse_countPerPage_filter() {
        // given
        String COUNT_PER_PAGE = "countPerPage=2";

        // when
        ExtractableResponse<Response> COUNT_PER_PAGE_필터링값_응답 = 최신순_피드_목록_조회_요청(COUNT_PER_PAGE);

        // then
        피드_목록_조회_응답됨(COUNT_PER_PAGE_필터링값_응답);
        페이지네이션_피드_목록_포함됨(COUNT_PER_PAGE_필터링값_응답,
                Arrays.asList(전시중_SOS_좋아요0개_4번째_피드_ID, 진행중_SOS_좋아요1개_3번째_피드_ID),
                2L);
    }

    @DisplayName("최신순으로 지원하지 않는 필터링값으로 피드를 조회하면 디폴트 값으로 조회를 한다.")
    @Test
    void recentResponseException() {
        // given
        String filter = "newfilter=true";

        // when
        ExtractableResponse<Response> ALL_필터링값_응답 = 최신순_피드_목록_조회_요청(filter);

        // then
        피드_목록_조회_응답됨(ALL_필터링값_응답);
        페이지네이션_피드_목록_포함됨(ALL_필터링값_응답,
                Arrays.asList(전시중_SOS_좋아요0개_4번째_피드_ID, 진행중_SOS_좋아요1개_3번째_피드_ID, 전시중_좋아요2개_2번째_피드_ID, 진행중_좋아요3개_1번째_피드_ID),
                null);
    }

    @DisplayName("쿼리, 기술, 필터값 없이 피드를 검색한다. - 빈 배열")
    @Test
    void searchResponseNothing() {
        // when
        ExtractableResponse<Response> response = 쿼리_기술_필터링값_모두없이_피드_검색_요청();

        // then
        피드_목록_조회_응답됨(response);
        페이지네이션_피드_목록_포함됨(response, Collections.emptyList(), null);
    }

    @DisplayName("query로만 피드를 검색한다.")
    @Test
    void searchResponseOnlyQuery() {
        // when
        ExtractableResponse<Response> 제목_쿼리_응답 = 쿼리로_피드_검색_요청(진행중_단계의_피드_요청.getTitle());
        ExtractableResponse<Response> 내용_쿼리_응답 = 쿼리로_피드_검색_요청(진행중_단계의_피드_요청.getContent());

        // then
        피드_목록_조회_응답됨(제목_쿼리_응답);
        피드_목록_조회_응답됨(내용_쿼리_응답);
        페이지네이션_피드_목록_포함됨(제목_쿼리_응답, Collections.singletonList(진행중_좋아요3개_1번째_피드_ID), null);
        페이지네이션_피드_목록_포함됨(내용_쿼리_응답, Collections.singletonList(진행중_좋아요3개_1번째_피드_ID), null);
    }

    @DisplayName("techs로만 피드를 검색한다.")
    @Test
    void searchResponseOnlyTech() {
        // given
        FeedRequest JAVA_기술가진_피드_요청 = new FeedRequest("제목1", Collections.singletonList(자바.getId()),
                "내용1", "PROGRESS", false, "www.github.com/woowacourse", null, null);
        FeedRequest JAVA_AND_SPRING_기술가진_피드_요청 = new FeedRequest("제목1", Arrays.asList(자바.getId(), 스프링.getId()),
                "내용1", "PROGRESS", false, "www.github.com/woowacourse", null, null);

        Long JAVA_기술가진_피드_ID = 피드_업로드되어_있음(JAVA_기술가진_피드_요청);
        Long JAVA_AND_SPRING_기술가진_피드_ID = 피드_업로드되어_있음(JAVA_AND_SPRING_기술가진_피드_요청);

        // when
        ExtractableResponse<Response> JAVA_기술_응답 = 기술로_피드_검색_요청(자바.getName());
        ExtractableResponse<Response> JAVA_OR_SPRING_기술_응답 = 기술로_피드_검색_요청(자바.getName() + "," + 스프링.getName());

        // then
        피드_목록_조회_응답됨(JAVA_기술_응답);
        피드_목록_조회_응답됨(JAVA_OR_SPRING_기술_응답);
        페이지네이션_피드_목록_포함됨(JAVA_기술_응답, Arrays.asList(JAVA_AND_SPRING_기술가진_피드_ID, JAVA_기술가진_피드_ID), null);
        페이지네이션_피드_목록_포함됨(JAVA_OR_SPRING_기술_응답, Arrays.asList(JAVA_AND_SPRING_기술가진_피드_ID, JAVA_기술가진_피드_ID), null);
    }

    @DisplayName("query, techs로 피드를 검색하고 필터링 조건으로도 검색한다.")
    @Test
    void searchResponseWithQueryTech() {
        // given
        String 제목_쿼리 = "title1";
        String 필터링값 = "COMPLETE";

        FeedRequest JAVA_기술가진_피드_요청 = new FeedRequest(제목_쿼리, Collections.singletonList(자바.getId()),
                "내용1", "PROGRESS", false, "www.github.com/woowacourse", null, null);
        FeedRequest JAVA_AND_SPRING_기술가진_피드_요청 = new FeedRequest(제목_쿼리, Arrays.asList(자바.getId(), 스프링.getId()),
                "내용1", 필터링값, false, "www.github.com/woowacourse", "www.github.com/woowacourse", null);

        Long JAVA_기술가진_피드_ID = 피드_업로드되어_있음(JAVA_기술가진_피드_요청);
        Long JAVA_AND_SPRING_기술가진_피드_ID = 피드_업로드되어_있음(JAVA_AND_SPRING_기술가진_피드_요청);

        // when
        ExtractableResponse<Response> 쿼리와_기술로_피드_검색_응답 = 쿼리와_기술로_피드_검색_요청(제목_쿼리, 자바.getName());
        ExtractableResponse<Response> 쿼리_기술_필터링값으로_피드_검색_응답 = 쿼리_기술_필터링값으로_피드_검색_요청(제목_쿼리, 자바.getName() + "," + 스프링.getName(), 필터링값);

        // then
        피드_목록_조회_응답됨(쿼리와_기술로_피드_검색_응답);
        피드_목록_조회_응답됨(쿼리_기술_필터링값으로_피드_검색_응답);
        페이지네이션_피드_목록_포함됨(쿼리와_기술로_피드_검색_응답, Arrays.asList(JAVA_AND_SPRING_기술가진_피드_ID, JAVA_기술가진_피드_ID), null);
        페이지네이션_피드_목록_포함됨(쿼리_기술_필터링값으로_피드_검색_응답, Arrays.asList(JAVA_AND_SPRING_기술가진_피드_ID, JAVA_기술가진_피드_ID), null);
    }

    @DisplayName("지원하지 않는 필터값으로만 피드를 검색한다.")
    @Test
    void searchResponseOnlyFilter() {
        // given
        String step = "notSupported";

        // when
        ExtractableResponse<Response> 지원하지_않는_프로젝트_단계_응답 = 프로젝트_단계로_피드_검색_요청(step);

        // then
        페이지네이션_피드_목록_포함됨(지원하지_않는_프로젝트_단계_응답, Collections.emptyList(), null);
    }

    @DisplayName("필터값으로만 피드를 검색한다. - 빈 배열")
    @Test
    void searchResponseOnlyFilterButNotSupported() {
        // given
        String ALL = "all";
        String SOS = "sos";
        String PROGRESS = "progress";
        String COMPLETE = "complete";

        // when
        ExtractableResponse<Response> ALL_필터링값_응답 = 필터링값으로_피드_검색_요청(ALL);
        ExtractableResponse<Response> SOS_필터링값_응답 = 필터링값으로_피드_검색_요청(SOS);
        ExtractableResponse<Response> PROGRESS_필터링값_응답 = 필터링값으로_피드_검색_요청(PROGRESS);
        ExtractableResponse<Response> COMPLETE_필터링값_응답 = 필터링값으로_피드_검색_요청(COMPLETE);

        // then
        피드_목록_조회_응답됨(ALL_필터링값_응답);
        피드_목록_조회_응답됨(SOS_필터링값_응답);
        피드_목록_조회_응답됨(PROGRESS_필터링값_응답);
        피드_목록_조회_응답됨(COMPLETE_필터링값_응답);

        페이지네이션_피드_목록_포함됨(ALL_필터링값_응답, Collections.emptyList(), null);
        페이지네이션_피드_목록_포함됨(SOS_필터링값_응답, Collections.emptyList(), null);
        페이지네이션_피드_목록_포함됨(PROGRESS_필터링값_응답, Collections.emptyList(), null);
        페이지네이션_피드_목록_포함됨(COMPLETE_필터링값_응답, Collections.emptyList(), null);
    }

    public static ExtractableResponse<Response> 피드_작성_요청(FeedRequest feedRequest, String token) {
        RequestSpecification requestSpecification = RestAssured.given().log().all()
                .auth().oauth2(token)
                .formParam("title", feedRequest.getTitle())
                .formParam("content", feedRequest.getContent())
                .formParam("step", feedRequest.getStep())
                .formParam("sos", feedRequest.isSos())
                .formParam("StorageUrl", feedRequest.getStorageUrl())
                .formParam("DeployedUrl", feedRequest.getDeployedUrl())
                .multiPart("thumbnailImage", THUMBNAIL_IMAGE);

        feedRequest.getTechs().stream()
                .forEach(techId -> requestSpecification.formParam("techs", techId));

        return requestSpecification
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .post("/feeds")
                .then().log().all()
                .extract();
    }

    private void 피드_정보가_같은지_조회(ExtractableResponse<Response> response, FeedRequest request) {
        FeedResponse feedResponse = response.as(FeedResponse.class);
        List<Long> techIds = feedResponse.getTechs().stream()
                .map(TechResponse::getId)
                .collect(Collectors.toList());

        assertThat(request.getTitle()).isEqualTo(feedResponse.getTitle());
        assertThat(request.getTechs()).containsExactlyElementsOf(techIds);
        assertThat(request.getContent()).isEqualTo(feedResponse.getContent());
        assertThat(request.getStep()).isEqualTo(feedResponse.getStep());
        assertThat(request.isSos()).isEqualTo(feedResponse.isSos());
        assertThat(request.getStorageUrl()).isEqualTo(feedResponse.getStorageUrl());
        assertThat(request.getDeployedUrl()).isEqualTo(feedResponse.getDeployedUrl());
    }

    public static ExtractableResponse<Response> 피드_조회_요청(Long 업로드되어_있는_피드_ID) {
        return RestAssured.given().log().all()
                .when()
                .get("/feeds/{feedId}", 업로드되어_있는_피드_ID)
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 피드_조회_요청(Long 업로드되어_있는_피드_ID, String token) {
        return RestAssured.given().log().all()
                .when()
                .auth().oauth2(token)
                .get("/feeds/{feedId}", 업로드되어_있는_피드_ID)
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 피드_조회_요청(Long 업로드되어_있는_피드_ID, Cookie cookie) {
        return RestAssured.given().log().all()
                .when()
                .cookie(cookie)
                .get("/feeds/{feedId}", 업로드되어_있는_피드_ID)
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 피드_수정_요청(String token, Long feedId, FeedRequest request) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .formParam("title", request.getTitle())
                .formParam("techs", String.valueOf(스프링.getId()))
                .formParam("techs", String.valueOf(리액트.getId()))
                .formParam("content", request.getContent())
                .formParam("step", request.getStep())
                .formParam("sos", request.isSos())
                .formParam("StorageUrl", request.getStorageUrl())
                .formParam("DeployedUrl", request.getDeployedUrl())
                .multiPart("thumbnailImage", THUMBNAIL_IMAGE)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .put("/feeds/{feedId}", feedId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 피드_삭제_요청(String token, Long feedId) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when()
                .delete("/feeds/{feedId}", feedId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 좋아요_요청(String token, Long feedId) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when()
                .post("/feeds/{feedId}/like", feedId)
                .then().log().all()
                .extract();
    }

    private void 좋아요_예외_발생(ExtractableResponse<Response> response, ErrorType alreadyLiked) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(exceptionResponse.getErrorCode()).isEqualTo(alreadyLiked.getErrorCode());
        assertThat(exceptionResponse.getMessage()).isEqualTo(alreadyLiked.getMessage());
    }

    private ExtractableResponse<Response> 좋아요_취소_요청(String token, Long feedId) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when()
                .post("/feeds/{feedId}/unlike", feedId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 최신순_피드_목록_조회_요청(String params) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/recent?{param}", params)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 인기순_피드_목록_조회_요청() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/hot")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 쿼리_기술_필터링값_모두없이_피드_검색_요청() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/search")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 쿼리로_피드_검색_요청(String query) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/search?query={query}", query)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 기술로_피드_검색_요청(String techs) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/search?techs={techs}", techs)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 쿼리와_기술로_피드_검색_요청(String query, String techs) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/search?query={query}&techs={techs}", query, techs)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 필터링값으로_피드_검색_요청(String filter) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/search?filter={filter}", filter)
                .then().log().all()
                .extract();
    }


    private ExtractableResponse<Response> 프로젝트_단계로_피드_검색_요청(String step) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/search?step={step}", step)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 쿼리_기술_필터링값으로_피드_검색_요청(String query, String techs, String filter) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/search?query={query}&techs={techs}&filter={filter}", query, techs, filter)
                .then().log().all()
                .extract();
    }

    private void 피드_목록_조회_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 존재하지_않는_필터링값으로_피드_목록_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().jsonPath().getString("errorCode")).isEqualTo("feed-005");
        assertThat(response.body().jsonPath().getString("message")).isEqualTo("지원하지 않는 피드의 필터링 값입니다.");
    }

    private void 피드_목록_포함됨(ExtractableResponse<Response> response, List<Long> expectedLineIds) {
        List<Long> resultLineIds = response.jsonPath().getList(".", FeedCardResponse.class).stream()
                .map(FeedCardResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).isEqualTo(expectedLineIds);
    }

    private void 페이지네이션_피드_목록_포함됨(ExtractableResponse<Response> response, List<Long> expectedLineIds, Long nextFeedId) {
        FeedCardPaginationResponse cardPaginationResponse = response.jsonPath().getObject(".", FeedCardPaginationResponse.class);

        List<Long> feedIds = cardPaginationResponse.getFeeds().stream()
                .map(FeedCardResponse::getId)
                .collect(Collectors.toList());

        assertThat(feedIds).isEqualTo(expectedLineIds);
        assertThat(cardPaginationResponse.getNextFeedId()).isEqualTo(nextFeedId);
    }
}