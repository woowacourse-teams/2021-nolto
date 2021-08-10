package com.wooteco.nolto.acceptance;

import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.dto.ExceptionResponse;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.image.application.ImageKind;
import com.wooteco.nolto.image.application.ImageService;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Like;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.ui.dto.*;
import com.wooteco.nolto.user.ui.dto.CommentHistoryResponse;
import com.wooteco.nolto.user.ui.dto.FeedHistoryResponse;
import com.wooteco.nolto.user.ui.dto.MemberHistoryResponse;
import com.wooteco.nolto.user.ui.dto.MemberResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.wooteco.nolto.acceptance.FeedAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@DisplayName("회원 관련 기능")
public class UserAcceptanceTest extends AcceptanceTest {

    private static final ProfileRequest PROFILE_REQUEST = new ProfileRequest(
            "edited nickname",
            "edited bio",
            null
    );

    @MockBean
    private ImageService imageService;

    @BeforeEach
    void setUpOnUserAcceptance() {
        super.setUp();
        BDDMockito.given(imageService.upload(any(MultipartFile.class), any(ImageKind.class))).willReturn(DEFAULT_IMAGE_URL);
        BDDMockito.given(imageService.update(any(String.class), any(MultipartFile.class), any(ImageKind.class))).willReturn(DEFAULT_IMAGE_URL);
    }

    @DisplayName("로그인 된 사용자라면 회원 정보를 받아올 수 있다.")
    @Test
    void getMemberInfoWithToken() {
        //given
        TokenResponse userToken = 가입된_유저의_토큰을_받는다();

        //when
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(userToken);

        //then
        알맞은_회원_정보_조회됨(response, 엄청난_유저);
    }

    @DisplayName("로그인 되지 않은 사용자라면 회원 정보를 받아올 수 없다.")
    @Test
    void cannotGetMemberInfoWithoutToken() {
        //when
        ExtractableResponse<Response> response = 토큰_없이_회원_정보_요청("/members/me");

        //then
        토큰_예외_발생(response, ErrorType.TOKEN_NEEDED);
    }

    @DisplayName("유효하지 않은 토큰을 가진 사용자라면 회원 정보를 받아올 수 없다.")
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
        String token = 가입된_유저의_토큰을_받는다().getAccessToken();
        String 존재하는_유저의_닉네임 = "엄청난 유저";
        String 존재하지_않는_유저의_닉네임 = "엄청나지 않은 유저";

        // when
        ExtractableResponse<Response> isUsableTrueResponse = 닉네임_사용_가능_여부_조회_요청(token, 존재하지_않는_유저의_닉네임);
        ExtractableResponse<Response> isUsableFalseResponse = 닉네임_사용_가능_여부_조회_요청(token, 존재하는_유저의_닉네임);
        NicknameValidationResponse 닉네임_사용_가능_응답 = isUsableTrueResponse.as(NicknameValidationResponse.class);
        NicknameValidationResponse 닉네임_사용_불가능_응답 = isUsableFalseResponse.as(NicknameValidationResponse.class);

        // then
        assertThat(isUsableTrueResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(isUsableFalseResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(닉네임_사용_가능_응답.isIsUsable()).isTrue();
        assertThat(닉네임_사용_불가능_응답.isIsUsable()).isFalse();
    }

    @DisplayName("멤버가 자신의 프로필을 조회한다. 자신이 좋아요를 누른 경우 알림 X")
    @Test
    void findProfileOfMine() {
        // given
        String token = 가입된_유저의_토큰을_받는다(엄청난_유저).getAccessToken();
        Long 업로드된_피드_ID = 피드_업로드되어_있음(진행중_단계의_피드_요청, token);
        피드에_좋아요_눌러져있음(token, 업로드된_피드_ID);

        // when
        ExtractableResponse<Response> response = 프로필_조회_요청(token);

        // then
        프로필_조회_응답됨(response, 엄청난_유저, 0);
    }

    @DisplayName("멤버가 자신의 프로필을 수정한다.")
    @Test
    void updateProfileOfMine() {
        // given
        String token = 가입된_유저의_토큰을_받는다().getAccessToken();

        // when
        ExtractableResponse<Response> response = 프로필_수정_요청(token, PROFILE_REQUEST);

        // then
        프로필_수정_응답됨(response, PROFILE_REQUEST);
    }

    @DisplayName("로그인 된 사용자는 자신의 히스토리(좋아요 한 글, 내가 작성한 글, 내가 남긴 댓글)를 조회할 수 있다.")
    @Test
    void findMemberHistory() {
        //given
        Feed feed = new Feed("title", "content", Step.PROGRESS, true, "https://github.com/nolto", "", "https://cloudfront.net/image.png").writtenBy(엄청난_유저);
        Comment comment = new Comment("comment", true).writtenBy(엄청난_유저, feed);
        엄청난_유저.addLike(new Like(엄청난_유저, feed));
        userRepository.saveAndFlush(엄청난_유저);
        TokenResponse userToken = 가입된_유저의_토큰을_받는다(엄청난_유저);

        //when
        ExtractableResponse<Response> response = 내_히스토리_조회_요청(userToken);

        //then
        회원_히스토리_조회됨(response, 엄청난_유저);
    }

    @DisplayName("로그인 되지 않은 사용자라면 회원의 히스토리를 받아올 수 없다.")
    @Test
    void cannotFindMemberHistoryWithoutToken() {
        //when
        ExtractableResponse<Response> response = 토큰_없이_회원_정보_요청("/members/me/history");

        //then
        토큰_예외_발생(response, ErrorType.TOKEN_NEEDED);
    }

    public ExtractableResponse<Response> 내_회원_정보_조회_요청(TokenResponse tokenResponse) {
        return given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public void 알맞은_회원_정보_조회됨(ExtractableResponse<Response> response, User expectedUser) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getNickname()).isEqualTo(expectedUser.getNickName());
        assertThat(memberResponse.getImageUrl()).isEqualTo(expectedUser.getImageUrl());
    }

    public ExtractableResponse<Response> 토큰_없이_회원_정보_요청(String urlPath) {
        return given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(urlPath)
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 유효하지_않은_토큰으로_회원_정보_요청() {
        return given().log().all()
                .auth().oauth2("유효하지 않은 토큰")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me")
                .then()
                .log().all()
                .extract();
    }

    public void 토큰_예외_발생(ExtractableResponse<Response> response, ErrorType errorType) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(exceptionResponse.getErrorCode()).isEqualTo(errorType.getErrorCode());
        assertThat(exceptionResponse.getMessage()).isEqualTo(errorType.getMessage());
    }

    private ExtractableResponse<Response> 닉네임_사용_가능_여부_조회_요청(String token, String nickName) {
        return given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me/profile/validation?nickname={nickname}", nickName)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 프로필_조회_요청(String token) {
        return given().log().all()
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

    private ExtractableResponse<Response> 프로필_수정_요청(String token, ProfileRequest profileRequest) {
        return given().log().all()
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

    public Long 피드_업로드되어_있음(FeedRequest request, String token) {
        return Long.valueOf(피드를_작성한다(request, token).header("Location").replace("/feeds/", ""));
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

    public void 피드에_좋아요_눌러져있음(String token, Long feedId) {
        given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/feeds/{feedId}/like", feedId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 내_히스토리_조회_요청(TokenResponse tokenResponse) {
        return given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me/history")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private void 회원_히스토리_조회됨(ExtractableResponse<Response> response, User expectedUser) {
        MemberHistoryResponse memberHistoryResponse = response.as(MemberHistoryResponse.class);
        알맞은_피드_조회됨(memberHistoryResponse.getLikedFeeds(), expectedUser.findLikedFeeds());
        알맞은_피드_조회됨(memberHistoryResponse.getMyFeeds(), expectedUser.getFeeds());
        알맞은_댓글_조회됨(memberHistoryResponse.getMyComments(), expectedUser.getComments());
    }

    private void 알맞은_피드_조회됨(List<FeedHistoryResponse> feedHistoryResponses, List<Feed> feeds) {
        List<String> feedTitles = feeds.stream().map(Feed::getTitle).collect(Collectors.toList());
        assertThat(feedHistoryResponses).extracting("title").isEqualTo(feedTitles);
    }

    private void 알맞은_댓글_조회됨(List<CommentHistoryResponse> commentHistoryResponses, List<Comment> comments) {
        List<String> contents = comments.stream().map(Comment::getContent).collect(Collectors.toList());
        assertThat(commentHistoryResponses).extracting("text").isEqualTo(contents);
    }
}
