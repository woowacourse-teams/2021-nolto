package com.wooteco.nolto.api;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.ui.UserController;
import com.wooteco.nolto.user.ui.dto.MemberResponse;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest extends ControllerTest {
    private static final User LOGIN_USER =
            new User(1L, "11111", SocialType.GOOGLE, "아마찌", "imageUrl");
    private static final MemberResponse MEMBER_RESPONSE = MemberResponse.of(LOGIN_USER);

    @DisplayName("멤버가 자신의 정보를 조회한다.")
    @Test
    void findMemberOfMine() throws Exception {
        given(authService.findUserByToken("accessToken")).willReturn(LOGIN_USER);

        mockMvc.perform(
                get("/members/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer accessToken"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(MEMBER_RESPONSE)))
                .andDo(document("member-findMe",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("멤버 ID"),
                                fieldWithPath("socialType").type(JsonFieldType.STRING).description("멤버 소셜 타입"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING).description("멤버 닉네임"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("멤버 이미지 주소")
                        )
                ));
    }
}