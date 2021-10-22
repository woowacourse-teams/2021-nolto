package com.wooteco.nolto.api;

import com.wooteco.nolto.auth.ui.OAuthController;
import com.wooteco.nolto.auth.ui.dto.AllTokenResponse;
import com.wooteco.nolto.auth.ui.dto.OAuthRedirectResponse;
import com.wooteco.nolto.auth.ui.dto.RefreshTokenRequest;
import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import com.wooteco.nolto.exception.BadRequestException;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.UnauthorizedException;
import com.wooteco.nolto.exception.dto.ExceptionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OAuthController.class)
class OAuthControllerTest extends ControllerTest {

    private static final String SOCIAL_TYPE_NAME = "github";
    private static final String CODE = "code";
    private static final long ACCESS_TOKEN_EXPIRES_IN = 7200000;
    private static final long REFRESH_TOKEN_EXPIRES_IN = 1209600000;

    public static final RefreshTokenRequest REFRESH_TOKEN_REQUEST =
            new RefreshTokenRequest("refresh token value", "127.0.0.1");
    private static final OAuthRedirectResponse OAUTH_REDIRECT_RESPONSE =
            new OAuthRedirectResponse("client_id", "redirect_uri", "scope", "response_type");
    private static final AllTokenResponse ALL_TOKEN_RESPONSE =
            new AllTokenResponse(
                    new TokenResponse("access token value", ACCESS_TOKEN_EXPIRES_IN),
                    new TokenResponse("refresh token value", REFRESH_TOKEN_EXPIRES_IN));

    @DisplayName("소셜 로그인을 기능 요청의 code 값을 얻기 위한 파라미터 반환해준다.")
    @Test
    void requestSocialRedirect() throws Exception {
        given(authService.requestSocialRedirect(SOCIAL_TYPE_NAME)).willReturn(OAUTH_REDIRECT_RESPONSE);

        mockMvc.perform(
                        get("/login/oauth/{socialType}", SOCIAL_TYPE_NAME)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(OAUTH_REDIRECT_RESPONSE)))
                .andDo(document("auth-requestSocialRedirect",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("socialType").description("소셜 서비스 이름")
                        ),
                        responseFields(
                                fieldWithPath("client_id").type(JsonFieldType.STRING).description("소셜 로그인 요청 id"),
                                fieldWithPath("scope").type(JsonFieldType.STRING).description("유저 정보 범위"),
                                fieldWithPath("redirect_uri").type(JsonFieldType.STRING).description("Redirect 될 uri 정보"),
                                fieldWithPath("response_type").type(JsonFieldType.STRING).description("응답 타입")
                        )
                ));
    }

    @DisplayName("소셜로그인 측에서 발급한 코드를 받아 소셜로그인을 완료하고, 백엔드 자체의 토큰을 발급한다.")
    @Test
    void oAuthSignIn() throws Exception {
        given(authService.oAuthSignIn(SOCIAL_TYPE_NAME, "code", "127.0.0.1")).willReturn(ALL_TOKEN_RESPONSE);

        mockMvc.perform(
                        get("/login/oauth/{socialType}/token", SOCIAL_TYPE_NAME)
                                .param("code", CODE)
                                .with(request -> {
                                    request.addHeader("x-forwarded-for", "127.0.0.1");
                                    request.setRemoteAddr("127.0.0.1");
                                    return request;
                                })
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ALL_TOKEN_RESPONSE)))
                .andDo(document("auth-oAuthSignIn",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("code").description("소셜 서비스에서 반환해준 code 값")
                        ),
                        pathParameters(
                                parameterWithName("socialType").description("소셜 서비스 이름")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.OBJECT).description("재발급한 액세스 토큰"),
                                fieldWithPath("accessToken.value").type(JsonFieldType.STRING).description("액세스 토큰 값"),
                                fieldWithPath("accessToken.expiredIn").type(JsonFieldType.NUMBER).description("액세스 토큰 만료 시간"),
                                fieldWithPath("refreshToken").type(JsonFieldType.OBJECT).description("재발급한 리프레시 토큰"),
                                fieldWithPath("refreshToken.value").type(JsonFieldType.STRING).description("리프레시 토큰 값"),
                                fieldWithPath("refreshToken.expiredIn").type(JsonFieldType.NUMBER).description("리프레시 토큰 만료 시간")
                        )
                ));
    }

    @DisplayName("리프레시 토큰을 사용해 리프레시 토큰, 액세스 토큰을 재발급한다.")
    @Test
    void refreshToken() throws Exception {
        given(authService.refreshToken(any())).willReturn(ALL_TOKEN_RESPONSE);

        mockMvc.perform(
                        post("/login/oauth/refreshToken")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(REFRESH_TOKEN_REQUEST))
                                .with(servletRequest -> {
                                    servletRequest.setRemoteAddr("127.0.0.1");
                                    return servletRequest;
                                }))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ALL_TOKEN_RESPONSE)))
                .andDo(document("auth-refreshToken",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰 (이후 재사용 X)"),
                                fieldWithPath("clientIP").type(JsonFieldType.STRING).description("요청한 클라이언트의 IP")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.OBJECT).description("재발급한 액세스 토큰"),
                                fieldWithPath("accessToken.value").type(JsonFieldType.STRING).description("액세스 토큰 값"),
                                fieldWithPath("accessToken.expiredIn").type(JsonFieldType.NUMBER).description("액세스 토큰 만료 시간"),
                                fieldWithPath("refreshToken").type(JsonFieldType.OBJECT).description("재발급한 리프레시 토큰"),
                                fieldWithPath("refreshToken.value").type(JsonFieldType.STRING).description("리프레시 토큰 값"),
                                fieldWithPath("refreshToken.expiredIn").type(JsonFieldType.NUMBER).description("리프레시 토큰 만료 시간")
                        )
                ));
    }

    @DisplayName("리프레시 토큰 발급 시 리프레시 토큰 입력이 잘못된 경우 예외처리한다.")
    @Test
    void invalidRefreshToken() throws Exception {
        given(authService.refreshToken(any())).willThrow(new BadRequestException(ErrorType.INVALID_TOKEN));

        mockMvc.perform(
                        post("/login/oauth/refreshToken")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(REFRESH_TOKEN_REQUEST))
                                .with(servletRequest -> {
                                    servletRequest.setRemoteAddr("127.0.0.1");
                                    return servletRequest;
                                }))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(objectMapper.writeValueAsString(ExceptionResponse.of(ErrorType.INVALID_TOKEN))))
                .andDo(document("auth-invalidRefreshToken",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("잘못된 리프레시 토큰"),
                                fieldWithPath("clientIP").type(JsonFieldType.STRING).description("요청한 클라이언트의 IP")
                        ),
                        responseFields(
                                fieldWithPath("errorCode").type(JsonFieldType.STRING).description("에러 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                        )
                ));
    }

    @DisplayName("리프레시 토큰 발급 시 잘못된 클라이언트 IP로 요청된 경우 예외처리한다.")
    @Test
    void invalidClientIP() throws Exception {
        given(authService.refreshToken(any())).willThrow(new UnauthorizedException(ErrorType.INVALID_CLIENT));

        mockMvc.perform(
                        post("/login/oauth/refreshToken")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(REFRESH_TOKEN_REQUEST))
                                .with(servletRequest -> {
                                    servletRequest.setRemoteAddr("127.0.0.1");
                                    return servletRequest;
                                }))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(objectMapper.writeValueAsString(ExceptionResponse.of(ErrorType.INVALID_CLIENT))))
                .andDo(document("auth-invalidClient-RefreshToken",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰. (이후 재사용될 수 없음)"),
                                fieldWithPath("clientIP").type(JsonFieldType.STRING).description("권한이 없는 클라이언트의 IP")
                        ),
                        responseFields(
                                fieldWithPath("errorCode").type(JsonFieldType.STRING).description("에러 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                        )
                ));
    }
}
