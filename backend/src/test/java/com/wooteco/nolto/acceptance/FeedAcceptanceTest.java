package com.wooteco.nolto.acceptance;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import com.wooteco.nolto.exception.dto.ExceptionResponse;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.ui.dto.FeedCardResponse;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.image.application.ImageKind;
import com.wooteco.nolto.image.application.ImageService;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import com.wooteco.nolto.tech.ui.dto.TechResponse;
import com.wooteco.nolto.user.domain.User;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.wooteco.nolto.exception.ErrorType.ALREADY_LIKED;
import static com.wooteco.nolto.exception.ErrorType.NOT_LIKED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;


public class FeedAcceptanceTest extends AcceptanceTest {

    private User 좋아요_1개_누를_유저;
    private User 좋아요_2개_누를_유저;
    private User 좋아요_3개_누를_유저;

    private Long 진행중_좋아요3개_1번째_피드_ID;
    private Long 전시중_좋아요2개_2번째_피드_ID;
    private Long 진행중_SOS_좋아요1개_3번째_피드_ID;
    private Long 전시중_SOS_좋아요0개_4번째_피드_ID;

    private FeedRequest 진행중_단계의_피드_요청 = new FeedRequest("title1", new ArrayList<>(), "content1", "PROGRESS", false,
            "www.github.com/woowacourse", null, null);
    private FeedRequest 전시중_단계의_피드_요청 = new FeedRequest("title2", new ArrayList<>(), "content2", "COMPLETE", false,
            "www.github.com/woowacourse", "www.github.com/woowacourse", null);
    private FeedRequest 진행중_단계의_SOS_피드_요청 = new FeedRequest("title3", new ArrayList<>(), "content3", "PROGRESS", true,
            "www.github.com/woowacourse", "www.github.com/woowacourse", null);
    private FeedRequest 전시중_단계의_SOS_피드_요청 = new FeedRequest("title4", new ArrayList<>(), "content4", "COMPLETE", true,
            "www.github.com/woowacourse", "www.github.com/woowacourse", null);

    private final String defaultImageUrl = "nolto-default-thumbnail.png";
    private final File thumbnail = new File(new File("").getAbsolutePath() + "/src/test/resources/static/" + defaultImageUrl);

    @Autowired
    private TechRepository techRepository;

    @MockBean
    private ImageService imageService;

    private final Tech JAVA = new Tech("Java");
    private final Tech SPRING = new Tech("Spring");
    private final Tech REACT = new Tech("React");

    @Override
    @BeforeEach
    public void setUp() {
        BDDMockito.given(imageService.upload(any(MultipartFile.class), any(ImageKind.class))).willReturn("https://dksykemwl00pf.cloudfront.net/" + defaultImageUrl);
        techRepository.saveAll(Arrays.asList(JAVA, SPRING, REACT));

        진행중_좋아요3개_1번째_피드_ID = 피드_업로드되어_있음(진행중_단계의_피드_요청);
        전시중_좋아요2개_2번째_피드_ID = 피드_업로드되어_있음(전시중_단계의_피드_요청);
        진행중_SOS_좋아요1개_3번째_피드_ID = 피드_업로드되어_있음(진행중_단계의_SOS_피드_요청);
        전시중_SOS_좋아요0개_4번째_피드_ID = 피드_업로드되어_있음(전시중_단계의_SOS_피드_요청);

        좋아요_1개_누를_유저 = 회원_등록되어_있음(new User( "2", SocialType.GITHUB, "아마찌", "https://dksykemwl00pf.cloudfront.net/amazzi.jpeg"));
        좋아요_2개_누를_유저 = 회원_등록되어_있음(new User( "3", SocialType.GITHUB, "마찌", "https://dksykemwl00pf.cloudfront.net/amazzi.jpeg"));
        좋아요_3개_누를_유저 = 회원_등록되어_있음(new User( "4", SocialType.GITHUB, "아마짜", "https://dksykemwl00pf.cloudfront.net/amazzi.jpeg"));
    }

    @DisplayName("놀토의 회원이 피드를 작성한다. (이미지 : 기본 썸네일)")
    @Test
    public void create() {
        // given
        FeedRequest feedRequest = new FeedRequest(
                "엄청난 프로젝트",
                Arrays.asList(JAVA.getId(), SPRING.getId()),
                "안녕하세요. \n",
                Step.COMPLETE.name(),
                false,
                "https://github.com/woowacourse-teams/2021-nolto",
                "https://github.com/woowacourse-teams/2021-nolto",
                null
        );

        // when
        ExtractableResponse<Response> response = 피드를_작성한다(feedRequest, 가입된_유저의_토큰을_받는다().getAccessToken());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("비회원이 피드를 조회한다.")
    @Test
    void findByIdWithGuest() {
        // given
        FeedRequest request = new FeedRequest(
                "My Project",
                Arrays.asList(JAVA.getId(), SPRING.getId()),
                "hello. this is my project",
                Step.PROGRESS.name(),
                true,
                "https://github.com/woowacourse-teams/2021-nolto",
                "",
                null
        );
        ExtractableResponse<Response> saveResponse = 피드를_작성한다(request, 가입된_유저의_토큰을_받는다().getAccessToken());
        Long feedId = Long.valueOf(saveResponse.header("Location").replace("/feeds/", ""));

        // when
        ExtractableResponse<Response> response = given().log().all()
                .when()
                .get("/feeds/{feedId}", feedId)
                .then()
                .log().all()
                .extract();

        // then
        FeedResponse feedResponse = response.as(FeedResponse.class);
        피드_정보가_같은지_조회(request, feedResponse);
    }


    @DisplayName("회원이 피드를 조회한다.")
    @Test
    void findByIdWithMember() {
        // given
        FeedRequest request = new FeedRequest(
                "My Project",
                Arrays.asList(JAVA.getId(), SPRING.getId()),
                "hello. this is my project",
                Step.PROGRESS.name(),
                true,
                "https://github.com/woowacourse-teams/2021-nolto",
                "",
                null
        );
        String token = 가입된_유저의_토큰을_받는다().getAccessToken();
        ExtractableResponse<Response> saveResponse = 피드를_작성한다(request, token);
        Long feedId = Long.valueOf(saveResponse.header("Location").replace("/feeds/", ""));

        // when
        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .when()
                .get("/feeds/{feedId}", feedId)
                .then()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract();

        // then
        FeedResponse feedResponse = response.as(FeedResponse.class);
        피드_정보가_같은지_조회(request, feedResponse);
    }

    @DisplayName("놀토의 회원이 자신의 피드를 수정한다.")
    @Test
    void update() {
        // given
        FeedRequest request = new FeedRequest(
                "Amazing Project",
                Arrays.asList(JAVA.getId(), SPRING.getId()),
                "This is my Project. follow me if you want my other projects",
                Step.PROGRESS.name(),
                true,
                "https://github.com/woowacourse-teams/2021-nolto",
                "",
                null
        );
        String token = 가입된_유저의_토큰을_받는다().getAccessToken();
        ExtractableResponse<Response> saveResponse = 피드를_작성한다(request, token);
        Long feedId = Long.valueOf(saveResponse.header("Location").replace("/feeds/", ""));

        // when
        ExtractableResponse<Response> updateResponse = given().log().all()
                .auth().oauth2(token)
                .formParam("title", "수정된 프로젝트 제목")
                .formParam("techs", String.valueOf(SPRING.getId()))
                .formParam("techs", String.valueOf(REACT.getId()))
                .formParam("content", "수정된 컨텐츠")
                .formParam("step", Step.COMPLETE)
                .formParam("sos", false)
                .formParam("StorageUrl", "https://github.com/woowacourse-teams/2021-nolto")
                .formParam("DeployedUrl", "https://github.com/woowacourse-teams/2021-nolto")
                .multiPart("thumbnailImage", thumbnail)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .put("/feeds/{feedId}", feedId)
                .then().log().all()
                .extract();

        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("놀토의 회원이 자신의 피드를 삭제한다.")
    @Test
    void delete() {
        // given
        FeedRequest request = new FeedRequest(
                "Amazing Project",
                Arrays.asList(JAVA.getId(), SPRING.getId()),
                "This is my Project. follow me if you want my other projects",
                Step.PROGRESS.name(),
                true,
                "https://github.com/woowacourse-teams/2021-nolto",
                "",
                null
        );
        String token = 가입된_유저의_토큰을_받는다().getAccessToken();
        ExtractableResponse<Response> saveResponse = 피드를_작성한다(request, token);
        Long feedId = Long.valueOf(saveResponse.header("Location").replace("/feeds/", ""));

        // when
        ExtractableResponse<Response> updateResponse = given().log().all()
                .auth().oauth2(token)
                .when()
                .delete("/feeds/{feedId}", feedId)
                .then().log().all()
                .extract();

        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("놀토 회원이 좋아요를 누른다.")
    @Test
    void addLike() {
        // given
        FeedRequest request = new FeedRequest(
                "Amazing Project",
                Arrays.asList(JAVA.getId(), SPRING.getId()),
                "This is my Project. follow me if you want my other projects",
                Step.PROGRESS.name(),
                true,
                "https://github.com/woowacourse-teams/2021-nolto",
                "",
                null
        );
        String token = 가입된_유저의_토큰을_받는다().getAccessToken();
        ExtractableResponse<Response> saveResponse = 피드를_작성한다(request, token);
        Long feedId = Long.valueOf(saveResponse.header("Location").replace("/feeds/", ""));

        // when
        ExtractableResponse<Response> likeResponse = 좋아요를_누른다(token, feedId);

        // then
        assertThat(likeResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("이미 좋아요를 누른 놀토 회원이 좋아요를 다시 누르면 예외가 발생한다.")
    @Test
    void canNotAddLike() {
        // given
        FeedRequest request = new FeedRequest(
                "Amazing Project",
                Arrays.asList(JAVA.getId(), SPRING.getId()),
                "This is my Project. follow me if you want my other projects",
                Step.PROGRESS.name(),
                true,
                "https://github.com/woowacourse-teams/2021-nolto",
                "",
                null
        );
        String token = 가입된_유저의_토큰을_받는다().getAccessToken();
        ExtractableResponse<Response> saveResponse = 피드를_작성한다(request, token);
        Long feedId = Long.valueOf(saveResponse.header("Location").replace("/feeds/", ""));
        좋아요를_누른다(token, feedId);

        // when
        ExtractableResponse<Response> likeResponse = 좋아요를_누른다(token, feedId);

        // then
        ExceptionResponse response = likeResponse.as(ExceptionResponse.class);
        assertThat(response.getErrorCode()).isEqualTo(ALREADY_LIKED.getErrorCode());
        assertThat(response.getMessage()).isEqualTo(ALREADY_LIKED.getMessage());
    }

    @DisplayName("놀토 회원이 좋아요를 취소한다.")
    @Test
    void deleteLike() {
        // given
        FeedRequest request = new FeedRequest(
                "Amazing Project",
                Arrays.asList(JAVA.getId(), SPRING.getId()),
                "This is my Project. follow me if you want my other projects",
                Step.PROGRESS.name(),
                true,
                "https://github.com/woowacourse-teams/2021-nolto",
                "",
                null
        );
        String token = 가입된_유저의_토큰을_받는다().getAccessToken();
        ExtractableResponse<Response> saveResponse = 피드를_작성한다(request, token);
        Long feedId = Long.valueOf(saveResponse.header("Location").replace("/feeds/", ""));
        좋아요를_누른다(token, feedId);

        // when
        ExtractableResponse<Response> likeResponse = given().log().all()
                .auth().oauth2(token)
                .when()
                .post("/feeds/{feedId}/unlike", feedId)
                .then().log().all()
                .extract();

        // then
        assertThat(likeResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("좋아요를 누르지 않은 채로 놀토 회원이 좋아요를 취소하면 예외가 발생한다.")
    @Test
    void canNotDeleteLike() {
        // given
        FeedRequest request = new FeedRequest(
                "Amazing Project",
                Arrays.asList(JAVA.getId(), SPRING.getId()),
                "This is my Project. follow me if you want my other projects",
                Step.PROGRESS.name(),
                true,
                "https://github.com/woowacourse-teams/2021-nolto",
                "",
                null
        );
        String token = 가입된_유저의_토큰을_받는다().getAccessToken();
        ExtractableResponse<Response> saveResponse = 피드를_작성한다(request, token);
        Long feedId = Long.valueOf(saveResponse.header("Location").replace("/feeds/", ""));

        // when
        ExtractableResponse<Response> likeResponse = given().log().all()
                .auth().oauth2(token)
                .when()
                .post("/feeds/{feedId}/unlike", feedId)
                .then().log().all()
                .extract();

        // then
        ExceptionResponse response = likeResponse.body().as(ExceptionResponse.class);
        assertThat(response.getErrorCode()).isEqualTo(NOT_LIKED.getErrorCode());
        assertThat(response.getMessage()).isEqualTo(NOT_LIKED.getMessage());
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
        FeedRequest JAVA_기술가진_피드_요청 = new FeedRequest("제목1", Collections.singletonList(JAVA.getId()),
                "내용1", "PROGRESS", false, "www.github.com/woowacourse", null, null);
        FeedRequest JAVA_AND_SPRING_기술가진_피드_요청 = new FeedRequest("제목1", Arrays.asList(JAVA.getId(), SPRING.getId()),
                "내용1", "PROGRESS", false, "www.github.com/woowacourse", null, null);

        Long JAVA_기술가진_피드_ID = 피드_업로드되어_있음(JAVA_기술가진_피드_요청);
        Long JAVA_AND_SPRING_기술가진_피드_ID = 피드_업로드되어_있음(JAVA_AND_SPRING_기술가진_피드_요청);

        // when
        ExtractableResponse<Response> JAVA_기술_응답 = 기술로_피드_검색_요청(JAVA.getName());
        ExtractableResponse<Response> JAVA_AND_SPRING_기술_응답 = 기술로_피드_검색_요청(JAVA.getName() + "," + SPRING.getName());

        // then
        피드_목록_조회_응답됨(JAVA_기술_응답);
        피드_목록_조회_응답됨(JAVA_AND_SPRING_기술_응답);
        피드_목록_포함됨(JAVA_기술_응답,Arrays.asList(JAVA_기술가진_피드_ID, JAVA_AND_SPRING_기술가진_피드_ID));
        피드_목록_포함됨(JAVA_AND_SPRING_기술_응답, Collections.singletonList(JAVA_AND_SPRING_기술가진_피드_ID));
    }

    @DisplayName("query, techs로 피드를 검색하고 필터링 조건으로도 검색한다.")
    @Test
    void searchResponseWithQueryTech() {
        // given
        String 제목_쿼리 = "title1";
        String 필터링값 = "COMPLETE";

        FeedRequest JAVA_기술가진_피드_요청 = new FeedRequest(제목_쿼리, Collections.singletonList(JAVA.getId()),
                "내용1", "PROGRESS", false, "www.github.com/woowacourse", null, null);
        FeedRequest JAVA_AND_SPRING_기술가진_피드_요청 = new FeedRequest(제목_쿼리, Arrays.asList(JAVA.getId(), SPRING.getId()),
                "내용1", 필터링값, false, "www.github.com/woowacourse", "www.github.com/woowacourse", null);

        Long JAVA_기술가진_피드_ID = 피드_업로드되어_있음(JAVA_기술가진_피드_요청);
        Long JAVA_AND_SPRING_기술가진_피드_ID = 피드_업로드되어_있음(JAVA_AND_SPRING_기술가진_피드_요청);

        // when
        ExtractableResponse<Response> 쿼리와_기술로_피드_검색_응답 = 쿼리와_기술로_피드_검색_요청(제목_쿼리, JAVA.getName());
        ExtractableResponse<Response> 쿼리_기술_필터링값으로_피드_검색_응답 = 쿼리_기술_필터링값으로_피드_검색_요청(제목_쿼리, JAVA.getName() + "," + SPRING.getName(), 필터링값);

        // then
        피드_목록_조회_응답됨(쿼리와_기술로_피드_검색_응답);
        피드_목록_조회_응답됨(쿼리_기술_필터링값으로_피드_검색_응답);
        피드_목록_포함됨(쿼리와_기술로_피드_검색_응답,Arrays.asList(JAVA_기술가진_피드_ID, JAVA_AND_SPRING_기술가진_피드_ID));
        피드_목록_포함됨(쿼리_기술_필터링값으로_피드_검색_응답, Collections.singletonList(JAVA_AND_SPRING_기술가진_피드_ID));

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

    private ExtractableResponse<Response> 좋아요를_누른다(String token, Long feedId) {
        return given().log().all()
                .auth().oauth2(token)
                .when()
                .post("/feeds/{feedId}/like", feedId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 피드를_작성한다(FeedRequest feedRequest, String token) {
        RequestSpecification requestSpecification = given().log().all()
                .auth().oauth2(token)
                .formParam("title", feedRequest.getTitle())
                .formParam("content", feedRequest.getContent())
                .formParam("step", feedRequest.getStep())
                .formParam("sos", feedRequest.isSos())
                .formParam("StorageUrl", feedRequest.getStorageUrl())
                .formParam("DeployedUrl", feedRequest.getDeployedUrl())
                .multiPart("thumbnailImage", thumbnail);

        feedRequest.getTechs().stream()
                .forEach(techId -> requestSpecification.formParam("techs", techId));

        return requestSpecification
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .post("/feeds")
                .then().log().all()
                .extract();
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

    private ExtractableResponse<Response> 최신순_피드_목록_조회_요청(String filter) {
        return given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/recent?filter={filter}", filter)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 인기순_피드_목록_조회_요청() {
        return given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/hot")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 쿼리_기술_필터링값_모두없이_피드_검색_요청() {
        return given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/search")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 쿼리로_피드_검색_요청(String query) {
        return given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/search?query={query}", query)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 기술로_피드_검색_요청(String techs) {
        return given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/search?techs={techs}", techs)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 쿼리와_기술로_피드_검색_요청(String query, String techs) {
        return given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/search?query={query}&techs={techs}", query, techs)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 필터링값으로_피드_검색_요청(String filter) {
        return given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/search?filter={filter}", filter)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 쿼리_기술_필터링값으로_피드_검색_요청(String query, String techs, String filter) {
        return given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/feeds/search?query={query}&techs={techs}&filter={filter}", query, techs, filter)
                .then().log().all()
                .extract();
    }

    public void 피드_목록_조회_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public void 존재하지_않는_필터링값으로_피드_목록_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().jsonPath().getString("errorCode")).isEqualTo("feed-005");
        assertThat(response.body().jsonPath().getString("message")).isEqualTo("지원하지 않는 피드의 필터링 값입니다.");
    }

    public void 피드_목록_포함됨(ExtractableResponse<Response> response, List<Long> expectedLineIds) {
        List<Long> resultLineIds = response.jsonPath().getList(".", FeedCardResponse.class).stream()
                .map(FeedCardResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).isEqualTo(expectedLineIds);
    }

    public Long 피드_업로드되어_있음(FeedRequest request) {
        TokenResponse tokenResponse = 가입된_유저의_토큰을_받는다();
        return Long.valueOf(피드를_작성한다(request, tokenResponse.getAccessToken()).header("Location").replace("/feeds/", ""));
    }

    public void 피드에_좋아요_눌러져있음(TokenResponse tokenResponse, Long feedId) {
        given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/feeds/{feedId}/like", feedId)
                .then().log().all()
                .extract();
    }
}