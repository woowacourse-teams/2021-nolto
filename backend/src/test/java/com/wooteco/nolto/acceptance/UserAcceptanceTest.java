package com.wooteco.nolto.acceptance;

import com.wooteco.nolto.auth.ui.dto.AllTokenResponse;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.dto.ExceptionResponse;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.ui.dto.*;
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

import static com.wooteco.nolto.FeedFixture.DEFAULT_IMAGE_URL;
import static com.wooteco.nolto.acceptance.CommentAcceptanceTest.댓글_등록되어_있음;
import static com.wooteco.nolto.acceptance.CommentAcceptanceTest.일반_댓글_작성요청;
import static com.wooteco.nolto.acceptance.FeedAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("회원 관련 기능")
class UserAcceptanceTest extends AcceptanceTest {

    private static final ProfileRequest 프로필_수정_요청 = new ProfileRequest(
            "edited nickname",
            "edited bio",
            null
    );

    private String 존재하는_유저의_토큰;

    @BeforeEach
    void setUpOnUserAcceptance() {
        super.setUp();
        존재하는_유저의_토큰 = 가입된_유저의_토큰을_받는다().getAccessToken().getValue();
    }

    @AfterEach
    void clearOnUserAcceptance() {
        super.clear();
    }

    @DisplayName("멤버 자신의 정보를 확인한다.")
    @Test
    void getMemberInfoWithToken() {
        //when
        ExtractableResponse<Response> response = 멤버_자신의_정보_조회_요청(존재하는_유저의_토큰);

        //then
        알맞은_회원_정보_조회됨(response, 가입된_유저);
    }

    @DisplayName("게스트라면 회원 정보를 받아올 수 없다.")
    @Test
    void cannotGetMemberInfoWithoutToken() {
        //when
        ExtractableResponse<Response> response = 토큰_없이_회원_정보_요청("/members/me");

        //then
        토큰_예외_발생(response, ErrorType.TOKEN_NEEDED);
    }

    @DisplayName("유효하지 않은 토큰을 가진 멤버라면 회원 정보를 받아올 수 없다.")
    @Test
    void cannotGetMemberInfoWithoutInvalidToken() {
        //when
        ExtractableResponse<Response> response = 유효하지_않은_토큰으로_회원_정보_요청();

        //then
        토큰_예외_발생(response, ErrorType.INVALID_TOKEN);
    }

    @DisplayName("멤버가 닉네임 사용 여부를 확인한다.")
    @Test
    void validateDuplicatedNickname() {
        // given
        String 존재하는_유저의_닉네임 = 가입된_유저.getNickName();
        String 존재하지_않는_유저의_닉네임 = "존재하지_않는 닉네임";

        // when
        ExtractableResponse<Response> isUsableTrueResponse = 닉네임_사용_가능_여부_조회_요청(존재하는_유저의_토큰, 존재하지_않는_유저의_닉네임);
        ExtractableResponse<Response> isUsableFalseResponse = 닉네임_사용_가능_여부_조회_요청(존재하는_유저의_토큰, 존재하는_유저의_닉네임);

        // then
        알맞은_닉네임_사용_가능_여부_응답됨(isUsableTrueResponse, true);
        알맞은_닉네임_사용_가능_여부_응답됨(isUsableFalseResponse, false);
    }

    @DisplayName("멤버가 자신의 프로필을 조회한다. 자신이 좋아요를 누른 경우 알림 X")
    @Test
    void findProfileOfMine() {
        // given
        Long 업로드된_피드_ID = 피드_업로드되어_있음(진행중_단계의_피드_요청, 존재하는_유저의_토큰);
        좋아요_요청(존재하는_유저의_토큰, 업로드된_피드_ID);

        // when
        ExtractableResponse<Response> response = 프로필_조회_요청(존재하는_유저의_토큰);

        // then
        프로필_조회_응답됨(response, 가입된_유저, 0);
    }

    @DisplayName("멤버가 자신의 프로필을 수정한다.")
    @Test
    void updateProfileOfMine() {
        // when
        ExtractableResponse<Response> response = 프로필_수정_요청(존재하는_유저의_토큰, 프로필_수정_요청);

        // then
        프로필_수정_응답됨(response, 프로필_수정_요청);
    }

    @DisplayName("멤버는 자신의 히스토리(좋아요 한 글, 내가 작성한 글, 내가 남긴 댓글)를 조회할 수 있다.")
    @Test
    void findMemberHistory() {
        //given
        Long 작성과_좋아요한_피드_ID = 피드_업로드되어_있음(진행중_단계의_피드_요청, 존재하는_유저의_토큰);
        좋아요_요청(존재하는_유저의_토큰, 작성과_좋아요한_피드_ID);
        String 등록한_댓글_내용 = 댓글_등록되어_있음(일반_댓글_작성요청, 존재하는_유저의_토큰, 작성과_좋아요한_피드_ID).getContent();

        AllTokenResponse userToken = 유저의_토큰을_받는다(가입된_유저);

        //when
        ExtractableResponse<Response> response = 내_히스토리_조회_요청(userToken);

        //then
        멤버_히스토리_조회됨(response, 작성과_좋아요한_피드_ID, 등록한_댓글_내용);
    }

    @DisplayName("게스트라면 회원의 히스토리를 받아올 수 없다.")
    @Test
    void cannotFindMemberHistoryWithoutToken() {
        //when
        ExtractableResponse<Response> response = 토큰_없이_회원_정보_요청("/members/me/history");

        //then
        토큰_예외_발생(response, ErrorType.TOKEN_NEEDED);
    }

    private static ExtractableResponse<Response> 멤버_자신의_정보_조회_요청(String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private void 알맞은_회원_정보_조회됨(ExtractableResponse<Response> response, User expectedUser) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getNickname()).isEqualTo(expectedUser.getNickName());
        assertThat(memberResponse.getImageUrl()).isEqualTo(expectedUser.getImageUrl());
    }

    private static ExtractableResponse<Response> 토큰_없이_회원_정보_요청(String urlPath) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(urlPath)
                .then()
                .log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 유효하지_않은_토큰으로_회원_정보_요청() {
        return RestAssured.given().log().all()
                .auth().oauth2("유효하지 않은 토큰")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me")
                .then()
                .log().all()
                .extract();
    }

    private void 토큰_예외_발생(ExtractableResponse<Response> response, ErrorType errorType) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(exceptionResponse.getErrorCode()).isEqualTo(errorType.getErrorCode());
        assertThat(exceptionResponse.getMessage()).isEqualTo(errorType.getMessage());
    }

    private static ExtractableResponse<Response> 닉네임_사용_가능_여부_조회_요청(String token, String nickName) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me/profile/validation?nickname={nickname}", nickName)
                .then().log().all()
                .extract();
    }

    private void 알맞은_닉네임_사용_가능_여부_응답됨(ExtractableResponse<Response> response, boolean expected) {
        NicknameValidationResponse nicknameResponse = response.as(NicknameValidationResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(nicknameResponse.isIsUsable()).isEqualTo(expected);
    }

    private static ExtractableResponse<Response> 프로필_조회_요청(String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me/profile")
                .then().log().all()
                .extract();
    }

    private void 프로필_조회_응답됨(ExtractableResponse<Response> response, User expectUser, int expectedNotifications) {
        ProfileResponse profileResponse = response.as(ProfileResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(profileResponse.getId()).isEqualTo(expectUser.getId());
        assertThat(profileResponse.getNickname()).isEqualTo(expectUser.getNickName());
        assertThat(profileResponse.getBio()).isEqualTo(expectUser.getBio());
        assertThat(profileResponse.getCreatedAt()).isEqualTo(expectUser.getCreatedDate());
        assertThat(profileResponse.getNotifications()).isEqualTo(expectedNotifications);
    }

    private static ExtractableResponse<Response> 프로필_수정_요청(String token, ProfileRequest profileRequest) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .formParam("nickname", profileRequest.getNickname())
                .formParam("bio", profileRequest.getBio())
                .multiPart("image", THUMBNAIL_IMAGE)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .put("/members/me/profile")
                .then().log().all()
                .extract();
    }

    private void 프로필_수정_응답됨(ExtractableResponse<Response> response, ProfileRequest profileRequest) {
        ProfileResponse profileResponse = response.as(ProfileResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(profileResponse.getNickname()).isEqualTo(profileRequest.getNickname());
        assertThat(profileResponse.getBio()).isEqualTo(profileRequest.getBio());
        assertThat(profileResponse.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
    }

    private static ExtractableResponse<Response> 내_히스토리_조회_요청(AllTokenResponse allTokenResponse) {
        return RestAssured.given().log().all()
                .auth().oauth2(allTokenResponse.getAccessToken().getValue())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me/history")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private void 멤버_히스토리_조회됨(ExtractableResponse<Response> response, Long expectedFeedId, String expectedCommentText) {
        MemberHistoryResponse memberHistoryResponse = response.as(MemberHistoryResponse.class);
        알맞은_피드_조회됨(memberHistoryResponse.getLikedFeeds(), expectedFeedId);
        알맞은_피드_조회됨(memberHistoryResponse.getMyFeeds(), expectedFeedId);
        알맞은_댓글_조회됨(memberHistoryResponse.getMyComments(), expectedCommentText);
    }

    private void 알맞은_피드_조회됨(List<FeedHistoryResponse> feedHistoryResponses, Long expectedFeedId) {
        assertThat(feedHistoryResponses).extracting("id").containsExactly(expectedFeedId);
    }

    private void 알맞은_댓글_조회됨(List<CommentHistoryResponse> commentHistoryResponses, String expectedCommentText) {
        assertThat(commentHistoryResponses).extracting("text").containsExactly(expectedCommentText);
    }
}
