package com.wooteco.nolto.acceptance;

import com.wooteco.nolto.notification.domain.NotificationType;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.ui.dto.NotificationResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static com.wooteco.nolto.UserFixture.찰리_생성;
import static com.wooteco.nolto.UserFixture.포모_생성;
import static com.wooteco.nolto.acceptance.CommentAcceptanceTest.*;
import static com.wooteco.nolto.acceptance.FeedAcceptanceTest.좋아요_요청;
import static com.wooteco.nolto.acceptance.FeedAcceptanceTest.진행중_단계의_피드_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("알림 관련 기능")
class NotificationAcceptanceTest extends AcceptanceTest {

    private String 피드_작성자의_토큰;
    private Long 엄청난_유저의_1번째_피드_ID;

    private User 좋아요_1개_누를_유저;
    private User 댓글을_남긴_유저;

    @BeforeEach
    void setUpOnNotificationAcceptanceTest() {
        super.setUp();

        피드_작성자의_토큰 = 가입된_유저의_토큰을_받는다().getAccessToken().getValue();
        엄청난_유저의_1번째_피드_ID = 피드_업로드되어_있음(진행중_단계의_피드_요청);
        좋아요_1개_누를_유저 = 회원_등록되어_있음(찰리_생성());
        댓글을_남긴_유저 = 회원_등록되어_있음(포모_생성());
    }

    @AfterEach
    void clearOnNotificationAcceptanceTest() {
        super.clear();
    }

    @DisplayName("피드에 좋아요를 누르는 경우 좋아요 알림이 저장된다.")
    @Test
    void notifyWhenFeedLike() {
        // given
        String 좋아요를_누를_유저의_토큰 = 유저의_토큰을_받는다(좋아요_1개_누를_유저).getAccessToken().getValue();
        좋아요_요청(좋아요를_누를_유저의_토큰, 엄청난_유저의_1번째_피드_ID);

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
    void notifyWhenComment() {
        // given
        String 댓글을_남길_유저의_토큰 = 유저의_토큰을_받는다(댓글을_남긴_유저).getAccessToken().getValue();
        댓글_등록되어_있음(일반_댓글_작성요청, 댓글을_남길_유저의_토큰, 엄청난_유저의_1번째_피드_ID);

        // when
        ExtractableResponse<Response> response = 작성자의_알림을_조회한다(피드_작성자의_토큰);

        // then
        List<NotificationResponse> notificationResponses = response.jsonPath().getList(".", NotificationResponse.class);
        assertThat(notificationResponses).hasSize(1);
        알림이_같은지_조회한다(notificationResponses.get(0), 댓글을_남긴_유저, 엄청난_유저의_1번째_피드_ID, NotificationType.COMMENT);
    }

    @DisplayName("피드에 댓글을 남기는 경우 도움 알림이 저장된다. (도움을 제안한 상태)")
    @Test
    void notifyWhenCommentWithHelp() {
        // given
        String 댓글을_남길_유저의_토큰 = 유저의_토큰을_받는다(댓글을_남긴_유저).getAccessToken().getValue();
        댓글_등록되어_있음(도와줄게요_댓글_작성요청, 댓글을_남길_유저의_토큰, 엄청난_유저의_1번째_피드_ID);

        // when
        ExtractableResponse<Response> response = 작성자의_알림을_조회한다(피드_작성자의_토큰);

        // then
        List<NotificationResponse> notificationResponses = response.jsonPath().getList(".", NotificationResponse.class);
        assertThat(notificationResponses).hasSize(1);
        알림이_같은지_조회한다(notificationResponses.get(0), 댓글을_남긴_유저, 엄청난_유저의_1번째_피드_ID, NotificationType.COMMENT_SOS);
    }

    @DisplayName("알림을 단일 삭제한다 (알림 2개 중 1개만 삭제)")
    @Test
    void deleteNotification() {
        // given
        String 댓글을_남길_유저의_토큰 = 유저의_토큰을_받는다(댓글을_남긴_유저).getAccessToken().getValue();
        댓글_등록되어_있음(일반_댓글_작성요청, 댓글을_남길_유저의_토큰, 엄청난_유저의_1번째_피드_ID);

        String 좋아요를_누를_유저의_토큰 = 유저의_토큰을_받는다(좋아요_1개_누를_유저).getAccessToken().getValue();
        좋아요_요청(좋아요를_누를_유저의_토큰, 엄청난_유저의_1번째_피드_ID);

        // when
        List<NotificationResponse> notificationResponse = 작성자의_알림을_조회한다(피드_작성자의_토큰).jsonPath().getList(".", NotificationResponse.class);
        ExtractableResponse<Response> deleteResponse = 알림_단일_삭제_요청(notificationResponse, 피드_작성자의_토큰);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        deleteResponse = 작성자의_알림을_조회한다(피드_작성자의_토큰);
        List<NotificationResponse> notificationResponses = deleteResponse.jsonPath().getList(".", NotificationResponse.class);
        assertThat(notificationResponses).hasSize(1);
    }

    @DisplayName("알림을 일괄 삭제한다 (알림 2개 중 2개 모두 삭제)")
    @Test
    void deleteAllNotification() {
        // given
        String 댓글을_남길_유저의_토큰 = 유저의_토큰을_받는다(댓글을_남긴_유저).getAccessToken().getValue();
        댓글_등록되어_있음(일반_댓글_작성요청, 댓글을_남길_유저의_토큰, 엄청난_유저의_1번째_피드_ID);

        String 좋아요를_누를_유저의_토큰 = 유저의_토큰을_받는다(좋아요_1개_누를_유저).getAccessToken().getValue();
        좋아요_요청(좋아요를_누를_유저의_토큰, 엄청난_유저의_1번째_피드_ID);

        // when
        ExtractableResponse<Response> deleteResponse = 알림_일괄_삭제_요청(피드_작성자의_토큰);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        deleteResponse = 작성자의_알림을_조회한다(피드_작성자의_토큰);
        List<NotificationResponse> notificationResponses = deleteResponse.jsonPath().getList(".", NotificationResponse.class);
        assertThat(notificationResponses).isEmpty();
    }

    private static ExtractableResponse<Response> 작성자의_알림을_조회한다(String 피드_작성자의_토큰) {
        return RestAssured.given().log().all()
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


    private static ExtractableResponse<Response> 알림_단일_삭제_요청(List<NotificationResponse> notificationResponse, String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when()
                .delete("/members/me/notifications/{notificationId}", notificationResponse.get(0).getId())
                .then()
                .log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 알림_일괄_삭제_요청(String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when()
                .delete("/members/me/notifications")
                .then()
                .log().all()
                .extract();
    }
}
