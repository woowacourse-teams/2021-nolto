package com.wooteco.nolto.acceptance;

import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import com.wooteco.nolto.exception.dto.ExceptionResponse;
import com.wooteco.nolto.image.application.ImageKind;
import com.wooteco.nolto.image.application.ImageService;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.ui.dto.*;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import static com.wooteco.nolto.acceptance.FeedAcceptanceTest.DEFAULT_IMAGE_URL;
import static com.wooteco.nolto.acceptance.FeedAcceptanceTest.THUMBNAIL_IMAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@DisplayName("회원 관련 기능")
public class UserAcceptanceTest extends AcceptanceTest {

    private final ProfileRequest PROFILE_REQUEST = new ProfileRequest(
            "edited nickname",
            "edited bio",
            null
    );

    @MockBean
    private ImageService imageService;

    @BeforeEach
    void setUpOnUserAcceptance(){
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
        ExtractableResponse<Response> response = 토큰_없이_회원_정보_요청();

        //then
        토큰_필요_예외_발생(response);
    }

    @DisplayName("멤버가 닉네임 사용 여부를 확인한다. - 중복인 경우")
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

    @DisplayName("멤버가 자신의 프로필을 조회한다.")
    @Test
    void findProfileOfMine() {
        // given
        String token = 가입된_유저의_토큰을_받는다(엄청난_유저).getAccessToken();

        // when
        ExtractableResponse<Response> response = 프로필_조회_요청(token);

        // then
        프로필_조회_응답됨(response, 엄청난_유저);
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

    public ExtractableResponse<Response> 토큰_없이_회원_정보_요청() {
        return given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me")
                .then()
                .log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    public void 토큰_필요_예외_발생(ExtractableResponse<Response> response) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getErrorCode()).isEqualTo("auth-002");
        assertThat(exceptionResponse.getMessage()).isEqualTo("토큰이 필요합니다.");
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

    private void 프로필_조회_응답됨(ExtractableResponse<Response> response, User expectUser) {
        ProfileResponse profileResponse = response.as(ProfileResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(profileResponse.getId()).isEqualTo(expectUser.getId());
        assertThat(profileResponse.getNickname()).isEqualTo(expectUser.getNickName());
        assertThat(profileResponse.getBio()).isEqualTo(expectUser.getBio());
        assertThat(profileResponse.getCreatedAt()).isEqualTo(expectUser.getCreatedDate());
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

    private void 알맞은_회원_히스토리_조회됨(ExtractableResponse<Response> response, User expectedUser) {
        MemberHistoryResponse memberHistoryResponse = response.as(MemberHistoryResponse.class);
        assertThat(memberHistoryResponse.getLikedFeeds()).isNotNull();
        assertThat(memberHistoryResponse.getMyFeeds()).isNotNull();
        assertThat(memberHistoryResponse.getMyComments()).isNotNull();
    }
}
