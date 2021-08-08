package com.wooteco.nolto.acceptance;


import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import com.wooteco.nolto.feed.ui.dto.CommentRequest;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.image.application.ImageKind;
import com.wooteco.nolto.image.application.ImageService;
import com.wooteco.nolto.notification.domain.NotificationType;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.ui.dto.NotificationResponse;
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
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

public class NotificationAcceptanceTest extends AcceptanceTest {

    private Long 엄청난_유저의_1번째_피드_ID;

    private User 좋아요_1개_누를_유저;
    private User 댓글을_남긴_유저;

    private final String defaultImageUrl = "nolto-default-thumbnail.png";
    private final File thumbnail = new File(new File("").getAbsolutePath() + "/src/test/resources/static/" + defaultImageUrl);

    @MockBean
    private ImageService imageService;

    @Autowired
    private TechRepository techRepository;

    @Override
    @BeforeEach
    public void setUp() {
        Tech java = techRepository.save(new Tech("Java"));
        FeedRequest 진행중_단계의_피드_요청 = new FeedRequest("title1", Arrays.asList(java.getId()), "content1", "PROGRESS", false, "www.github.com/woowacourse", null, null);

        BDDMockito.given(imageService.upload(any(MultipartFile.class), any(ImageKind.class))).willReturn("https://dksykemwl00pf.cloudfront.net/" + defaultImageUrl);

        엄청난_유저의_1번째_피드_ID = 피드_업로드되어_있음(진행중_단계의_피드_요청);
        좋아요_1개_누를_유저 = 회원_등록되어_있음(new User("2", SocialType.GITHUB, "찰리", "https://dksykemwl00pf.cloudfront.net/amazzi.jpeg"));
        댓글을_남긴_유저 = 회원_등록되어_있음(new User("3", SocialType.GITHUB, "포모", "https://dksykemwl00pf.cloudfront.net/amazzi.jpeg"));
    }

    @DisplayName("피드에 좋아요를 누르는 경우 좋아요 알림이 저장된다.")
    @Test
    void notifyWhenFeedLike() {
        // given
        String 피드_작성자의_토큰 = 가입된_유저의_토큰을_받는다().getAccessToken();
        String 좋아요를_누를_유저의_토큰 = 가입된_유저의_토큰을_받는다(좋아요_1개_누를_유저).getAccessToken();
        좋아요를_누른다(좋아요를_누를_유저의_토큰, 엄청난_유저의_1번째_피드_ID);

        // when
        ExtractableResponse<Response> response = 작성자의_알림을_조회한다(피드_작성자의_토큰);

        // then
        List<NotificationResponse> notificationResponses = response.jsonPath().getList(".", NotificationResponse.class);
        assertThat(notificationResponses).hasSize(1);
        assertThat(notificationResponses.get(0).getUser().getId()).isEqualTo(좋아요_1개_누를_유저.getId());
        assertThat(notificationResponses.get(0).getFeed().getId()).isEqualTo(엄청난_유저의_1번째_피드_ID);
        assertThat(notificationResponses.get(0).getType()).isEqualTo(NotificationType.LIKE.name());
    }

    @DisplayName("피드에 댓글을 남기는 경우 댓글 알림이 저장된다. (도움을 제안하지 않은 상태)")
    @Test
    void notifyWhenComment() throws JsonProcessingException {
        // given
        String 피드_작성자의_토큰 = 가입된_유저의_토큰을_받는다().getAccessToken();
        String 댓글을_남길_유저의_토큰 = 가입된_유저의_토큰을_받는다(댓글을_남긴_유저).getAccessToken();
        CommentRequest 댓글 = new CommentRequest("댓글 내용", false);
        피드에_댓글을_남긴다(댓글을_남길_유저의_토큰, 엄청난_유저의_1번째_피드_ID, 댓글);

        // when
        ExtractableResponse<Response> response = 작성자의_알림을_조회한다(피드_작성자의_토큰);

        // then
        List<NotificationResponse> notificationResponses = response.jsonPath().getList(".", NotificationResponse.class);
        assertThat(notificationResponses).hasSize(1);
        알림이_같은지_조회한다(notificationResponses.get(0), 댓글을_남긴_유저, 엄청난_유저의_1번째_피드_ID, NotificationType.COMMENT);
    }

    @DisplayName("피드에 댓글을 남기는 경우 도움 알림이 저장된다. (도움을 제안한 상태)")
    @Test
    void notifyWhenCommentWithHelp() throws JsonProcessingException {
        // given
        String 피드_작성자의_토큰 = 가입된_유저의_토큰을_받는다().getAccessToken();
        String 댓글을_남길_유저의_토큰 = 가입된_유저의_토큰을_받는다(댓글을_남긴_유저).getAccessToken();
        CommentRequest 댓글 = new CommentRequest("댓글 내용", true);
        피드에_댓글을_남긴다(댓글을_남길_유저의_토큰, 엄청난_유저의_1번째_피드_ID, 댓글);

        // when
        ExtractableResponse<Response> response = 작성자의_알림을_조회한다(피드_작성자의_토큰);

        // then
        List<NotificationResponse> notificationResponses = response.jsonPath().getList(".", NotificationResponse.class);
        assertThat(notificationResponses).hasSize(1);
        알림이_같은지_조회한다(notificationResponses.get(0), 댓글을_남긴_유저, 엄청난_유저의_1번째_피드_ID, NotificationType.COMMENT_SOS);
    }

    @DisplayName("알림을 단일 삭제한다 (알림 2개 중 1개만 삭제)")
    @Test
    void deleteNotification() throws JsonProcessingException {
        // given
        String 피드_작성자의_토큰 = 가입된_유저의_토큰을_받는다().getAccessToken();

        String 댓글을_남길_유저의_토큰 = 가입된_유저의_토큰을_받는다(댓글을_남긴_유저).getAccessToken();
        CommentRequest 댓글 = new CommentRequest("댓글 내용", true);
        피드에_댓글을_남긴다(댓글을_남길_유저의_토큰, 엄청난_유저의_1번째_피드_ID, 댓글);

        String 좋아요를_누를_유저의_토큰 = 가입된_유저의_토큰을_받는다(좋아요_1개_누를_유저).getAccessToken();
        좋아요를_누른다(좋아요를_누를_유저의_토큰, 엄청난_유저의_1번째_피드_ID);

        // when
        List<NotificationResponse> notificationResponse = 작성자의_알림을_조회한다(피드_작성자의_토큰).jsonPath().getList(".", NotificationResponse.class);
        ExtractableResponse<Response> deleteResponse = given().log().all()
                .auth().oauth2(피드_작성자의_토큰)
                .when()
                .delete("/members/me/notifications/{notificationId}", notificationResponse.get(0).getId())
                .then()
                .log().all()
                .extract();

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        deleteResponse = 작성자의_알림을_조회한다(피드_작성자의_토큰);
        List<NotificationResponse> notificationResponses = deleteResponse.jsonPath().getList(".", NotificationResponse.class);
        assertThat(notificationResponses).hasSize(1);
    }

    @DisplayName("알림을 일괄 삭제한다 (알림 2개 중 2개 모두 삭제)")
    @Test
    void deleteAllNotification() throws JsonProcessingException {
        // given
        String 피드_작성자의_토큰 = 가입된_유저의_토큰을_받는다().getAccessToken();

        String 댓글을_남길_유저의_토큰 = 가입된_유저의_토큰을_받는다(댓글을_남긴_유저).getAccessToken();
        CommentRequest 댓글 = new CommentRequest("댓글 내용", true);
        피드에_댓글을_남긴다(댓글을_남길_유저의_토큰, 엄청난_유저의_1번째_피드_ID, 댓글);

        String 좋아요를_누를_유저의_토큰 = 가입된_유저의_토큰을_받는다(좋아요_1개_누를_유저).getAccessToken();
        좋아요를_누른다(좋아요를_누를_유저의_토큰, 엄청난_유저의_1번째_피드_ID);

        // when
        ExtractableResponse<Response> deleteResponse = given().log().all()
                .auth().oauth2(피드_작성자의_토큰)
                .when()
                .delete("/members/me/notifications")
                .then()
                .log().all()
                .extract();

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        deleteResponse = 작성자의_알림을_조회한다(피드_작성자의_토큰);
        List<NotificationResponse> notificationResponses = deleteResponse.jsonPath().getList(".", NotificationResponse.class);
        assertThat(notificationResponses).hasSize(0);
    }

    private ExtractableResponse<Response> 작성자의_알림을_조회한다(String 피드_작성자의_토큰) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(피드_작성자의_토큰)
                .when()
                .get("/members/me/notifications")
                .then()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract();
    }

    private void 알림이_같은지_조회한다(NotificationResponse response, User publisher, Long feedId, NotificationType type) {
        assertThat(response.getId()).isNotNull();
        assertThat(response.getUser().getId()).isEqualTo(publisher.getId());
        assertThat(response.getFeed().getId()).isEqualTo(feedId);
        assertThat(response.getType()).isEqualTo(type.name());
    }

    private void 피드에_댓글을_남긴다(String token, Long feedId, CommentRequest request) throws JsonProcessingException {
        given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMapper().writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/feeds/{feedId}/comments", feedId)
                .then().log().all()
                .extract();
    }

    public Long 피드_업로드되어_있음(FeedRequest request) {
        TokenResponse tokenResponse = 가입된_유저의_토큰을_받는다();
        return Long.valueOf(피드를_작성한다(request, tokenResponse.getAccessToken()).header("Location").replace("/feeds/", ""));
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

        feedRequest.getTechs()
                .forEach(techId -> requestSpecification.formParam("techs", techId));

        return requestSpecification
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .post("/feeds")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 좋아요를_누른다(String token, Long feedId) {
        return given().log().all()
                .auth().oauth2(token)
                .when()
                .post("/feeds/{feedId}/like", feedId)
                .then().log().all()
                .extract();
    }
}
