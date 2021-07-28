package com.wooteco.nolto.acceptance;

import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.image.application.ImageService;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;


public class FeedAcceptanceTest extends AcceptanceTest {

    public static final String BEARER = "Bearer ";

    private File thumbnail = new File(new File("").getAbsolutePath() + "/src/test/resources/static/nolto-default-thumbnail.png");

    @Autowired
    private TechRepository techRepository;
    @MockBean
    private ImageService imageService;

    private Tech JAVA = new Tech("Java");
    private Tech SPRING = new Tech("Spring");
    private Tech REACT = new Tech("React");

    @Override
    @BeforeEach
    public void setUp() {
        BDDMockito.given(imageService.upload(any(MultipartFile.class))).willReturn(" https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png");
        techRepository.saveAll(Arrays.asList(JAVA, SPRING, REACT));
    }

    /*
    이미지는 놀토의 기본 썸네일을 저장한다.
     */
    @DisplayName("놀토의 회원이 피드를 작성한다.")
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
    void findById() {
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
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/feeds/{feedId}", feedId)
                .then()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract();

        // then
        FeedResponse feedResponse = response.as(FeedResponse.class);
        assertThat(feedResponse.getTitle()).isEqualTo(request.getTitle());
    }

    @DisplayName("놀토의 회원이 기존의 피드를 수정한다.")
    @Test
    void update() {
        // given
        FeedRequest request = new FeedRequest(
                "Olympic Project",
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

    @Test
    void addLike() {

    }

    private ExtractableResponse<Response> 피드를_작성한다(FeedRequest feedRequest, String token) {
        return given().log().all()
                .header(HttpHeaders.AUTHORIZATION, BEARER + token)
                .formParam("title", feedRequest.getTitle())
                .formParam("techs", String.valueOf(JAVA.getId()))
                .formParam("content", feedRequest.getContent())
                .formParam("step", feedRequest.getStep())
                .formParam("sos", feedRequest.isSos())
                .formParam("StorageUrl", feedRequest.getStorageUrl())
                .formParam("DeployedUrl", feedRequest.getDeployedUrl())
                .multiPart("thumbnailImage", thumbnail)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .post("/feeds")
                .then().log().all()
                .extract();
    }
}