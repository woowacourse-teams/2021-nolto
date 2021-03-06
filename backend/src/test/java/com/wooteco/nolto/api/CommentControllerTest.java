package com.wooteco.nolto.api;

import com.wooteco.nolto.feed.application.CommentLikeService;
import com.wooteco.nolto.feed.application.CommentService;
import com.wooteco.nolto.feed.ui.CommentController;
import com.wooteco.nolto.feed.ui.dto.AuthorResponse;
import com.wooteco.nolto.feed.ui.dto.CommentRequest;
import com.wooteco.nolto.feed.ui.dto.CommentResponse;
import com.wooteco.nolto.feed.ui.dto.ReplyResponse;
import com.wooteco.nolto.user.domain.User;
import org.junit.jupiter.api.DisplayName;
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
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest extends ControllerTest {

    private static final long FEED_ID = 1L;
    private static final long COMMENT_ID = 2L;
    private static final long COMMENT_ID_2 = 3L;
    private static final long REPLY_ID = 4L;
    private static final long REPLY_ID_2 = 5L;

    private static final AuthorResponse AUTHOR_RESPONSE = AuthorResponse.of(LOGIN_USER);

    private static final CommentResponse COMMENT_RESPONSE = new CommentResponse(COMMENT_ID, "????????? ???????????????.",
            false, 0, false, true, LocalDateTime.now(), false, AUTHOR_RESPONSE);

    private static final CommentResponse UPDATED_COMMENT_RESPONSE1 = new CommentResponse(COMMENT_ID, "???????????? ????????? ???????????????.",
            true, 0, false, true, LocalDateTime.now(), false, AUTHOR_RESPONSE);

    private static final CommentResponse COMMENT_REPLY_RESPONSE = new CommentResponse(COMMENT_ID, "????????? ??????????????????.",
            false, 0, false, true, LocalDateTime.now(), false, AUTHOR_RESPONSE);

    private static final ReplyResponse REPLY_RESPONSE = new ReplyResponse(REPLY_ID, "????????? ??????????????????.", 0, false,
            false, LocalDateTime.now(), false, COMMENT_ID, AUTHOR_RESPONSE);

    private static final ReplyResponse REPLY_RESPONSE2 = new ReplyResponse(REPLY_ID_2, "????????? ??????????????????.", 0, false,
            false, LocalDateTime.now(), false, COMMENT_ID, AUTHOR_RESPONSE);

    private static final CommentResponse COMMENT_WITH_REPLY_RESPONSE = new CommentResponse(COMMENT_ID,
            "??? ?????? ??????", false, 3, true, true, LocalDateTime.now(), false,
            AUTHOR_RESPONSE);

    private static final CommentResponse COMMENT_WITH_REPLY_RESPONSE2 = new CommentResponse(COMMENT_ID_2,
            "??? ?????? ??????", true, 0, false, false, LocalDateTime.now(), false,
            AUTHOR_RESPONSE);

    private static final FieldDescriptor[] SINGLE_COMMENT_WITH_REPLY_RESPONSE = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ID"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("?????? ??????"),
            fieldWithPath("helper").type(JsonFieldType.BOOLEAN).description("??????????????? ??????"),
            fieldWithPath("likes").type(JsonFieldType.NUMBER).description("?????? ????????? ??????"),
            fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("?????? ???????????? ????????? ?????? ?????? ????????? ??????"),
            fieldWithPath("feedAuthor").type(JsonFieldType.BOOLEAN).description("?????? ????????? ?????? ??????"),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
            fieldWithPath("modified").type(JsonFieldType.BOOLEAN).description("?????? ?????? ??????"),
            fieldWithPath("author").type(JsonFieldType.OBJECT).description("?????? ?????????"),
            fieldWithPath("author.id").type(JsonFieldType.NUMBER).description("?????? ????????? ID"),
            fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("?????? ????????? ?????????"),
            fieldWithPath("author.imageUrl").type(JsonFieldType.STRING).description("?????? ????????? ????????? URL"),
    };

    private static final FieldDescriptor[] SINGLE_REPLY_RESPONSE = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ID"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("????????? ??????"),
            fieldWithPath("likes").type(JsonFieldType.NUMBER).description("????????? ????????? ??????"),
            fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("?????? ???????????? ????????? ?????? ????????? ????????? ??????"),
            fieldWithPath("feedAuthor").type(JsonFieldType.BOOLEAN).description("????????? ????????? ?????? ??????"),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("????????? ?????? ??????"),
            fieldWithPath("modified").type(JsonFieldType.BOOLEAN).description("????????? ?????? ??????"),
            fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("???????????? ???????????? ????????? ID"),
            fieldWithPath("author").type(JsonFieldType.OBJECT).description("????????? ?????????"),
            fieldWithPath("author.id").type(JsonFieldType.NUMBER).description("????????? ????????? ID"),
            fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("????????? ????????? ?????????"),
            fieldWithPath("author.imageUrl").type(JsonFieldType.STRING).description("????????? ????????? ????????? URL")
    };

    @MockBean
    private CommentService commentService;

    @MockBean
    private CommentLikeService commentLikeService;


    @DisplayName("????????? ????????? ????????? ????????????.")
    @Test
    void createComment() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        given(commentService.createComment(any(User.class), any(Long.class), any(CommentRequest.class))).willReturn(COMMENT_RESPONSE);

        mockMvc.perform(post("/feeds/{feedId}/comments", FEED_ID)
                        .header("Authorization", BEARER + ACCESS_TOKEN)
                        .content(new ObjectMapper().writeValueAsString(new CommentRequest("????????? ???????????????.", false)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(COMMENT_RESPONSE)))
                .andDo(document("comment-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("?????? ID")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("helper").type(JsonFieldType.BOOLEAN).description("??????????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("helper").type(JsonFieldType.BOOLEAN).description("??????????????? ??????"),
                                fieldWithPath("likes").type(JsonFieldType.NUMBER).description("?????? ????????? ??????"),
                                fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("?????? ???????????? ????????? ?????? ?????? ????????? ??????"),
                                fieldWithPath("feedAuthor").type(JsonFieldType.BOOLEAN).description("?????? ????????? ?????? ??????"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                fieldWithPath("modified").type(JsonFieldType.BOOLEAN).description("?????? ?????? ??????"),
                                fieldWithPath("author").type(JsonFieldType.OBJECT).description("?????? ?????????"),
                                fieldWithPath("author.id").type(JsonFieldType.NUMBER).description("?????? ????????? ID"),
                                fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("?????? ????????? ?????????"),
                                fieldWithPath("author.imageUrl").type(JsonFieldType.STRING).description("?????? ????????? ????????? URL")
                        )));
    }

    @DisplayName("??????(?????? ?????????)??? ???????????? ????????? ??????(?????? ?????????) ?????? ????????? ?????? ??? ??????.")
    @Test
    void updateComment() throws Exception {
        CommentRequest updateRequest = new CommentRequest("????????? ???????????????.", true);

        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        given(commentService.updateComment(any(Long.class), any(CommentRequest.class), any(User.class))).willReturn(UPDATED_COMMENT_RESPONSE1);

        mockMvc.perform(patch("/feeds/{feedId}/comments/{commentId}", FEED_ID, COMMENT_ID)
                        .header(HttpHeaders.AUTHORIZATION, BEARER + ACCESS_TOKEN)
                        .content(new ObjectMapper().writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(UPDATED_COMMENT_RESPONSE1)))
                .andDo(document("comment-update",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("?????? ID"),
                                parameterWithName("commentId").description("??????(?????? ?????????) ID")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("helper").type(JsonFieldType.BOOLEAN).description("??????????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("??????(?????? ?????????) ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("????????? ??????(?????? ?????????) ??????"),
                                fieldWithPath("helper").type(JsonFieldType.BOOLEAN).description("????????? ??????????????? ??????"),
                                fieldWithPath("likes").type(JsonFieldType.NUMBER).description("??????(?????? ?????????) ????????? ??????"),
                                fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("?????? ???????????? ????????? ?????? ??????(?????? ?????????) ????????? ??????"),
                                fieldWithPath("feedAuthor").type(JsonFieldType.BOOLEAN).description("??????(?????? ?????????) ????????? ?????? ??????"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("??????(?????? ?????????) ?????? ??????"),
                                fieldWithPath("modified").type(JsonFieldType.BOOLEAN).description("??????(?????? ?????????) ?????? ??????"),
                                fieldWithPath("author").type(JsonFieldType.OBJECT).description("??????(?????? ?????????) ?????????"),
                                fieldWithPath("author.id").type(JsonFieldType.NUMBER).description("??????(?????? ?????????) ????????? ID"),
                                fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("??????(?????? ?????????) ????????? ?????????"),
                                fieldWithPath("author.imageUrl").type(JsonFieldType.STRING).description("??????(?????? ?????????) ????????? ????????? URL")
                        )));
    }

    @DisplayName("??????(?????? ?????????)??? ???????????? ????????? ?????? ?????? ????????? ?????? ??? ??????.")
    @Test
    void deleteComment() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);

        mockMvc.perform(delete("/feeds/{feedId}/comments/{commentId}", FEED_ID, COMMENT_ID)
                        .header(HttpHeaders.AUTHORIZATION, BEARER + ACCESS_TOKEN))
                .andExpect(status().isNoContent())
                .andDo(document("comment-delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("?????? ID"),
                                parameterWithName("commentId").description("??????(?????? ?????????) ID")
                        )));
    }

    @DisplayName("????????? ????????? ?????? ?????? ????????? ????????????. ???, ???????????? ?????? ????????? ???????????? ?????????.")
    @Test
    void findAllCommentsByFeedId() throws Exception {
        List<CommentResponse> responses = Arrays.asList(COMMENT_WITH_REPLY_RESPONSE, COMMENT_WITH_REPLY_RESPONSE2);

        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        given(commentService.findAllByFeedId(any(Long.class), any(User.class))).willReturn(responses);

        mockMvc.perform(
                        get("/feeds/{feedId}/comments", FEED_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)))
                .andDo(document("comment-findAllCommentsByFeedId",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("?????? ID")
                        ),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY).description("????????? ?????? ?????? ?????? ??????(????????? ?????????)")
                        ).andWithPrefix("[].", SINGLE_COMMENT_WITH_REPLY_RESPONSE))
                );

    }

    @DisplayName("????????? ??????(?????? ?????????)??? ???????????? ????????????.")
    @Test
    void addCommentLike() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);

        mockMvc.perform(post("/feeds/{feedId}/comments/{commentId}/like", FEED_ID, COMMENT_ID)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("comment-addLike",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("?????? ID"),
                                parameterWithName("commentId").description("?????? ID")
                        )));
    }

    @DisplayName("????????? ??????(?????? ?????????)??? ???????????? ????????????.")
    @Test
    void deleteCommentLike() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);

        mockMvc.perform(post("/feeds/{feedId}/comments/{commentId}/unlike", FEED_ID, COMMENT_ID)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("comment-deleteLike",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("?????? ID"),
                                parameterWithName("commentId").description("?????? ID")
                        )));
    }

    @DisplayName("????????? ????????? ???????????? ????????????.")
    @Test
    void createReply() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        given(commentService.createReply(any(User.class), any(Long.class), any(Long.class), any())).willReturn(COMMENT_REPLY_RESPONSE);

        mockMvc.perform(post("/feeds/{feedId}/comments/{commentId}/replies", FEED_ID, COMMENT_ID)
                        .header(HttpHeaders.AUTHORIZATION, BEARER + ACCESS_TOKEN)
                        .content(new ObjectMapper().writeValueAsString(new CommentRequest("????????? ??????????????????.", false)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(COMMENT_REPLY_RESPONSE)))
                .andDo(document("reply-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("?????? ID"),
                                parameterWithName("commentId").description("?????? ID")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("????????? ??????"),
                                fieldWithPath("helper").type(JsonFieldType.BOOLEAN).description("??????????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("????????? ??????"),
                                fieldWithPath("helper").type(JsonFieldType.BOOLEAN).description("??????????????? ??????"),
                                fieldWithPath("likes").type(JsonFieldType.NUMBER).description("????????? ????????? ??????"),
                                fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("?????? ???????????? ????????? ?????? ????????? ????????? ??????"),
                                fieldWithPath("feedAuthor").type(JsonFieldType.BOOLEAN).description("????????? ????????? ?????? ??????"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("????????? ?????? ??????"),
                                fieldWithPath("modified").type(JsonFieldType.BOOLEAN).description("????????? ?????? ??????"),
                                fieldWithPath("author").type(JsonFieldType.OBJECT).description("????????? ?????????"),
                                fieldWithPath("author.id").type(JsonFieldType.NUMBER).description("????????? ????????? ID"),
                                fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("????????? ????????? ?????????"),
                                fieldWithPath("author.imageUrl").type(JsonFieldType.STRING).description("????????? ????????? ????????? URL")
                        )));
    }

    @DisplayName("????????? ?????? ????????? ????????? ????????????.")
    @Test
    void findAllReplies() throws Exception {
        ReplyResponse firstResponse = new ReplyResponse(REPLY_ID, "?????? ????????? ??????????????????.", 0, false,
                false, LocalDateTime.now(), false, COMMENT_ID, AuthorResponse.of(LOGIN_USER));
        ReplyResponse secondResponse = new ReplyResponse(REPLY_ID + 1, "????????? ????????? ??????????????????.", 0, false,
                false, LocalDateTime.now(), true, COMMENT_ID, AuthorResponse.of(LOGIN_USER));

        List<ReplyResponse> replyResponses = new ArrayList<>();
        replyResponses.add(secondResponse);
        replyResponses.add(firstResponse);

        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        given(commentService.findAllRepliesById(any(User.class), any(Long.class), any())).willReturn(replyResponses);

        mockMvc.perform(
                        get("/feeds/{feedId}/comments/{commentId}/replies", FEED_ID, COMMENT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(replyResponses)))
                .andDo(document("reply-findAllReplies",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("?????? ID"),
                                parameterWithName("commentId").description("?????? ID")
                        ),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY).description("??????????????? ????????? ????????? ??????")
                        ).andWithPrefix("[].", SINGLE_REPLY_RESPONSE)));

    }
}
