package com.wooteco.nolto.api;

import com.wooteco.nolto.auth.domain.SocialType;
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

    private static final String ACCESS_TOKEN = "accessToken";
    private static final User LOGIN_USER =
            new User(1L, "11111", SocialType.GOOGLE, "아마찌", "imageUrl", "");

    private static final long NOTIFICATIONS = 0L;
    private static final long NOTIFICATION_ID = 1L;

    private static Comment COMMENT1;
    private static Comment COMMENT2;

    private static final MemberResponse MEMBER_RESPONSE = MemberResponse.of(LOGIN_USER, NOTIFICATIONS);
    private static final NicknameValidationResponse NICKNAME_VALIDATION_RESPONSE = new NicknameValidationResponse(true);
    private static final MockMultipartFile MOCK_MULTIPART_FILE =
            new MockMultipartFile("thumbnailImage", "thumbnailImage.png", "image/png", "<<png data>>".getBytes());
    private static final ProfileRequest PROFILE_REQUEST = new ProfileRequest("수정할 닉네임", "수정할 한 줄 소개", MOCK_MULTIPART_FILE);
    private static final ProfileResponse PROFILE_RESPONSE = ProfileResponse.of(LOGIN_USER, NOTIFICATIONS);

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

    private static final FieldDescriptor[] NOTIFICATION_RESPONSE = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("알림 ID"),
            fieldWithPath("user").type(JsonFieldType.OBJECT).description("반응을 보낸 유저"),
            fieldWithPath("user.id").type(JsonFieldType.NUMBER).description("반응을 보낸 유저 ID"),
            fieldWithPath("user.nickname").type(JsonFieldType.STRING).description("반응을 보낸 유저 닉네임"),
            fieldWithPath("user.imageUrl").type(JsonFieldType.STRING).description("반응을 보낸 유저 이미지 URL"),
            fieldWithPath("feed").type(JsonFieldType.OBJECT).description("반응 받은 자신의 피드(COMMENT, COMMENT_SOS, LIKE) 자신이 댓글을 단 피드(REPLY)"),
            fieldWithPath("feed.id").type(JsonFieldType.NUMBER).description("반응 받은 자신의 피드 ID(COMMENT, COMMENT_SOS, LIKE) 자신이 댓글을 단 피드 ID(REPLY)"),
            fieldWithPath("feed.title").type(JsonFieldType.STRING).description("반응 받은 자신의 피드 제목(COMMENT, COMMENT_SOS, LIKE) 자신이 댓글을 단 피드 제목(REPLY)"),
            fieldWithPath("comment").type(JsonFieldType.OBJECT).optional().description("새로 달린 댓글(COMMENT, COMMENT_SOS) 반응 받은 자신의 댓글(REPLY)"),
            fieldWithPath("comment.id").type(JsonFieldType.NUMBER).optional().description("새로 달린 댓글 ID(COMMENT, COMMENT_SOS) 반응 받은 자신의 댓글 ID(REPLY)"),
            fieldWithPath("comment.text").type(JsonFieldType.STRING).optional().description("새로 달린 댓글 내용(COMMENT, COMMENT_SOS) 반응 받은 자신의 댓글 내용(REPLY)"),
            fieldWithPath("type").type(JsonFieldType.STRING).description("반응 타입(COMMENT, COMMENT_SOS, LIKE, REPLY)")
    };

    @MockBean
    private MemberService memberService;

    @DisplayName("멤버가 자신의 정보를 조회한다.")
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
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("멤버 ID"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("멤버 닉네임"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("멤버 이미지 주소"),
                                fieldWithPath("notifications").type(JsonFieldType.NUMBER).description("알림 수")
                        )
                ));
    }

    @DisplayName("멤버가 닉네임 사용 가능 여부를 조회한다.")
    @Test
    void validateDuplicatedNickname() throws Exception {
        String 검증할_닉네임 = "검증할 닉네임";
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        given(memberService.validateDuplicated(검증할_닉네임)).willReturn(NICKNAME_VALIDATION_RESPONSE);

        mockMvc.perform(
                get("/members/me/profile/validation").param("nickname", 검증할_닉네임)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(NICKNAME_VALIDATION_RESPONSE)))
                .andDo(document("member-validateDuplicatedNickname",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("nickname").description("중복체크할 닉네임").optional()
                        ),
                        responseFields(
                                fieldWithPath("isUsable").type(JsonFieldType.BOOLEAN).description("닉네임 사용 가능 여부")
                        )
                ));
    }

    @DisplayName("멤버가 자신의 프로필을 조회한다.")
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
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("멤버 ID"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("멤버 닉네임"),
                                fieldWithPath("bio").type(JsonFieldType.STRING).description("멤버 한줄 소개"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("멤버 이미지 주소"),
                                fieldWithPath("notifications").type(JsonFieldType.NUMBER).description("알림 수"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("가입 날짜")
                        )
                ));
    }

    @DisplayName("멤버의 프로필을 업데이트한다.")
    @Test
    void update() throws Exception {
        PROFILE_RESPONSE.setCreatedAt(LocalDateTime.now());
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        given(memberService.updateProfile(any(User.class), any(ProfileRequest.class))).willReturn(PROFILE_RESPONSE);

        MockHttpServletRequestBuilder request = multipart("/members/me/profile")
                .file("image", MOCK_MULTIPART_FILE.getBytes())
                .param("nickname", "수정할 닉네임")
                .param("bio", "수정할 한 줄 소개")
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
                                partWithName("image").description("수정될 수 있는 이미지")
                        ),
                        requestParameters(
                                parameterWithName("nickname").description("수정될 수 있는 닉네임"),
                                parameterWithName("bio").description("수정될 수 있는 한 줄 소개").optional()
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("멤버 ID"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("멤버 닉네임"),
                                fieldWithPath("bio").type(JsonFieldType.STRING).description("멤버 한줄 소개"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("멤버 이미지 주소"),
                                fieldWithPath("notifications").type(JsonFieldType.NUMBER).description("알림 수"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("가입 날짜")
                        )
                ));
    }

    @DisplayName("멤버가 자신의 히스토리(좋아요 한 글, 내가 작성한 글, 내가 남긴 댓글)를 조회한다.")
    @Test
    void findHistory() throws Exception {
        given(authService.findUserByToken("accessToken")).willReturn(LOGIN_USER);
        MemberHistoryResponse memberHistoryResponse = setMemberHistoryResponse();
        given(memberService.findHistory(LOGIN_USER)).willReturn(memberHistoryResponse);

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

    public MemberHistoryResponse setMemberHistoryResponse() {
        List<Feed> likedFeeds = Arrays.asList(FEED1, FEED2);
        List<Feed> myFeeds = Arrays.asList(FEED1, FEED2);
        COMMENT1 = new Comment("comment1", true).writtenBy(LOGIN_USER, FEED1);
        COMMENT2 = new Comment("comment2", true).writtenBy(LOGIN_USER, FEED2);
        List<Comment> myComments = Arrays.asList(COMMENT1, COMMENT2);

        return MemberHistoryResponse.of(likedFeeds, myFeeds, myComments);
    }

    @DisplayName("멤버가 자신의 알림을 조회한다.")
    @Test
    void findNotifications() throws Exception {
        given(authService.findUserByToken("accessToken")).willReturn(LOGIN_USER);
        List<NotificationResponse> notificationResponses = setMemberNotificationResponse();
        given(memberService.findNotifications(LOGIN_USER)).willReturn(notificationResponses);

        mockMvc.perform(
                get("/members/me/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer accessToken"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(notificationResponses)))
                .andDo(document("member-findNotification",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY).description("알림 목록")
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

    @DisplayName("멤버가 자신의 알림 중 알림 ID에 해당하는 알림을 제거한다.")
    @Test
    void deleteNotification() throws Exception {
        given(authService.findUserByToken("accessToken")).willReturn(LOGIN_USER);

        mockMvc.perform(
                delete("/members/me/notifications/{notificationId}", NOTIFICATION_ID)
                        .header("Authorization", "Bearer accessToken"))
                .andExpect(status().isNoContent())
                .andDo(document("member-deleteNotification",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("notificationId").description("삭제할 알림 ID")
                        )
                ));
    }

    @DisplayName("멤버가 자신의 알림을 모두 제거한다.")
    @Test
    void deleteAllNotifications() throws Exception {
        given(authService.findUserByToken("accessToken")).willReturn(LOGIN_USER);

        mockMvc.perform(
                delete("/members/me/notifications")
                        .header("Authorization", "Bearer accessToken"))
                .andExpect(status().isNoContent())
                .andDo(document("member-deleteAllNotifications",
                        getDocumentRequest(),
                        getDocumentResponse()
                ));
    }
}