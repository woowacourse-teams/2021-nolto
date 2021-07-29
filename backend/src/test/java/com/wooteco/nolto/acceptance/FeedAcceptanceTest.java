package com.wooteco.nolto.acceptance;

import com.wooteco.nolto.exception.dto.ExceptionResponse;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.image.application.ImageService;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import com.wooteco.nolto.tech.ui.dto.TechResponse;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.wooteco.nolto.exception.ErrorType.ALREADY_LIKED;
import static com.wooteco.nolto.exception.ErrorType.NOT_LIKED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;


public class FeedAcceptanceTest extends AcceptanceTest {

    public static final String BEARER = "Bearer ";

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
        BDDMockito.given(imageService.upload(any(MultipartFile.class))).willReturn("https://dksykemwl00pf.cloudfront.net/" + defaultImageUrl);
        techRepository.saveAll(Arrays.asList(JAVA, SPRING, REACT));
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
                .header(HttpHeaders.AUTHORIZATION, token)
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
                .header(HttpHeaders.AUTHORIZATION, BEARER + token)
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
                .header(HttpHeaders.AUTHORIZATION, BEARER + token)
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
                .header(HttpHeaders.AUTHORIZATION, BEARER + token)
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
                .header(HttpHeaders.AUTHORIZATION, BEARER + token)
                .when()
                .post("/feeds/{feedId}/unlike", feedId)
                .then().log().all()
                .extract();

        // then
        ExceptionResponse response = likeResponse.body().as(ExceptionResponse.class);
        assertThat(response.getErrorCode()).isEqualTo(NOT_LIKED.getErrorCode());
        assertThat(response.getMessage()).isEqualTo(NOT_LIKED.getMessage());
    }

    private ExtractableResponse<Response> 좋아요를_누른다(String token, Long feedId) {
        return given().log().all()
                .header(HttpHeaders.AUTHORIZATION, BEARER + token)
                .when()
                .post("/feeds/{feedId}/like", feedId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 피드를_작성한다(FeedRequest feedRequest, String token) {
        RequestSpecification requestSpecification = given().log().all()
                .header(HttpHeaders.AUTHORIZATION, BEARER + token)
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
}