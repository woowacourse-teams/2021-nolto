package com.wooteco.nolto.api;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.feed.application.CommentLikeService;
import com.wooteco.nolto.feed.application.CommentService;
import com.wooteco.nolto.feed.ui.CommentController;
import com.wooteco.nolto.feed.ui.dto.AuthorResponse;
import com.wooteco.nolto.feed.ui.dto.ReplyRequest;
import com.wooteco.nolto.feed.ui.dto.ReplyResponse;
import com.wooteco.nolto.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class)
public class CommentControllerTest extends ControllerTest {

    private static final String BEARER = "Bearer ";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final User LOGIN_USER =
            new User(1L, "11111L", SocialType.GITHUB, "유저", "imageUrl");

    private static final long FEED_ID = 1L;
    private static final long COMMENT_ID = 2L;
    private static final long REPLY_ID = 3L;

    private static final ReplyResponse REPLY_RESPONSE = new ReplyResponse(REPLY_ID, "작성된 대댓글입니다.", 0, false,
            false, LocalDateTime.now(), false, COMMENT_ID, AuthorResponse.of(LOGIN_USER));

    @MockBean
    private CommentService commentService;

    @MockBean
    private CommentLikeService commentLikeService;

    private static final FieldDescriptor[] SINGLE_REPLY_RESPONSE = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("대댓글 ID"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("수정된 대댓글 내용"),
            fieldWithPath("likes").type(JsonFieldType.NUMBER).description("대댓글 좋아요 개수"),
            fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("현재 로그인한 유저의 현재 대댓글 좋아요 여부"),
            fieldWithPath("feedAuthor").type(JsonFieldType.BOOLEAN).description("대댓글 작성자 확인 여부"),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("대댓글 작성 날짜"),
            fieldWithPath("modified").type(JsonFieldType.BOOLEAN).description("대댓글 수정 여부"),
            fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("대댓글이 가리키는 댓글의 ID"),
            fieldWithPath("author").type(JsonFieldType.OBJECT).description("댓글 작성자"),
            fieldWithPath("author.id").type(JsonFieldType.NUMBER).description("댓글 작성자 ID"),
            fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("댓글 작성자 닉네임"),
            fieldWithPath("author.imageUrl").type(JsonFieldType.STRING).description("댓글 작성자 이미지 URL")
    };

    @Test
    void createReply() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        given(commentService.createReply(any(User.class), any(Long.class), any(Long.class), any())).willReturn(REPLY_RESPONSE);

        mockMvc.perform(post("/feeds/{feedId}/comments/{commentId}/replies", FEED_ID, COMMENT_ID)
                .header("Authorization", BEARER + ACCESS_TOKEN)
                .content(new ObjectMapper().writeValueAsString(new ReplyRequest("작성된 대댓글입니다.")))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(REPLY_RESPONSE)))
                .andDo(document("reply-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("피드 ID"),
                                parameterWithName("commentId").description("댓글 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("대댓글 ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("대댓글 내"),
                                fieldWithPath("likes").type(JsonFieldType.NUMBER).description("대댓글 좋아요 개수"),
                                fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("현재 로그인한 유저의 현재 대댓글 좋아요 여부"),
                                fieldWithPath("feedAuthor").type(JsonFieldType.BOOLEAN).description("대댓글 작성자 확인 여부"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("대댓글 작성 날짜"),
                                fieldWithPath("modified").type(JsonFieldType.BOOLEAN).description("대댓글 수정 여부"),
                                fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("대댓글이 가리키는 댓글의 ID"),
                                fieldWithPath("author").type(JsonFieldType.OBJECT).description("댓글 작성자"),
                                fieldWithPath("author.id").type(JsonFieldType.NUMBER).description("댓글 작성자 ID"),
                                fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("댓글 작성자 닉네임"),
                                fieldWithPath("author.imageUrl").type(JsonFieldType.STRING).description("댓글 작성자 이미지 URL")
                        )));
    }

    @Test
    void updateReply() throws Exception {
        ReplyRequest updateReplyRequest = new ReplyRequest("수정된 대댓글입니다.");
        ReplyResponse updateReplyResponse = new ReplyResponse(REPLY_ID, "수정된 대댓글입니다.", 0, false,
                false, LocalDateTime.now(), true, COMMENT_ID, AuthorResponse.of(LOGIN_USER));

        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        given(commentService.updateReply(any(User.class), any(Long.class), any(Long.class), any(Long.class), any())).willReturn(updateReplyResponse);

        mockMvc.perform(put("/feeds/{feedId}/comments/{commentId}/replies/{replyId}", FEED_ID, COMMENT_ID, REPLY_ID)
                .header("Authorization", BEARER + ACCESS_TOKEN)
                .content(new ObjectMapper().writeValueAsString(updateReplyRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updateReplyResponse)))
                .andDo(document("reply-update",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("피드 ID"),
                                parameterWithName("commentId").description("댓글 ID"),
                                parameterWithName("replyId").description("대댓글 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("대댓글 ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정된 대댓글 내용"),
                                fieldWithPath("likes").type(JsonFieldType.NUMBER).description("대댓글 좋아요 개수"),
                                fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("현재 로그인한 유저의 현재 대댓글 좋아요 여부"),
                                fieldWithPath("feedAuthor").type(JsonFieldType.BOOLEAN).description("대댓글 작성자 확인 여부"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("대댓글 작성 날짜"),
                                fieldWithPath("modified").type(JsonFieldType.BOOLEAN).description("대댓글 수정 여부"),
                                fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("대댓글이 가리키는 댓글의 ID"),
                                fieldWithPath("author").type(JsonFieldType.OBJECT).description("댓글 작성자"),
                                fieldWithPath("author.id").type(JsonFieldType.NUMBER).description("댓글 작성자 ID"),
                                fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("댓글 작성자 닉네임"),
                                fieldWithPath("author.imageUrl").type(JsonFieldType.STRING).description("댓글 작성자 이미지 URL")
                        )));
    }

    @Test
    void deleteReply() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);

        mockMvc.perform(delete("/feeds/{feedId}/comments/{commentId}/replies/{replyId}", FEED_ID, COMMENT_ID, REPLY_ID)
                .header(HttpHeaders.AUTHORIZATION, BEARER + ACCESS_TOKEN))
                .andExpect(status().isNoContent())
                .andDo(document("reply-delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("피드 ID"),
                                parameterWithName("commentId").description("댓글 ID"),
                                parameterWithName("replyId").description("대댓글 ID")
                        )));
    }

    @Test
    void findAllReplies() throws Exception {
        ReplyResponse firstResponse = new ReplyResponse(REPLY_ID, "처음 작성된 대댓글입니다.", 0, false,
                false, LocalDateTime.now(), false, COMMENT_ID, AuthorResponse.of(LOGIN_USER));
        ReplyResponse secondResponse = new ReplyResponse(REPLY_ID + 1, "두번째 작성된 대댓글입니다.", 0, false,
                false, LocalDateTime.now(), true, COMMENT_ID, AuthorResponse.of(LOGIN_USER));

        List<ReplyResponse> replyResponses = new ArrayList<>();
        replyResponses.add(secondResponse);
        replyResponses.add(firstResponse);

        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        given(commentService.findAllRepliesById(any(User.class), any(Long.class), any())).willReturn(replyResponses);

        mockMvc.perform(
                get("/feeds/{feedId}/comments/{commentId}", FEED_ID, COMMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(replyResponses)))
                .andDo(document("reply-findAllReplies",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("피드 ID"),
                                parameterWithName("commentId").description("댓글 ID")
                        ),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY).description("최신순으로 정렬된 대댓글 목록")
                        ).andWithPrefix("[].", SINGLE_REPLY_RESPONSE)));

    }
}
