package com.wooteco.nolto.api;

import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.notification.domain.Notification;
import com.wooteco.nolto.notification.domain.NotificationType;
import com.wooteco.nolto.user.application.MemberService;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.ui.MemberController;
import com.wooteco.nolto.user.ui.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.wooteco.nolto.api.FeedControllerTest.FEED1;
import static com.wooteco.nolto.api.FeedControllerTest.FEED2;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest extends ControllerTest {

    private static final long NOTIFICATIONS = 0L;
    private static final long NOTIFICATION_ID = 1L;

    private static Comment COMMENT1 = new Comment("comment1", true).writtenBy(LOGIN_USER, FEED1);
    private static Comment COMMENT2 = new Comment("comment2", true).writtenBy(LOGIN_USER, FEED2);

    private static final MemberResponse MEMBER_RESPONSE = MemberResponse.of(LOGIN_USER, NOTIFICATIONS);
    private static final NicknameValidationResponse NICKNAME_VALIDATION_RESPONSE = new NicknameValidationResponse(true);
    private static final MockMultipartFile MOCK_MULTIPART_FILE =
            new MockMultipartFile("thumbnailImage", "thumbnailImage.png", "image/png", "<<png data>>".getBytes());
    private static final ProfileRequest PROFILE_REQUEST = new ProfileRequest("????????? ?????????", "????????? ??? ??? ??????", MOCK_MULTIPART_FILE);
    private static final ProfileResponse PROFILE_RESPONSE = ProfileResponse.of(LOGIN_USER, NOTIFICATIONS);

    private static final FieldDescriptor[] FEED_SIMPLE_RESPONSE = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ID"),
            fieldWithPath("title").type(JsonFieldType.STRING).description("?????? ??????"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("?????? ??????"),
            fieldWithPath("step").type(JsonFieldType.STRING).description("???????????? ??????"),
            fieldWithPath("sos").type(JsonFieldType.BOOLEAN).description("SOS ??????"),
            fieldWithPath("thumbnailUrl").type(JsonFieldType.STRING).description("????????? URL")
    };

    private static final FieldDescriptor[] COMMENT_RESPONSE = new FieldDescriptor[]{
            fieldWithPath("text").type(JsonFieldType.STRING).description("?????? ??????")
    };

    private static final FieldDescriptor[] NOTIFICATION_RESPONSE = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ID"),
            fieldWithPath("user").type(JsonFieldType.OBJECT).description("????????? ?????? ??????"),
            fieldWithPath("user.id").type(JsonFieldType.NUMBER).description("????????? ?????? ?????? ID"),
            fieldWithPath("user.nickname").type(JsonFieldType.STRING).description("????????? ?????? ?????? ?????????"),
            fieldWithPath("user.imageUrl").type(JsonFieldType.STRING).description("????????? ?????? ?????? ????????? URL"),
            fieldWithPath("feed").type(JsonFieldType.OBJECT).description("?????? ?????? ????????? ??????(COMMENT, COMMENT_SOS, LIKE) ????????? ????????? ??? ??????(REPLY)"),
            fieldWithPath("feed.id").type(JsonFieldType.NUMBER).description("?????? ?????? ????????? ?????? ID(COMMENT, COMMENT_SOS, LIKE) ????????? ????????? ??? ?????? ID(REPLY)"),
            fieldWithPath("feed.title").type(JsonFieldType.STRING).description("?????? ?????? ????????? ?????? ??????(COMMENT, COMMENT_SOS, LIKE) ????????? ????????? ??? ?????? ??????(REPLY)"),
            fieldWithPath("comment").type(JsonFieldType.OBJECT).optional().description("?????? ?????? ??????(COMMENT, COMMENT_SOS) ?????? ?????? ????????? ??????(REPLY)"),
            fieldWithPath("comment.id").type(JsonFieldType.NUMBER).optional().description("?????? ?????? ?????? ID(COMMENT, COMMENT_SOS) ?????? ?????? ????????? ?????? ID(REPLY)"),
            fieldWithPath("comment.text").type(JsonFieldType.STRING).optional().description("?????? ?????? ?????? ??????(COMMENT, COMMENT_SOS) ?????? ?????? ????????? ?????? ??????(REPLY)"),
            fieldWithPath("type").type(JsonFieldType.STRING).description("?????? ??????(COMMENT, COMMENT_SOS, LIKE, REPLY)")
    };

    @MockBean
    private MemberService memberService;

    @DisplayName("????????? ????????? ????????? ????????????.")
    @Test
    void findMemberOfMine() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        given(memberService.findMemberOfMine(LOGIN_USER)).willReturn(MEMBER_RESPONSE);

        mockMvc.perform(
                        get("/members/me")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(MEMBER_RESPONSE)))
                .andDo(document("member-findMe",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("?????? ????????? ??????"),
                                fieldWithPath("notifications").type(JsonFieldType.NUMBER).description("?????? ???")
                        )
                ));
    }

    @DisplayName("????????? ????????? ?????? ?????? ????????? ????????????.")
    @Test
    void validateDuplicatedNickname() throws Exception {
        String ?????????_????????? = "????????? ?????????";
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        given(memberService.validateDuplicated(?????????_?????????)).willReturn(NICKNAME_VALIDATION_RESPONSE);

        mockMvc.perform(
                        get("/members/me/profile/validation").param("nickname", ?????????_?????????)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(NICKNAME_VALIDATION_RESPONSE)))
                .andDo(document("member-validateDuplicatedNickname",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("nickname").description("??????????????? ?????????").optional()
                        ),
                        responseFields(
                                fieldWithPath("isUsable").type(JsonFieldType.BOOLEAN).description("????????? ?????? ?????? ??????")
                        )
                ));
    }

    @DisplayName("????????? ????????? ???????????? ????????????.")
    @Test
    void findProfileOfMine() throws Exception {
        PROFILE_RESPONSE.setCreatedAt(LocalDateTime.now());
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        given(memberService.findProfile(LOGIN_USER)).willReturn(PROFILE_RESPONSE);

        mockMvc.perform(
                        get("/members/me/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PROFILE_RESPONSE)))
                .andDo(document("member-findProfileOfMine",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("bio").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("?????? ????????? ??????"),
                                fieldWithPath("notifications").type(JsonFieldType.NUMBER).description("?????? ???"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("?????? ??????")
                        )
                ));
    }

    @DisplayName("????????? ???????????? ??????????????????.")
    @Test
    void update() throws Exception {
        PROFILE_RESPONSE.setCreatedAt(LocalDateTime.now());
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        given(memberService.updateProfile(any(User.class), any(ProfileRequest.class))).willReturn(PROFILE_RESPONSE);

        MockHttpServletRequestBuilder request = multipart("/members/me/profile")
                .file("image", MOCK_MULTIPART_FILE.getBytes())
                .param("nickname", "????????? ?????????")
                .param("bio", "????????? ??? ??? ??????")
                .header("Authorization", "Bearer " + ACCESS_TOKEN);

        request.with(req -> {
            req.setMethod("PUT");
            return req;
        });

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PROFILE_RESPONSE)))
                .andDo(document("member-updateProfileOfMine",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(
                                partWithName("image").description("????????? ??? ?????? ?????????")
                        ),
                        requestParameters(
                                parameterWithName("nickname").description("????????? ??? ?????? ?????????"),
                                parameterWithName("bio").description("????????? ??? ?????? ??? ??? ??????").optional()
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("bio").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("?????? ????????? ??????"),
                                fieldWithPath("notifications").type(JsonFieldType.NUMBER).description("?????? ???"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("?????? ??????")
                        )
                ));
    }

    @DisplayName("????????? ????????? ????????????(????????? ??? ???, ?????? ????????? ???, ?????? ?????? ??????)??? ????????????.")
    @Test
    void findHistory() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        MemberHistoryResponse memberHistoryResponse = setMemberHistoryResponse();
        given(memberService.findHistory(LOGIN_USER)).willReturn(memberHistoryResponse);

        mockMvc.perform(
                        get("/members/me/history")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(memberHistoryResponse)))
                .andDo(document("member-findHistory",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("likedFeeds").type(JsonFieldType.ARRAY).description("????????? ?????? ??????"),
                                fieldWithPath("myFeeds").type(JsonFieldType.ARRAY).description("?????? ????????? ??????"),
                                fieldWithPath("myComments").type(JsonFieldType.ARRAY).description("?????? ????????? ??????")
                        ).andWithPrefix("likedFeeds.[].", FEED_SIMPLE_RESPONSE)
                                .andWithPrefix("myFeeds.[].", FEED_SIMPLE_RESPONSE)
                                .andWithPrefix("myComments.[].feed.", FEED_SIMPLE_RESPONSE)
                                .andWithPrefix("myComments.[].", COMMENT_RESPONSE)
                ));
    }

    public MemberHistoryResponse setMemberHistoryResponse() {
        List<Feed> likedFeeds = Arrays.asList(FEED1, FEED2);
        List<Feed> myFeeds = Arrays.asList(FEED1, FEED2);
        List<Comment> myComments = Arrays.asList(COMMENT1, COMMENT2);

        return MemberHistoryResponse.of(likedFeeds, myFeeds, myComments);
    }

    @DisplayName("????????? ????????? ????????? ????????????.")
    @Test
    void findNotifications() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        List<NotificationResponse> notificationResponses = setMemberNotificationResponse();
        given(memberService.findNotifications(LOGIN_USER)).willReturn(notificationResponses);

        mockMvc.perform(
                        get("/members/me/notifications")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(notificationResponses)))
                .andDo(document("member-findNotification",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY).description("?????? ??????")
                        ).andWithPrefix("[].", NOTIFICATION_RESPONSE)
                ));
    }

    public List<NotificationResponse> setMemberNotificationResponse() {
        COMMENT1 = new Comment(1L, "comment1", true).writtenBy(LOGIN_USER, FEED1);
        COMMENT2 = new Comment(2L, "comment2", true).writtenBy(LOGIN_USER, FEED2);
        return NotificationResponse.toList(Arrays.asList(
                new Notification(1L, LOGIN_USER, FEED1, COMMENT1, LOGIN_USER, NotificationType.COMMENT),
                new Notification(2L, LOGIN_USER, FEED1, COMMENT1, LOGIN_USER, NotificationType.REPLY),
                new Notification(3L, LOGIN_USER, FEED2, null, LOGIN_USER, NotificationType.LIKE)));
    }

    @DisplayName("????????? ????????? ?????? ??? ?????? ID??? ???????????? ????????? ????????????.")
    @Test
    void deleteNotification() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);

        mockMvc.perform(
                        delete("/members/me/notifications/{notificationId}", NOTIFICATION_ID)
                                .header("Authorization", BEARER + ACCESS_TOKEN))
                .andExpect(status().isNoContent())
                .andDo(document("member-deleteNotification",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("notificationId").description("????????? ?????? ID")
                        )
                ));
    }

    @DisplayName("????????? ????????? ????????? ?????? ????????????.")
    @Test
    void deleteAllNotifications() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);

        mockMvc.perform(
                        delete("/members/me/notifications")
                                .header("Authorization", BEARER + ACCESS_TOKEN))
                .andExpect(status().isNoContent())
                .andDo(document("member-deleteAllNotifications",
                        getDocumentRequest(),
                        getDocumentResponse()
                ));
    }
}