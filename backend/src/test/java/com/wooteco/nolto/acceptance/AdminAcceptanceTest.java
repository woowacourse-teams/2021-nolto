package com.wooteco.nolto.acceptance;

import com.wooteco.nolto.admin.ui.dto.AdminLoginRequest;
import com.wooteco.nolto.admin.ui.dto.AdminLoginResponse;
import com.wooteco.nolto.admin.ui.dto.CommentsByFeedResponse;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.ui.dto.TechResponse;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.admin.ui.dto.UserResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static com.wooteco.nolto.TechFixture.리액트_생성;
import static com.wooteco.nolto.TechFixture.스프링_생성;
import static com.wooteco.nolto.UserFixture.*;
import static com.wooteco.nolto.acceptance.CommentAcceptanceTest.*;
import static com.wooteco.nolto.acceptance.FeedAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("어드민 관련 기능")
class AdminAcceptanceTest extends AcceptanceTest {

    private static final AdminLoginRequest 어드민_로그인_양식 = new AdminLoginRequest("test", "test");

    private Long 첫번째_피드_ID;
    private Long 두번째_피드_ID;
    private Long 세번째_피드_ID;
    private Long 네번째_피드_ID;

    private final User 아마찌 = 아마찌_생성();
    private final User 조엘 = 조엘_생성();
    private final User 찰리 = 찰리_생성();
    private final User 포모 = 포모_생성();

    private final Tech 스프링 = 스프링_생성();
    private final Tech 리액트 = 리액트_생성();

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void setUpOnFeedAcceptance() {
        super.setUp();
        techRepository.saveAll(Arrays.asList(스프링, 리액트));

        첫번째_피드_ID = 피드_업로드되어_있음(진행중_단계의_피드_요청);
        두번째_피드_ID = 피드_업로드되어_있음(전시중_단계의_피드_요청);
        세번째_피드_ID = 피드_업로드되어_있음(진행중_단계의_SOS_피드_요청);
        네번째_피드_ID = 피드_업로드되어_있음(전시중_단계의_SOS_피드_요청);

        회원_등록되어_있음(아마찌);
        회원_등록되어_있음(조엘);
        회원_등록되어_있음(찰리);
        회원_등록되어_있음(포모);

        댓글을_작성한다(일반_댓글_작성요청, 유저의_토큰을_받는다(조엘).getAccessToken().getValue(), 두번째_피드_ID);
        댓글을_작성한다(도와줄게요_댓글_작성요청, 유저의_토큰을_받는다(조엘).getAccessToken().getValue(), 세번째_피드_ID);
        댓글을_작성한다(도와줄게요_댓글_작성요청, 유저의_토큰을_받는다(조엘).getAccessToken().getValue(), 네번째_피드_ID);
    }

    @AfterEach
    public void clear() {
        databaseCleanup.execute();
    }

    @DisplayName("어드민 계정의 ID, PW를 통해 어드민 유저의 토큰을 발급받을 수 있다.")
    @Test
    void loginAsAdmin() {
        //when
        ExtractableResponse<Response> response = 어드민_로그인_요청(어드민_로그인_양식);

        //then
        어드민_로그인_요청_성공(response);
    }

    @DisplayName("어드민 계정의 ID, PW를 틀리면 Unauthorized 예외를 응답받는다.")
    @Test
    void loginAsAdminFail() {
        //when
        ExtractableResponse<Response> response = 어드민_로그인_요청(new AdminLoginRequest("fail", "fail"));

        //then
        어드민_관련_요청_실패(response);
    }

    @DisplayName("어드민 유저로 모든 피드를 받아올 수 있다.")
    @Test
    void findAllFeedAsAdmin() {
        //when
        ExtractableResponse<Response> response = 어드민_피드_전체_조회_요청(어드민_토큰_발급());

        //then
        어드민_피드_조회_응답_받음(response);
    }

    @DisplayName("어드민 유저가 아니라면 모든 피드를 받아오는 요청에 권한 없음 응답이 반환된다")
    @Test
    void findAllFeedNotAdmin() {
        //when
        String userAccessToken = 유저의_토큰을_받는다(조엘).getAccessToken().getValue();
        ExtractableResponse<Response> response = 어드민_피드_전체_조회_요청(userAccessToken);

        //then
        어드민_관련_요청_실패(response);
    }

    @DisplayName("어드민 유저로 피드를 수정할 수 있다.")
    @Test
    void updateFeedAsAdmin() {
        //when
        ExtractableResponse<Response> response = 어드민_피드_수정_요청(어드민_토큰_발급(), 첫번째_피드_ID, 전시중_단계의_SOS_피드_요청);

        //then
        어드민_수정_응답_받음(response);
    }

    @DisplayName("어드민 유저로 피드를 삭제할 수 있다.")
    @Test
    void deleteFeedAsAdmin() {
        //when
        ExtractableResponse<Response> response = 어드민_피드_삭제_요청(어드민_토큰_발급(), 첫번째_피드_ID);

        //then
        어드민_삭제_응답_받음(response);
    }

    @DisplayName("어드민 유저가 아니면 피드를 삭제할 수 없다.")
    @Test
    void deleteFeedAsNotAdmin() {
        //when
        String userAccessToken = 유저의_토큰을_받는다(조엘).getAccessToken().getValue();
        ExtractableResponse<Response> response = 어드민_피드_삭제_요청(userAccessToken, 첫번째_피드_ID);

        //then
        어드민_관련_요청_실패(response);
    }

    @DisplayName("어드민 유저로 현재 가입된 유저들을 받아올 수 있다")
    @Test
    void getUsersAsAdmin() {
        //when
        ExtractableResponse<Response> response = 어드민_유저_전체_조회_요청(어드민_토큰_발급());

        //then
        어드민_유저_조회_응답_받음(response);
    }

    @DisplayName("어드민 유저로 유저를 삭제할 수 있다")
    @Test
    void deleteUserAsAdmin() {
        //when
        ExtractableResponse<Response> response = 어드민_유저_삭제_요청(어드민_토큰_발급(), 조엘.getId());

        //then
        어드민_삭제_응답_받음(response);
    }

    @DisplayName("어드민 유저로 피드 별 댓글을 가져올 수 있다")
    @Test
    void getAllComments() {
        //when
        ExtractableResponse<Response> response = 어드민_댓글_조회_요청(어드민_토큰_발급());

        //then
        어드민_댓글_조회_응답_받음(response);
    }

    @DisplayName("어드민 유저로 댓글을 삭제할 수 있다")
    @Test
    void deleteCommentAsAdmin() {
        //when
        ExtractableResponse<Response> response = 어드민_댓글_삭제_요청(어드민_토큰_발급(), 1L);

        //then
        어드민_삭제_응답_받음(response);
    }

    @DisplayName("어드민 유저로 기술 스택을 모두 조회할 수 있다")
    @Test
    void getAllTechs() {
        //when
        ExtractableResponse<Response> response = 어드민_기술_스택_조회_요청(어드민_토큰_발급());

        //then
        어드민_기술_스택_조회_응답_받음(response);
    }

    @DisplayName("어드민 유저로 기술 스택을 삭제할 수 있다")
    @Test
    void deleteTechAsAdmin() {
        //when
        ExtractableResponse<Response> response = 어드민_기술_스택_삭제_요청(어드민_토큰_발급(), 스프링.getId());

        //then
        어드민_삭제_응답_받음(response);
    }

    @DisplayName("어드민 유저로 기술 스택 삭제 시, 해당 기술 스택을 사용하는 피드에서 삭제된다")
    @Test
    void deleteTechThenFeedTechBeDeletedAsAdmin() {
        //given
        어드민_피드_수정_요청(어드민_토큰_발급(), 첫번째_피드_ID, 전시중_단계의_SOS_피드_요청);
        FeedResponse 기술_스택_삭제_전 = 피드_조회_요청(첫번째_피드_ID).as(FeedResponse.class);
        assertThat(기술_스택_삭제_전.getTechs()).hasSize(2);
        assertThat(기술_스택_삭제_전.getTechs().get(0).getId()).isEqualTo(스프링.getId());
        assertThat(기술_스택_삭제_전.getTechs().get(1).getId()).isEqualTo(리액트.getId());

        //when
        ExtractableResponse<Response> response = 어드민_기술_스택_삭제_요청(어드민_토큰_발급(), 스프링.getId());

        //then
        FeedResponse 기술_스택_삭제_후 = 피드_조회_요청(첫번째_피드_ID).as(FeedResponse.class);
        assertThat(기술_스택_삭제_후.getTechs()).hasSize(1);
        assertThat(기술_스택_삭제_후.getTechs().get(0).getId()).isEqualTo(리액트.getId());
    }

    private ExtractableResponse<Response> 어드민_로그인_요청(AdminLoginRequest 어드민_로그인_양식) {
        return RestAssured.given().log().all()
                .body(어드민_로그인_양식)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/admin/login")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 어드민_피드_전체_조회_요청(String 어드민_토큰) {
        return RestAssured.given().log().all()
                .when()
                .auth().oauth2(어드민_토큰)
                .get("/admin/feeds")
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 어드민_피드_수정_요청(String 어드민_토큰, Long 피드_ID, FeedRequest 피드_요청) {
        return RestAssured.given().log().all()
                .when()
                .auth().oauth2(어드민_토큰)
                .formParam("title", 피드_요청.getTitle())
                .formParam("techs", String.valueOf(스프링.getId()))
                .formParam("techs", String.valueOf(리액트.getId()))
                .formParam("content", 피드_요청.getContent())
                .formParam("step", 피드_요청.getStep())
                .formParam("sos", 피드_요청.isSos())
                .formParam("StorageUrl", 피드_요청.getStorageUrl())
                .formParam("DeployedUrl", 피드_요청.getDeployedUrl())
                .multiPart("thumbnailImage", THUMBNAIL_IMAGE)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .put("/admin/feeds/{feedId}", 피드_ID)
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 어드민_피드_삭제_요청(String 어드민_토큰, Long 피드_ID) {
        return RestAssured.given().log().all()
                .when()
                .auth().oauth2(어드민_토큰)
                .delete("/admin/feeds/{feedId}", 피드_ID)
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 어드민_유저_전체_조회_요청(String 어드민_토큰) {
        return RestAssured.given().log().all()
                .when()
                .auth().oauth2(어드민_토큰)
                .get("/admin/users")
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 어드민_댓글_삭제_요청(String 어드민_토큰, Long 댓글_ID) {
        return RestAssured.given().log().all()
                .when()
                .auth().oauth2(어드민_토큰)
                .delete("/admin/comments/{commentId}", 댓글_ID)
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 어드민_유저_삭제_요청(String 어드민_토큰, Long 유저_ID) {
        return RestAssured.given().log().all()
                .when()
                .auth().oauth2(어드민_토큰)
                .delete("/admin/users/{userId}", 유저_ID)
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 어드민_댓글_조회_요청(String 어드민_토큰) {
        return RestAssured.given().log().all()
                .when()
                .auth().oauth2(어드민_토큰)
                .get("/admin/comments")
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 어드민_기술_스택_조회_요청(String 어드민_토큰) {
        return RestAssured.given().log().all()
                .when()
                .auth().oauth2(어드민_토큰)
                .get("/admin/techs")
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 어드민_기술_스택_삭제_요청(String 어드민_토큰, Long 테크_ID) {
        return RestAssured.given().log().all()
                .when()
                .auth().oauth2(어드민_토큰)
                .delete("/admin/techs/{techId}", 테크_ID)
                .then()
                .log().all()
                .extract();
    }

    private String 어드민_토큰_발급() {
        ExtractableResponse<Response> response = 어드민_로그인_요청(어드민_로그인_양식);
        AdminLoginResponse adminLoginResponse = response.as(AdminLoginResponse.class);
        return adminLoginResponse.getAdminAccessToken();
    }

    private void 어드민_로그인_요청_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        AdminLoginResponse adminLoginResponse = response.as(AdminLoginResponse.class);
        assertThat(adminLoginResponse.getAdminAccessToken()).isNotNull();
    }

    private void 어드민_관련_요청_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 어드민_피드_조회_응답_받음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        FeedResponse[] feedResponses = response.as(FeedResponse[].class);
        assertThat(feedResponses).isNotEmpty();
    }

    private void 어드민_유저_조회_응답_받음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        UserResponse[] userResponses = response.as(UserResponse[].class);
        assertThat(userResponses).isNotEmpty();
    }

    private void 어드민_댓글_조회_응답_받음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        CommentsByFeedResponse[] commentsByFeedResponse = response.as(CommentsByFeedResponse[].class);
        assertThat(commentsByFeedResponse).isNotEmpty();
    }

    private void 어드민_수정_응답_받음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 어드민_기술_스택_조회_응답_받음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        TechResponse[] techResponses = response.as(TechResponse[].class);
        assertThat(techResponses).isNotEmpty();
    }

    private void 어드민_삭제_응답_받음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
