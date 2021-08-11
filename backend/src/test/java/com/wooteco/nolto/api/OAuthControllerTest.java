package com.wooteco.nolto.api;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.auth.ui.OAuthController;
import com.wooteco.nolto.auth.ui.dto.OAuthRedirectResponse;
import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import com.wooteco.nolto.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OAuthController.class)
public class OAuthControllerTest extends ControllerTest {

    private static final OAuthRedirectResponse OAUTH_REDIRECT_RESPONSE = new OAuthRedirectResponse("client_id", "redirect_uri", "scope", "response_type");
    private static final TokenResponse TOKEN_RESPONSE =
            new TokenResponse("eefaccesstoken123");
    private static final User USER = new User("socialId", SocialType.GITHUB, "user", "imageUrl");
    private static final String SOCIAL_TYPE_NAME = "github";
    private static final String CODE = "code";

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
        given(authService.oAuthSignIn(SOCIAL_TYPE_NAME, "code")).willReturn(TOKEN_RESPONSE);

        mockMvc.perform(
                get("/login/oauth/{socialType}/token", SOCIAL_TYPE_NAME).param("code", CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(TOKEN_RESPONSE)))
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
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("액세스 토큰")
                        )
                ));
    }
}
