package com.wooteco.nolto.api;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.user.application.UserService;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.ui.UserController;
import com.wooteco.nolto.user.ui.dto.MemberHistoryResponse;
import com.wooteco.nolto.user.ui.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Arrays;
import java.util.List;

import static com.wooteco.nolto.api.FeedControllerTest.FEED1;
import static com.wooteco.nolto.api.FeedControllerTest.FEED2;
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

    private static final FieldDescriptor[] FEED_SIMPLE_RESPONSE = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("피드 ID"),
            fieldWithPath("title").type(JsonFieldType.STRING).description("피드 제목"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("피드 내용"),
            fieldWithPath("step").type(JsonFieldType.STRING).description("프로젝트 단계"),
            fieldWithPath("sos").type(JsonFieldType.BOOLEAN).description("SOS 여부"),
            fieldWithPath("thumbnailUrl").type(JsonFieldType.STRING).description("썸네일 URL")
    };

    private static final FieldDescriptor[] COMMENT_RESPONSE = new FieldDescriptor[]{
            fieldWithPath("text").type(JsonFieldType.STRING).description("댓글 내용")
    };

    @MockBean
    public UserService userService;

    private MemberHistoryResponse memberHistoryResponse;

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

    @DisplayName("멤버가 자신의 히스토리(좋아요 한 글, 내가 작성한 글, 내가 남긴 댓글)를 조회한다.")
    @Test
    void findHistory() throws Exception {
        given(authService.findUserByToken("accessToken")).willReturn(LOGIN_USER);
        setMemberHistoryResponse();
        given(userService.findHistory(LOGIN_USER)).willReturn(memberHistoryResponse);

        mockMvc.perform(
                get("/members/me/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer accessToken"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(memberHistoryResponse)))
                .andDo(document("member-findHistory",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("likedFeeds").type(JsonFieldType.ARRAY).description("좋아요 누른 피드"),
                                fieldWithPath("myFeeds").type(JsonFieldType.ARRAY).description("내가 작성한 피드"),
                                fieldWithPath("myComments").type(JsonFieldType.ARRAY).description("내가 작성한 댓글")
                        ).andWithPrefix("likedFeeds.[].", FEED_SIMPLE_RESPONSE)
                                .andWithPrefix("myFeeds.[].", FEED_SIMPLE_RESPONSE)
                                .andWithPrefix("myComments.[].feed.", FEED_SIMPLE_RESPONSE)
                                .andWithPrefix("myComments.[].", COMMENT_RESPONSE)
                ));
    }

    void setMemberHistoryResponse() {
        List<Feed> likedFeeds = Arrays.asList(FEED1, FEED2);
        List<Feed> myFeeds = Arrays.asList(FEED1, FEED2);
        Comment comment1 = new Comment("comment1", true).writtenBy(LOGIN_USER);
        comment1.setFeed(FEED1);
        Comment comment2 = new Comment("comment2", true).writtenBy(LOGIN_USER);
        comment2.setFeed(FEED2);
        List<Comment> myComments = Arrays.asList(comment1, comment2);

        memberHistoryResponse = MemberHistoryResponse.of(likedFeeds, myFeeds, myComments);
    }
}