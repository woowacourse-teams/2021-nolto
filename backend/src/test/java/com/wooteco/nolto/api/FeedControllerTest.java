package com.wooteco.nolto.api;

import com.wooteco.nolto.feed.application.FeedService;
import com.wooteco.nolto.feed.application.LikeService;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.ui.FeedController;
import com.wooteco.nolto.feed.ui.dto.FeedCardResponse;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FeedController.class)
public class FeedControllerTest extends ControllerTest {

    private static final String TOKEN_PAYLOAD = "user@email.com";

    private static final User LOGIN_USER =
            new User(2L, "user2@email.com", "user2", "아마찌", "imageUrl");

    private static final MockMultipartFile MOCK_MULTIPART_FILE =
            new MockMultipartFile("thumbnailImage", "thumbnailImage.png", "image/png", "<<png data>>".getBytes());

    private static final long FEED_ID = 1L;

    private static final Feed FEED1 =
            new Feed(1L, "title1", "content1", Step.PROGRESS, true, "www.surl.com", "www.durl.com", "www.turl.com", 1, LOGIN_USER, new ArrayList<>());

    private static final Feed FEED2 =
            new Feed(2L, "title2", "content2", Step.COMPLETE, false, "", "http://woowa.jofilm.com", "", 2, LOGIN_USER, new ArrayList<>());

    private static final FeedResponse FEED_RESPONSE = FeedResponse.of(LOGIN_USER, FEED1, true);

    private static final List<FeedCardResponse> FEED_CARD_RESPONSES = FeedCardResponse.toList(Arrays.asList(FEED1, FEED2));

    private static final FieldDescriptor[] FEED_CARD_RESPONSE = new FieldDescriptor[]{
            fieldWithPath("author").type(JsonFieldType.OBJECT).description("작성자"),
            fieldWithPath("author.id").type(JsonFieldType.NUMBER).description("작성자 ID"),
            fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
            fieldWithPath("author.imageUrl").type(JsonFieldType.STRING).description("작성자 이미지 URL"),
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("FEED ID"),
            fieldWithPath("title").type(JsonFieldType.STRING).description("FEED 제목"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("FEED 내용"),
            fieldWithPath("step").type(JsonFieldType.STRING).description("FEED 단계"),
            fieldWithPath("sos").type(JsonFieldType.BOOLEAN).description("sos 여부"),
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

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.multipart("/feeds")
                .file("thumbnailImage", MOCK_MULTIPART_FILE.getBytes())
                .param("title", "hello-title")
                .param("techs", "1")
                .param("techs", "2")
                .param("content", "hello-content")
                .param("step", "PROGRESS")
                .param("sos", "true")
                .param("storageUrl", "url.com")
                .param("deployedUrl", "url.com");

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
                                parameterWithName("step").description("단계"),
                                parameterWithName("sos").description("sos 여부"),
                                parameterWithName("storageUrl").description("저장소 URL").optional(),
                                parameterWithName("deployedUrl").description("배포 URL")
                        )
                ));
    }

    @DisplayName("유저 모두 글 상세페이지를 조회할 수 있다.")
    @Test
    void findById() throws Exception {
        given(authService.findUserByToken(TOKEN_PAYLOAD)).willReturn(LOGIN_USER);
        given(feedService.findById(LOGIN_USER, FEED_ID)).willReturn(FEED_RESPONSE);
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

        MockHttpServletRequestBuilder request = post("/feeds/{feedId}/like", FEED_ID);

        mockMvc.perform(request
                .header("Authorization", "Bearer dXNlcjpzZWNyZXQ=")
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(document("feed-addLike",
                        getDocumentRequest(),
                        getDocumentResponse()));
    }


    @DisplayName("피드의 좋아요를 취소한다.")
    @Test
    void deleteLike() throws Exception {
        given(authService.findUserByToken(TOKEN_PAYLOAD)).willReturn(LOGIN_USER);

        MockHttpServletRequestBuilder request = delete("/feeds/{feedId}/like", FEED_ID);

        mockMvc.perform(request
                .header("Authorization", "Bearer dXNlcjpzZWNyZXQ=")
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(document("feed-addLike",
                        getDocumentRequest(),
                        getDocumentResponse()));

    }
}
