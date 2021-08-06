package com.wooteco.nolto.api;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.feed.application.CommentLikeService;
import com.wooteco.nolto.feed.application.CommentService;
import com.wooteco.nolto.feed.ui.CommentController;
import com.wooteco.nolto.feed.ui.dto.*;
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
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
    private static final long COMMENT_ID_2 = 3L;
    private static final long REPLY_ID = 4L;
    private static final long REPLY_ID_2 = 5L;

    private static final AuthorResponse AUTHOR_RESPONSE = AuthorResponse.of(LOGIN_USER);

    private static final CommentResponse COMMENT_RESPONSE = new CommentResponse(COMMENT_ID, "작성된 댓글입니다.",
            false, 0, false, true, LocalDateTime.now(), false, AUTHOR_RESPONSE);

    private static final CommentResponse UPDATED_COMMENT_RESPONSE1 = new CommentResponse(COMMENT_ID, "수정해서 작성된 댓글입니다.",
            true, 0, false, true, LocalDateTime.now(), false, AUTHOR_RESPONSE);

    private static final ReplyResponse REPLY_RESPONSE = new ReplyResponse(REPLY_ID, "작성된 대댓글입니다.", 0, false,
            false, LocalDateTime.now(), false, COMMENT_ID, AUTHOR_RESPONSE);

    private static final ReplyResponse REPLY_RESPONSE2 = new ReplyResponse(REPLY_ID_2, "작성된 대댓글입니다.", 0, false,
            false, LocalDateTime.now(), false, COMMENT_ID, AUTHOR_RESPONSE);

    private static final CommentWithReplyResponse COMMENT_WITH_REPLY_RESPONSE = new CommentWithReplyResponse(COMMENT_ID,
            "첫 번째 댓글", false, 3, true, true, LocalDateTime.now(), false,
            AUTHOR_RESPONSE, Arrays.asList(REPLY_RESPONSE, REPLY_RESPONSE2));

    private static final CommentWithReplyResponse COMMENT_WITH_REPLY_RESPONSE2 = new CommentWithReplyResponse(COMMENT_ID_2,
            "두 번째 댓글", true, 0, false, false, LocalDateTime.now(), false,
            AUTHOR_RESPONSE, new ArrayList<>());

    private static final FieldDescriptor[] SINGLE_COMMENT_WITH_REPLY_RESPONSE = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("댓글 ID"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용"),
            fieldWithPath("helper").type(JsonFieldType.BOOLEAN).description("도와줄게요 여부"),
            fieldWithPath("likes").type(JsonFieldType.NUMBER).description("댓글 좋아요 개수"),
            fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("현재 로그인한 유저의 현재 댓글 좋아요 여부"),
            fieldWithPath("feedAuthor").type(JsonFieldType.BOOLEAN).description("댓글 작성자 확인 여부"),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("댓글 작성 날짜"),
            fieldWithPath("modified").type(JsonFieldType.BOOLEAN).description("댓글 수정 여부"),
            fieldWithPath("author").type(JsonFieldType.OBJECT).description("댓글 작성자"),
            fieldWithPath("author.id").type(JsonFieldType.NUMBER).description("댓글 작성자 ID"),
            fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("댓글 작성자 닉네임"),
            fieldWithPath("author.imageUrl").type(JsonFieldType.STRING).description("댓글 작성자 이미지 URL"),
            fieldWithPath("replies.[]").type(JsonFieldType.ARRAY).description("현재 댓글에 포함된 대댓글 목록"),
            fieldWithPath("replies.[].id").type(JsonFieldType.NUMBER).description("대댓글 ID"),
            fieldWithPath("replies.[].content").type(JsonFieldType.STRING).description("대댓글 내용"),
            fieldWithPath("replies.[].likes").type(JsonFieldType.NUMBER).description("대댓글 좋아요 개수"),
            fieldWithPath("replies.[].liked").type(JsonFieldType.BOOLEAN).description("현재 로그인한 유저의 현재 대댓글 좋아요 여부"),
            fieldWithPath("replies.[].feedAuthor").type(JsonFieldType.BOOLEAN).description("대댓글 작성자 확인 여부"),
            fieldWithPath("replies.[].createdAt").type(JsonFieldType.STRING).description("대댓글 작성 날짜"),
            fieldWithPath("replies.[].modified").type(JsonFieldType.BOOLEAN).description("대댓글 수정 여부"),
            fieldWithPath("replies.[].commentId").type(JsonFieldType.NUMBER).description("대댓글이 가리키는 댓글의 ID"),
            fieldWithPath("replies.[].author").type(JsonFieldType.OBJECT).description("대댓글 작성자"),
            fieldWithPath("replies.[].author.id").type(JsonFieldType.NUMBER).description("대댓글 작성자 ID"),
            fieldWithPath("replies.[].author.nickname").type(JsonFieldType.STRING).description("대댓글 작성자 닉네임"),
            fieldWithPath("replies.[].author.imageUrl").type(JsonFieldType.STRING).description("대댓글 작성자 이미지 URL")
    };

    private static final FieldDescriptor[] SINGLE_REPLY_RESPONSE = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("대댓글 ID"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("대댓글 내용"),
            fieldWithPath("likes").type(JsonFieldType.NUMBER).description("대댓글 좋아요 개수"),
            fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("현재 로그인한 유저의 현재 대댓글 좋아요 여부"),
            fieldWithPath("feedAuthor").type(JsonFieldType.BOOLEAN).description("대댓글 작성자 확인 여부"),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("대댓글 작성 날짜"),
            fieldWithPath("modified").type(JsonFieldType.BOOLEAN).description("대댓글 수정 여부"),
            fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("대댓글이 가리키는 댓글의 ID"),
            fieldWithPath("author").type(JsonFieldType.OBJECT).description("대댓글 작성자"),
            fieldWithPath("author.id").type(JsonFieldType.NUMBER).description("대댓글 작성자 ID"),
            fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("대댓글 작성자 닉네임"),
            fieldWithPath("author.imageUrl").type(JsonFieldType.STRING).description("대댓글 작성자 이미지 URL")
    };

    @MockBean
    private CommentService commentService;

    @MockBean
    private CommentLikeService commentLikeService;


    @DisplayName("유저가 작성한 댓글을 등록한다.")
    @Test
    void createComment() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        given(commentService.createComment(any(User.class), any(Long.class), any(CommentRequest.class))).willReturn(COMMENT_RESPONSE);

        mockMvc.perform(post("/feeds/{feedId}/comments", FEED_ID)
                .header("Authorization", BEARER + ACCESS_TOKEN)
                .content(new ObjectMapper().writeValueAsString(new CommentRequest("작성된 댓글입니다.", false)))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(COMMENT_RESPONSE)))
                .andDo(document("comment-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("피드 ID")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("helper").type(JsonFieldType.BOOLEAN).description("도와줄게요 여부")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("댓글 ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("helper").type(JsonFieldType.BOOLEAN).description("도와줄게요 여부"),
                                fieldWithPath("likes").type(JsonFieldType.NUMBER).description("댓글 좋아요 개수"),
                                fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("현재 로그인한 유저의 현재 댓글 좋아요 여부"),
                                fieldWithPath("feedAuthor").type(JsonFieldType.BOOLEAN).description("댓글 작성자 확인 여부"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("댓글 작성 날짜"),
                                fieldWithPath("modified").type(JsonFieldType.BOOLEAN).description("댓글 수정 여부"),
                                fieldWithPath("author").type(JsonFieldType.OBJECT).description("댓글 작성자"),
                                fieldWithPath("author.id").type(JsonFieldType.NUMBER).description("댓글 작성자 ID"),
                                fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("댓글 작성자 닉네임"),
                                fieldWithPath("author.imageUrl").type(JsonFieldType.STRING).description("댓글 작성자 이미지 URL")
                        )));
    }

    @DisplayName("댓글을 등록했던 유저가 댓글 수정 요청을 보낼 수 있다.")
    @Test
    void updateComment() throws Exception {
        CommentRequest updateRequest = new CommentRequest("수정된 댓글입니다.", true);

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
                                parameterWithName("feedId").description("피드 ID"),
                                parameterWithName("commentId").description("댓글 ID")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("helper").type(JsonFieldType.BOOLEAN).description("도와줄게요 여부")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("댓글 ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정된 댓글 내용"),
                                fieldWithPath("helper").type(JsonFieldType.BOOLEAN).description("수정된 도와줄게요 여부"),
                                fieldWithPath("likes").type(JsonFieldType.NUMBER).description("댓글 좋아요 개수"),
                                fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("현재 로그인한 유저의 현재 댓글 좋아요 여부"),
                                fieldWithPath("feedAuthor").type(JsonFieldType.BOOLEAN).description("댓글 작성자 확인 여부"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("댓글 작성 날짜"),
                                fieldWithPath("modified").type(JsonFieldType.BOOLEAN).description("댓글 수정 여부"),
                                fieldWithPath("author").type(JsonFieldType.OBJECT).description("댓글 작성자"),
                                fieldWithPath("author.id").type(JsonFieldType.NUMBER).description("댓글 작성자 ID"),
                                fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("댓글 작성자 닉네임"),
                                fieldWithPath("author.imageUrl").type(JsonFieldType.STRING).description("댓글 작성자 이미지 URL")
                        )));
    }

    @DisplayName("댓글을 등록했던 유저가 댓글 삭제 요청을 보낼 수 있다.")
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
                                parameterWithName("feedId").description("피드 ID"),
                                parameterWithName("commentId").description("댓글 ID")
                        )));
    }

    @DisplayName("요청한 피드에 대한 댓글 + 대댓글 전체를 조회한다.")
    @Test
    void findAllByFeedId() throws Exception {
        List<CommentWithReplyResponse> responses = Arrays.asList(COMMENT_WITH_REPLY_RESPONSE, COMMENT_WITH_REPLY_RESPONSE2);

        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        given(commentService.findAllByFeedId(any(Long.class), any(User.class))).willReturn(responses);

        mockMvc.perform(
                get("/feeds/{feedId}/comments", FEED_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)))
                .andDo(document("comment-findAllByFeedId",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("피드 ID")
                        ),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY).description("댓글과 대댓글 전체 목록")
                        ).andWithPrefix("[].", SINGLE_COMMENT_WITH_REPLY_RESPONSE))
                );

    }

    @DisplayName("유저가 댓글에 좋아요를 추가한다.")
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
                                parameterWithName("feedId").description("피드 ID"),
                                parameterWithName("commentId").description("댓글 ID")
                        )));
    }

    @DisplayName("유저가 댓글에 좋아요를 취소한다.")
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
                                parameterWithName("feedId").description("피드 ID"),
                                parameterWithName("commentId").description("댓글 ID")
                        )));
    }

    @DisplayName("유저가 작성한 대댓글을 등록한다.")
    @Test
    void createReply() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        given(commentService.createReply(any(User.class), any(Long.class), any(Long.class), any())).willReturn(REPLY_RESPONSE);

        mockMvc.perform(post("/feeds/{feedId}/comments/{commentId}/replies", FEED_ID, COMMENT_ID)
                .header(HttpHeaders.AUTHORIZATION, BEARER + ACCESS_TOKEN)
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
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("대댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("대댓글 ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("대댓글 내용"),
                                fieldWithPath("likes").type(JsonFieldType.NUMBER).description("대댓글 좋아요 개수"),
                                fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("현재 로그인한 유저의 현재 대댓글 좋아요 여부"),
                                fieldWithPath("feedAuthor").type(JsonFieldType.BOOLEAN).description("대댓글 작성자 확인 여부"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("대댓글 작성 날짜"),
                                fieldWithPath("modified").type(JsonFieldType.BOOLEAN).description("대댓글 수정 여부"),
                                fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("대댓글이 가리키는 댓글의 ID"),
                                fieldWithPath("author").type(JsonFieldType.OBJECT).description("대댓글 작성자"),
                                fieldWithPath("author.id").type(JsonFieldType.NUMBER).description("대댓글 작성자 ID"),
                                fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("대댓글 작성자 닉네임"),
                                fieldWithPath("author.imageUrl").type(JsonFieldType.STRING).description("대댓글 작성자 이미지 URL")
                        )));
    }

    @DisplayName("댓글을 등록했던 유저가 대댓글 수정 요청을 보낼 수 있다.")
    @Test
    void updateReply() throws Exception {
        ReplyRequest updateReplyRequest = new ReplyRequest("수정된 대댓글입니다.");
        ReplyResponse updateReplyResponse = new ReplyResponse(REPLY_ID, "수정된 대댓글입니다.", 0, false,
                false, LocalDateTime.now(), true, COMMENT_ID, AuthorResponse.of(LOGIN_USER));

        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        given(commentService.updateReply(any(User.class), any(Long.class), any(Long.class), any(Long.class), any())).willReturn(updateReplyResponse);

        mockMvc.perform(put("/feeds/{feedId}/comments/{commentId}/replies/{replyId}", FEED_ID, COMMENT_ID, REPLY_ID)
                .header(HttpHeaders.AUTHORIZATION, BEARER + ACCESS_TOKEN)
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
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("대댓글 내용")
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
                                fieldWithPath("author").type(JsonFieldType.OBJECT).description("대댓글 작성자"),
                                fieldWithPath("author.id").type(JsonFieldType.NUMBER).description("대댓글 작성자 ID"),
                                fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("대댓글 작성자 닉네임"),
                                fieldWithPath("author.imageUrl").type(JsonFieldType.STRING).description("대댓글 작성자 이미지 URL")
                        )));
    }

    @DisplayName("댓글을 등록했던 유저가 대댓글 삭제 요청을 보낼 수 있다.")
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

    @DisplayName("댓글에 대한 대댓글 전체를 조회한다.")
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
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
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

    @DisplayName("대댓글에 좋아요를 누른다.")
    @Test
    void addReplyLike() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        willDoNothing().given(commentLikeService).addCommentLike(COMMENT_ID, LOGIN_USER);

        mockMvc.perform(post("/feeds/{feedId}/comments/{commentId}/replies/{replyId}/like", FEED_ID, COMMENT_ID, REPLY_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("reply-like",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("피드 ID"),
                                parameterWithName("commentId").description("댓글 ID"),
                                parameterWithName("replyId").description("대댓글 ID")
                        )));
    }

    @DisplayName("대댓글의 좋아요를 취소한다.")
    @Test
    void deleteReplyLike() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        willDoNothing().given(commentLikeService).deleteCommentLike(FEED_ID, LOGIN_USER);

        mockMvc.perform(post("/feeds/{feedId}/comments/{commentId}/replies/{replyId}/unlike", FEED_ID, COMMENT_ID, REPLY_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(document("reply-unlike",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("피드 ID"),
                                parameterWithName("commentId").description("댓글 ID"),
                                parameterWithName("replyId").description("대댓글 ID")
                        )));
    }

}
