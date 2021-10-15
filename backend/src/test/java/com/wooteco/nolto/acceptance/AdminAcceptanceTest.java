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

    @DisplayName("어드민 계정의 ID, PW를 통해 어드민 유저의 토큰을 발급받을 수 있다.")
    @Test
    void loginAsAdmin() {
        //when
        AdminLoginRequest 어드민_로그인_양식 = new AdminLoginRequest("test", "test");
        ExtractableResponse<Response> response = 어드민_로그인_요청(어드민_로그인_양식);

        //then
        어드민_로그인_요청_성공(response);
    }

    private ExtractableResponse<Response> 어드민_로그인_요청(AdminLoginRequest 어드민_로그인_양식) {
        return RestAssured.given().log().all()
                .body(어드민_로그인_양식)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/admin/login")
                .then().log().all()
                .extract();
    }

    private void 어드민_로그인_요청_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        AdminLoginResponse adminLoginResponse = response.as(AdminLoginResponse.class);
        assertThat(adminLoginResponse.getAdminAccessToken()).isNotNull();
    }
}
