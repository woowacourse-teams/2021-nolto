package com.wooteco.nolto.api;

import com.wooteco.nolto.auth.ui.AuthController;
import com.wooteco.nolto.auth.ui.dto.TokenRequest;
import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest extends ControllerTest {

    public static TokenRequest TOKEN_REQUEST = new TokenRequest("user@email.com", "password");
    public static TokenResponse TOKEN_RESPONSE = new TokenResponse("accessToken");

    @DisplayName("이메일과 비밀번호가 요청으로 오면, 토큰을 발급해준다.")
    @Test
    void login() throws Exception {
        given(authService.login(any(TokenRequest.class))).willReturn(TOKEN_RESPONSE);

        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(TOKEN_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(TOKEN_RESPONSE)))
                .andDo(document("auth-login",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("발급된 토큰")
                        )
                ));
    }
}
