package com.wooteco.nolto.api;

import com.wooteco.nolto.feed.application.FeedService;
import com.wooteco.nolto.feed.application.LikeService;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.ui.FeedController;
import com.wooteco.nolto.feed.ui.dto.FeedAuthorResponse;
import com.wooteco.nolto.feed.ui.dto.FeedCardResponse;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.user.domain.User;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FeedController.class)
public class FeedControllerTest extends ControllerTest {

    private static final String TOKEN_PAYLOAD = "user@email.com";

    private static final User LOGIN_USER =
            new User(2L, "11111L", "github", "아마찌", "imageUrl");

    private static final MockMultipartFile MOCK_MULTIPART_FILE =
            new MockMultipartFile("thumbnailImage", "thumbnailImage.png", "image/png", "<<png data>>".getBytes());

    private static final long FEED_ID = 1L;

    private static final Feed FEED1 =
            new Feed(1L, "title1", "content1", Step.PROGRESS, true, "www.surl.com", "www.durl.com", "www.turl.com").writtenBy(LOGIN_USER);

    private static final Feed FEED2 =
            new Feed(2L, "title2", "content2", Step.COMPLETE, false, "", "http://woowa.jofilm.com", "", 2, LOGIN_USER, new ArrayList<>());

    private static final List<FeedCardResponse> FEED_CARD_RESPONSES = FeedCardResponse.toList(Arrays.asList(FEED1, FEED2));

    private static final FeedResponse FEED_RESPONSE = new FeedResponse(FeedAuthorResponse.of(LOGIN_USER), FEED1.getId(), FEED1.getTitle(), TechControllerTest.TECH_RESPONSES,
            FEED1.getContent(), FEED1.getStep().name(), FEED1.isSos(), FEED1.getStorageUrl(), FEED1.getDeployedUrl(),
            FEED1.getThumbnailUrl(), FEED1.getLikes().size(), FEED1.getViews(), true, LocalDateTime.now());

    private static final FieldDescriptor[] TECH = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("기술 ID"),
            fieldWithPath("text").type(JsonFieldType.STRING).description("기술 스택명")};

    private static final FieldDescriptor[] FEED_CARD_RESPONSE = new FieldDescriptor[]{
            fieldWithPath("author").type(JsonFieldType.OBJECT).description("작성자"),
            fieldWithPath("author.id").type(JsonFieldType.NUMBER).description("작성자 ID"),
            fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
            fieldWithPath("author.imageUrl").type(JsonFieldType.STRING).description("작성자 이미지 URL"),
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("피드 ID"),
            fieldWithPath("title").type(JsonFieldType.STRING).description("피드 제목"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("피드 내용"),
            fieldWithPath("step").type(JsonFieldType.STRING).description("프로젝트 단계"),
            fieldWithPath("sos").type(JsonFieldType.BOOLEAN).description("SOS 여부"),
            fieldWithPath("thumbnailUrl").type(JsonFieldType.STRING).description("썸네일 URL")
    };

    @MockBean
    private FeedService feedService;

    @MockBean
    private LikeService likeService;

    @DisplayName("멤버 사용자가 피드를 업로드한다")
    @Test
    void create() throws Exception {
        given(authService.findUserByToken(TOKEN_PAYLOAD)).willReturn(LOGIN_USER);
        given(feedService.create(any(), any())).willReturn(FEED_ID);

        MockHttpServletRequestBuilder request = multipart("/feeds")
                .file("thumbnailImage", MOCK_MULTIPART_FILE.getBytes())
                .param("title", "피드 제목")
                .param("techs", "1")
                .param("techs", "2")
                .param("content", "찰리의 18번 곡은 뭐지. 포모 TMI는 머지. 아마지는 오늘 저녁 머지. 조엘은 머(하)지. 미키미키 루키루키. 지그 재그.")
                .param("step", "PROGRESS")
                .param("sos", "true")
                .param("storageUrl", "https://github.com/woowacourse-teams/2021-nolto.git")
                .param("deployedUrl", "https://nolto.kro.kr");

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/feeds/" + FEED_ID))
                .andDo(document("feed-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(
                                partWithName("thumbnailImage").description("썸네일 이미지")
                        ),
                        requestParameters(
                                parameterWithName("title").description("제목"),
                                parameterWithName("techs").description("기술 스택 목록").optional(),
                                parameterWithName("content").description("내용"),
                                parameterWithName("step").description("프로젝트 단계(조립중, 전시중)"),
                                parameterWithName("sos").description("sos 여부"),
                                parameterWithName("storageUrl").description("저장소 URL").optional(),
                                parameterWithName("deployedUrl").description("배포 URL(전시중일 경우만 필수)").optional()
                        )
                ));
    }

    @DisplayName("유저 모두 글 상세페이지를 조회할 수 있다.")
    @Test
    void findById() throws Exception {
        given(authService.findUserByToken(TOKEN_PAYLOAD)).willReturn(LOGIN_USER);
        given(feedService.findById(any(), any())).willReturn(FEED_RESPONSE);

        mockMvc.perform(
                get("/feeds/{feedId}", FEED_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer optional"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(FEED_RESPONSE)))
                .andDo(document("feed-findById",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("피드 ID")
                        ),
                        responseFields(
                                fieldWithPath("author").type(JsonFieldType.OBJECT).description("작성자"),
                                fieldWithPath("author.id").type(JsonFieldType.NUMBER).description("작성자 ID"),
                                fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
                                fieldWithPath("author.imageUrl").type(JsonFieldType.STRING).description("작성자 이미지 URL"),
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("피드 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("피드 제목"),
                                fieldWithPath("techs").type(JsonFieldType.ARRAY).description("기술 스택 목록"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("피드 내용"),
                                fieldWithPath("step").type(JsonFieldType.STRING).description("프로젝트 단계(조립중, 전시중)"),
                                fieldWithPath("sos").type(JsonFieldType.BOOLEAN).description("SOS 여부"),
                                fieldWithPath("storageUrl").type(JsonFieldType.STRING).description("저장소 URL"),
                                fieldWithPath("deployedUrl").type(JsonFieldType.STRING).description("배포 URL(전시중일 경우만 필수)").optional(),
                                fieldWithPath("thumbnailUrl").type(JsonFieldType.STRING).description("썸네일 URL"),
                                fieldWithPath("likes").type(JsonFieldType.NUMBER).description("좋아요 개수"),
                                fieldWithPath("createdDate").type(JsonFieldType.STRING).description("작성 날짜"),
                                fieldWithPath("views").type(JsonFieldType.NUMBER).description("조회수"),
                                fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("현재 로그인한 유저의 현재 글 좋아요 여부")
                        ).andWithPrefix("techs.[].", TECH)
                ));
    }

    @DisplayName("유저 모두가 선택한 Filter로 최신 피드를 조회할 수 있다.")
    @Test
    void recentResponse() throws Exception {
        String filter = "ALL";
        given(feedService.findAll(filter)).willReturn(FEED_CARD_RESPONSES);

        mockMvc.perform(get("/feeds/recent").param("filter", filter))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(FEED_CARD_RESPONSES)))
                .andDo(document("feed-recentResponse",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("filter").description("필터 조건 (ALL, SOS, PROGRESS, COMPLETE)").optional()
                        ),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY).description("HOT 피드 목록")
                        ).andWithPrefix("[].", FEED_CARD_RESPONSE)));

    }

    @DisplayName("유저 모두가 인기(좋아요)순으로 피드를 조회한다.")
    @Test
    void hotResponse() throws Exception {
        given(feedService.findHotFeeds()).willReturn(FEED_CARD_RESPONSES);

        mockMvc.perform(get("/feeds/hot"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(FEED_CARD_RESPONSES)))
                .andDo(document("feed-hotResponse",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY).description("HOT 피드 목록")
                        ).andWithPrefix("[].", FEED_CARD_RESPONSE)));

    }

    @DisplayName("피드에 좋아요를 누른다.")
    @Test
    void addLike() throws Exception {
        given(authService.findUserByToken(TOKEN_PAYLOAD)).willReturn(LOGIN_USER);

        mockMvc.perform(post("/feeds/{feedId}/like", FEED_ID)
                .header("Authorization", "Bearer dXNlcjpzZWNyZXQ="))
                .andExpect(status().isOk())
                .andDo(document("feed-addLike",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("피드 ID")
                        )));
    }


    @DisplayName("피드의 좋아요를 취소한다.")
    @Test
    void deleteLike() throws Exception {
        given(authService.findUserByToken(TOKEN_PAYLOAD)).willReturn(LOGIN_USER);
        willDoNothing().given(likeService).deleteLike(LOGIN_USER, FEED_ID);

        mockMvc.perform(delete("/feeds/{feedId}/like", FEED_ID)
                .header("Authorization", "Bearer dXNlcjpzZWNyZXQ=")
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(document("feed-deleteLike",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("피드 ID")
                        )));

    }
}
