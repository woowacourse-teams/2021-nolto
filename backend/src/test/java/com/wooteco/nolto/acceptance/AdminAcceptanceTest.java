package com.wooteco.nolto.acceptance;

import com.wooteco.nolto.admin.ui.dto.AdminLoginRequest;
import com.wooteco.nolto.admin.ui.dto.AdminLoginResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("어드민 관련 기능")
class AdminAcceptanceTest extends AcceptanceTest {

    private static final AdminLoginRequest 어드민_로그인_양식 = new AdminLoginRequest("test", "test");

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
        어드민_로그인_요청_실패(response);
    }

    @DisplayName("어드민 유저로 모든 피드를 받아올 수 있다.")
    @Test
    void findAllFeed() {
        //when
        ExtractableResponse<Response> response = 어드민_피드_전체_요청(어드민_토큰_발급());

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private String 어드민_토큰_발급() {
        ExtractableResponse<Response> response = 어드민_로그인_요청(어드민_로그인_양식);
        AdminLoginResponse adminLoginResponse = response.as(AdminLoginResponse.class);
        return adminLoginResponse.getAdminAccessToken();
    }

    private ExtractableResponse<Response> 어드민_로그인_요청(AdminLoginRequest 어드민_로그인_양식) {
        return RestAssured.given().log().all()
                .body(어드민_로그인_양식)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/admin/login")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 어드민_피드_전체_요청(String 어드민_토큰) {
        return RestAssured.given().log().all()
                .when()
                .auth().oauth2(어드민_토큰)
                .get("/admin/feeds")
                .then()
                .log().all()
                .extract();
    }

    private void 어드민_로그인_요청_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        AdminLoginResponse adminLoginResponse = response.as(AdminLoginResponse.class);
        assertThat(adminLoginResponse.getAdminAccessToken()).isNotNull();
    }

    private void 어드민_로그인_요청_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
