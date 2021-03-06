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

    @DisplayName("?????? ???????????? ?????? ????????? code ?????? ?????? ?????? ???????????? ???????????????.")
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
                                parameterWithName("socialType").description("?????? ????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("client_id").type(JsonFieldType.STRING).description("?????? ????????? ?????? id"),
                                fieldWithPath("scope").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                fieldWithPath("redirect_uri").type(JsonFieldType.STRING).description("Redirect ??? uri ??????"),
                                fieldWithPath("response_type").type(JsonFieldType.STRING).description("?????? ??????")
                        )
                ));
    }

    @DisplayName("??????????????? ????????? ????????? ????????? ?????? ?????????????????? ????????????, ????????? ????????? ????????? ????????????.")
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
                                parameterWithName("code").description("?????? ??????????????? ???????????? code ???")
                        ),
                        pathParameters(
                                parameterWithName("socialType").description("?????? ????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.OBJECT).description("???????????? ????????? ??????"),
                                fieldWithPath("accessToken.value").type(JsonFieldType.STRING).description("????????? ?????? ???"),
                                fieldWithPath("accessToken.expiredIn").type(JsonFieldType.NUMBER).description("????????? ?????? ?????? ??????"),
                                fieldWithPath("refreshToken").type(JsonFieldType.OBJECT).description("???????????? ???????????? ??????"),
                                fieldWithPath("refreshToken.value").type(JsonFieldType.STRING).description("???????????? ?????? ???"),
                                fieldWithPath("refreshToken.expiredIn").type(JsonFieldType.NUMBER).description("???????????? ?????? ?????? ??????")
                        )
                ));
    }

    @DisplayName("???????????? ????????? ????????? ???????????? ??????, ????????? ????????? ???????????????.")
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
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("???????????? ?????? (?????? ????????? X)"),
                                fieldWithPath("clientIP").type(JsonFieldType.STRING).description("????????? ?????????????????? IP")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.OBJECT).description("???????????? ????????? ??????"),
                                fieldWithPath("accessToken.value").type(JsonFieldType.STRING).description("????????? ?????? ???"),
                                fieldWithPath("accessToken.expiredIn").type(JsonFieldType.NUMBER).description("????????? ?????? ?????? ??????"),
                                fieldWithPath("refreshToken").type(JsonFieldType.OBJECT).description("???????????? ???????????? ??????"),
                                fieldWithPath("refreshToken.value").type(JsonFieldType.STRING).description("???????????? ?????? ???"),
                                fieldWithPath("refreshToken.expiredIn").type(JsonFieldType.NUMBER).description("???????????? ?????? ?????? ??????")
                        )
                ));
    }

    @DisplayName("???????????? ?????? ?????? ??? ???????????? ?????? ????????? ????????? ?????? ??????????????????.")
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
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("????????? ???????????? ??????"),
                                fieldWithPath("clientIP").type(JsonFieldType.STRING).description("????????? ?????????????????? IP")
                        ),
                        responseFields(
                                fieldWithPath("errorCode").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????")
                        )
                ));
    }

    @DisplayName("???????????? ?????? ?????? ??? ????????? ??????????????? IP??? ????????? ?????? ??????????????????.")
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
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("???????????? ??????. (?????? ???????????? ??? ??????)"),
                                fieldWithPath("clientIP").type(JsonFieldType.STRING).description("????????? ?????? ?????????????????? IP")
                        ),
                        responseFields(
                                fieldWithPath("errorCode").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????")
                        )
                ));
    }
}
