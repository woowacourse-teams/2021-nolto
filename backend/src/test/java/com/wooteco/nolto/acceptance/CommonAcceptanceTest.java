package com.wooteco.nolto.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

class CommonAcceptanceTest extends AcceptanceTest {

    @DisplayName("존재하지 않는 api로 요청시 커스텀 not found가 응답된다.")
    @Test
    void notFound() {
        // when
        ExtractableResponse<Response> response = 존재하지_않는_api로_요청();

        // then
        존재하지_않는_요청_응답_확인(response);
    }

    private ExtractableResponse<Response> 존재하지_않는_api로_요청() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/")
                .then().log().all()
                .extract();
    }

    private void 존재하지_않는_요청_응답_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.body().jsonPath().getString("errorCode")).isEqualTo("common-003");
        assertThat(response.body().jsonPath().getString("message")).isEqualTo("해당 url을 찾을 수 없습니다.");
    }
}
