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
    private TechRepository techRepository;  // TODO : techRepository로 할까?
    @MockBean
    private ImageService imageService;

    private Tech JAVA;
    private Tech SPRING;
    private Tech REACT = new Tech("React");


    @Override
    @BeforeEach
    public void setUp() {
        BDDMockito.given(imageService.upload(any(MultipartFile.class))).willReturn(" https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png");
        JAVA = techRepository.save(new Tech("Java"));
        SPRING = techRepository.save(new Tech("Spring"));
    }

    @Test
    void addLike() {

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
        ExtractableResponse<Response> response = 피드_요청_데이터를_저장한다(feedRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private ExtractableResponse<Response> 피드_요청_데이터를_저장한다(FeedRequest feedRequest) {
        return given().log().all()
                .header(HttpHeaders.AUTHORIZATION, BEARER + 가입된_유저의_토큰을_받는다().getAccessToken())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .formParam("title", feedRequest.getTitle())
                .formParam("techs", String.valueOf(JAVA.getId()))
                .formParam("content", feedRequest.getContent())
                .formParam("step", feedRequest.getStep())
                .formParam("sos", feedRequest.isSos())
                .formParam("StorageUrl", feedRequest.getStorageUrl())
                .formParam("DeployedUrl", feedRequest.getDeployedUrl())
                .multiPart("thumbnailImage", thumbnail)
                .when()
                .post("/feeds")
                .then().log().all()
                .extract();
    }
}