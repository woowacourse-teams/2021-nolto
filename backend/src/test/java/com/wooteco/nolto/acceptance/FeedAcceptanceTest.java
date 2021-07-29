package com.wooteco.nolto.acceptance;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import com.wooteco.nolto.feed.application.FeedService;
import com.wooteco.nolto.feed.ui.dto.FeedCardResponse;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import com.wooteco.nolto.user.domain.User;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class FeedAcceptanceTest extends AcceptanceTest {
    private User 가입된_엄청난_유저;

    private User 좋아요_1개_누를_유저;
    private User 좋아요_2개_누를_유저;
    private User 좋아요_3개_누를_유저;

    private Long 진행중_좋아요3개_1번째_피드_ID;
    private Long 전시중_좋아요2개_2번째_피드_ID;
    private Long 진행중_SOS_좋아요1개_3번째_피드_ID;
    private Long 전시중_SOS_좋아요0개_4번째_피드_ID;

    private Tech Apollo;
    private Tech WebGL;

    private FeedRequest 진행중_단계의_피드_요청 = new FeedRequest("제목1", new ArrayList<>(), "내용1", "PROGRESS", false,
            "www.github.com/woowacourse", null, null);
    private FeedRequest 전시중_단계의_피드_요청 = new FeedRequest("제목2", new ArrayList<>(), "내용2", "COMPLETE", false,
            "www.github.com/woowacourse", "www.github.com/woowacourse", null);
    private FeedRequest 진행중_단계의_SOS_피드_요청 = new FeedRequest("제목3", new ArrayList<>(), "내용3", "PROGRESS", true,
            "www.github.com/woowacourse", "www.github.com/woowacourse", null);
    private FeedRequest 전시중_단계의_SOS_피드_요청 = new FeedRequest("제목4", new ArrayList<>(), "내용4", "COMPLETE", true,
            "www.github.com/woowacourse", "www.github.com/woowacourse", null);

    @Autowired
    private FeedService feedService;

    @Autowired
    private TechRepository techRepository;

    @BeforeEach
    void init() {
        super.setUp();

        Tech tech1 = new Tech("Apollo Client");
        Tech tech2 = new Tech("WebGL");
        Apollo = techRepository.save(tech1);
        WebGL = techRepository.save(tech2);

        가입된_엄청난_유저 = 회원_등록되어_있음(엄청난_유저);

        진행중_좋아요3개_1번째_피드_ID = 피드_업로드되어_있음(진행중_단계의_피드_요청);
        전시중_좋아요2개_2번째_피드_ID = 피드_업로드되어_있음(전시중_단계의_피드_요청);
        진행중_SOS_좋아요1개_3번째_피드_ID = 피드_업로드되어_있음(진행중_단계의_SOS_피드_요청);
        전시중_SOS_좋아요0개_4번째_피드_ID = 피드_업로드되어_있음(전시중_단계의_SOS_피드_요청);

        좋아요_1개_누를_유저 = userRepository.save(new User(null, "2", SocialType.GITHUB, "아마찌", "https://dksykemwl00pf.cloudfront.net/amazzi.jpeg"));
        좋아요_2개_누를_유저 = userRepository.save(new User(null, "3", SocialType.GITHUB, "마찌", "https://dksykemwl00pf.cloudfront.net/amazzi.jpeg"));
        좋아요_3개_누를_유저 = userRepository.save(new User(null, "4", SocialType.GITHUB, "아마짜", "https://dksykemwl00pf.cloudfront.net/amazzi.jpeg"));
    }

    @DisplayName("인기순으로 피드를 조회한다.")
    @Test
    void hotResponse() {
        // given
        TokenResponse 좋아요_1개_누를_유저_토큰 = 가입된_유저의_토큰을_받는다(좋아요_1개_누를_유저);
        TokenResponse 좋아요_2개_누를_유저_토큰 = 가입된_유저의_토큰을_받는다(좋아요_2개_누를_유저);
        TokenResponse 좋아요_3개_누를_유저_토큰 = 가입된_유저의_토큰을_받는다(좋아요_3개_누를_유저);
        피드에_좋아요_눌러져있음(좋아요_1개_누를_유저_토큰, 진행중_좋아요3개_1번째_피드_ID);
        피드에_좋아요_눌러져있음(좋아요_2개_누를_유저_토큰, 진행중_좋아요3개_1번째_피드_ID);
        피드에_좋아요_눌러져있음(좋아요_3개_누를_유저_토큰, 진행중_좋아요3개_1번째_피드_ID);
        피드에_좋아요_눌러져있음(좋아요_2개_누를_유저_토큰, 전시중_좋아요2개_2번째_피드_ID);
        피드에_좋아요_눌러져있음(좋아요_3개_누를_유저_토큰, 전시중_좋아요2개_2번째_피드_ID);
        피드에_좋아요_눌러져있음(좋아요_3개_누를_유저_토큰, 진행중_SOS_좋아요1개_3번째_피드_ID);

        // when
        ExtractableResponse<Response> response = 인기순_피드_목록_조회_요청();

        // then
        피드_목록_조회_응답됨(response);
        피드_목록_포함됨(response, Arrays.asList(
                진행중_좋아요3개_1번째_피드_ID, 전시중_좋아요2개_2번째_피드_ID,
                진행중_SOS_좋아요1개_3번째_피드_ID, 전시중_SOS_좋아요0개_4번째_피드_ID)
        );
    }


    @DisplayName("최신순으로 모든(all) 필터링값의 피드를 조회한다")
    @Test
    void allRecentResponse() {
        // given
        String ALL = "all";
        String SOS = "sos";
        String PROGRESS = "progress";
        String COMPLETE = "complete";

        // when
        ExtractableResponse<Response> ALL_필터링값_응답 = 최신순_피드_목록_조회_요청(ALL);
        ExtractableResponse<Response> SOS_필터링값_응답 = 최신순_피드_목록_조회_요청(SOS);
        ExtractableResponse<Response> PROGRESS_필터링값_응답 = 최신순_피드_목록_조회_요청(PROGRESS);
        ExtractableResponse<Response> COMPLETE_필터링값_응답 = 최신순_피드_목록_조회_요청(COMPLETE);

        // then
        피드_목록_조회_응답됨(ALL_필터링값_응답);
        피드_목록_조회_응답됨(SOS_필터링값_응답);
        피드_목록_조회_응답됨(PROGRESS_필터링값_응답);
        피드_목록_조회_응답됨(COMPLETE_필터링값_응답);

        피드_목록_포함됨(ALL_필터링값_응답, Arrays.asList(
                전시중_SOS_좋아요0개_4번째_피드_ID, 진행중_SOS_좋아요1개_3번째_피드_ID,
                전시중_좋아요2개_2번째_피드_ID, 진행중_좋아요3개_1번째_피드_ID)
        );
        피드_목록_포함됨(SOS_필터링값_응답, Arrays.asList(
                전시중_SOS_좋아요0개_4번째_피드_ID, 진행중_SOS_좋아요1개_3번째_피드_ID)
        );
        피드_목록_포함됨(PROGRESS_필터링값_응답, Arrays.asList(
                진행중_SOS_좋아요1개_3번째_피드_ID, 진행중_좋아요3개_1번째_피드_ID)
        );
        피드_목록_포함됨(COMPLETE_필터링값_응답, Arrays.asList(
                전시중_SOS_좋아요0개_4번째_피드_ID, 전시중_좋아요2개_2번째_피드_ID)
        );
    }

    @DisplayName("최신순으로 지원하지 않는 필터링값으로 피드를 조회한다.")
    @Test
    void recentResponseException() {
        // given
        String filter = "notSupported";

        // when
        ExtractableResponse<Response> response = 최신순_피드_목록_조회_요청(filter);

        // then
        존재하지_않는_필터링값으로_피드_목록_조회_실패(response);
    }

    @DisplayName("쿼리, 기술, 필터값 없이 피드를 검색한다. - 빈 배열")
    @Test
    void searchResponseNothing() {
        // when
        ExtractableResponse<Response> response = 쿼리_기술_필터링값_모두없이_피드_검색_요청();

        // then
        피드_목록_조회_응답됨(response);
        피드_목록_포함됨(response, Collections.emptyList());
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
        피드_목록_포함됨(제목_쿼리_응답, Collections.singletonList(진행중_좋아요3개_1번째_피드_ID));
        피드_목록_포함됨(내용_쿼리_응답, Collections.singletonList(진행중_좋아요3개_1번째_피드_ID));
    }

    @DisplayName("techs로만 피드를 검색한다.")
    @Test
    void searchResponseOnlyTech() {
        // given
        FeedRequest Apollo_기술가진_피드_요청 = new FeedRequest("제목1", Collections.singletonList(Apollo.getId()),
                "내용1", "PROGRESS", false, "www.github.com/woowacourse", null, null);
        FeedRequest Apollo_AND_WebGL_기술가진_피드_요청 = new FeedRequest("제목1", Arrays.asList(Apollo.getId(), WebGL.getId()),
                "내용1", "PROGRESS", false, "www.github.com/woowacourse", null, null);

        Long Apollo_기술가진_피드_ID = 피드_업로드되어_있음(Apollo_기술가진_피드_요청);
        Long Apollo_AND_WebGL_기술가진_피드_ID = 피드_업로드되어_있음(Apollo_AND_WebGL_기술가진_피드_요청);

        // when
        ExtractableResponse<Response> Apollo_기술_응답 = 기술로_피드_검색_요청(Apollo.getName());
        ExtractableResponse<Response> Apollo_AND_WebGL_기술_응답 = 기술로_피드_검색_요청(Apollo.getName() + "," + WebGL.getName());

        // then
        피드_목록_조회_응답됨(Apollo_기술_응답);
        피드_목록_조회_응답됨(Apollo_AND_WebGL_기술_응답);
        피드_목록_포함됨(Apollo_기술_응답,Arrays.asList(Apollo_기술가진_피드_ID, Apollo_AND_WebGL_기술가진_피드_ID));
        피드_목록_포함됨(Apollo_AND_WebGL_기술_응답, Collections.singletonList(Apollo_AND_WebGL_기술가진_피드_ID));
    }

    @DisplayName("query, techs로 피드를 검색하고 필터링 조건으로도 검색한다.")
    @Test
    void searchResponseWithQueryTech() {
        // given
        String 제목_쿼리 = "제목1";
        String 필터링값 = "COMPLETE";

        FeedRequest Apollo_기술가진_피드_요청 = new FeedRequest(제목_쿼리, Collections.singletonList(Apollo.getId()),
                "내용1", "PROGRESS", false, "www.github.com/woowacourse", null, null);
        FeedRequest Apollo_AND_WebGL_기술가진_피드_요청 = new FeedRequest(제목_쿼리, Arrays.asList(Apollo.getId(), WebGL.getId()),
                "내용1", 필터링값, false, "www.github.com/woowacourse", "www.github.com/woowacourse", null);

        Long Apollo_기술가진_피드_ID = 피드_업로드되어_있음(Apollo_기술가진_피드_요청);
        Long Apollo_AND_WebGL_기술가진_피드_ID = 피드_업로드되어_있음(Apollo_AND_WebGL_기술가진_피드_요청);

        // when
        ExtractableResponse<Response> 쿼리와_기술로_피드_검색_응답 = 쿼리와_기술로_피드_검색_요청(제목_쿼리, Apollo.getName());
        ExtractableResponse<Response> 쿼리_기술_필터링값으로_피드_검색_응답 = 쿼리_기술_필터링값으로_피드_검색_요청(제목_쿼리, Apollo.getName() + "," + WebGL.getName(), 필터링값);

        // then
        피드_목록_조회_응답됨(쿼리와_기술로_피드_검색_응답);
        피드_목록_조회_응답됨(쿼리_기술_필터링값으로_피드_검색_응답);
        피드_목록_포함됨(쿼리와_기술로_피드_검색_응답,Arrays.asList(Apollo_기술가진_피드_ID, Apollo_AND_WebGL_기술가진_피드_ID));
        피드_목록_포함됨(쿼리_기술_필터링값으로_피드_검색_응답, Collections.singletonList(Apollo_AND_WebGL_기술가진_피드_ID));

    }

    @DisplayName("지원하지 않는 필터값으로만 피드를 검색한다.")
    @Test
    void searchResponseOnlyFilter() {
        // given
        String filter = "notSupported";

        // when
        ExtractableResponse<Response> 지원하지_않는_필터링값_응답 = 필터링값으로_피드_검색_요청(filter);

        // then
        존재하지_않는_필터링값으로_피드_목록_조회_실패(지원하지_않는_필터링값_응답);
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

        피드_목록_포함됨(ALL_필터링값_응답, Collections.emptyList());
        피드_목록_포함됨(SOS_필터링값_응답, Collections.emptyList());
        피드_목록_포함됨(PROGRESS_필터링값_응답, Collections.emptyList());
        피드_목록_포함됨(COMPLETE_필터링값_응답, Collections.emptyList());
    }

    private static ExtractableResponse<Response> 최신순_피드_목록_조회_요청(String filter) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/recent?filter={filter}", filter)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 인기순_피드_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/hot")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 쿼리_기술_필터링값_모두없이_피드_검색_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/search")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 쿼리로_피드_검색_요청(String query) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/search?query={query}", query)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 기술로_피드_검색_요청(String techs) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/search?techs={techs}", techs)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 쿼리와_기술로_피드_검색_요청(String query, String techs) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/search?query={query}&techs={techs}", query, techs)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 필터링값으로_피드_검색_요청(String filter) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/search?filter={filter}", filter)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 쿼리_기술_필터링값으로_피드_검색_요청(String query, String techs, String filter) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/search?query={query}&techs={techs}&filter={filter}", query, techs, filter)
                .then().log().all()
                .extract();
    }

    public static void 피드_목록_조회_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 존재하지_않는_필터링값으로_피드_목록_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().jsonPath().getString("errorCode")).isEqualTo("feed-005");
        assertThat(response.body().jsonPath().getString("message")).isEqualTo("지원하지 않는 피드의 필터링 값입니다.");
    }

    public static void 피드_목록_포함됨(ExtractableResponse<Response> response, List<Long> expectedLineIds) {
        List<Long> resultLineIds = response.jsonPath().getList(".", FeedCardResponse.class).stream()
                .map(FeedCardResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).isEqualTo(expectedLineIds);
    }

    // TODO: feed 업로드 인수테스트로 변경해야함
    public Long 피드_업로드되어_있음(FeedRequest request) {
        return feedService.create(가입된_엄청난_유저, request);
    }

    public void 피드에_좋아요_눌러져있음(TokenResponse tokenResponse, Long feedId) {
        RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/feeds/{feedId}/like", feedId)
                .then().log().all()
                .extract();
    }
}


