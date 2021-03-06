package com.wooteco.nolto.api;

import com.wooteco.nolto.ViewHistoryManager;
import com.wooteco.nolto.feed.application.FeedService;
import com.wooteco.nolto.feed.application.LikeService;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.ui.FeedController;
import com.wooteco.nolto.feed.ui.dto.*;
import com.wooteco.nolto.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
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
class FeedControllerTest extends ControllerTest {

    @MockBean
    private ViewHistoryManager historyManager;

    private static final MockMultipartFile MOCK_MULTIPART_FILE =
            new MockMultipartFile("thumbnailImage", "thumbnailImage.png", "image/png", "<<png data>>".getBytes());
    private static final long FEED_ID = 1L;

    public static final Feed FEED1 = Feed.builder()
            .id(1L)
            .title("title")
            .content("??? ?????? ??????")
            .step(Step.PROGRESS)
            .isSos(true)
            .storageUrl("https://github.com/woowacourse-teams/2021-nolto")
            .deployedUrl("https://github.com/woowacourse-teams/2021-nolto")
            .thumbnailUrl("https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png")
            .build()
            .writtenBy(LOGIN_USER);

    public static final Feed FEED2 = Feed.builder()
            .id(2L)
            .title("title")
            .content("??? ?????? ??????")
            .step(Step.COMPLETE)
            .isSos(false)
            .deployedUrl("https://github.com/woowacourse-teams/2021-nolto")
            .thumbnailUrl("https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png")
            .views(2)
            .author(LOGIN_USER)
            .build();

    public static final Feed FEED3 =
            Feed.builder()
                    .id(3L)
                    .title("title3")
                    .content("content3")
                    .step(Step.PROGRESS)
                    .isSos(true)
                    .storageUrl("www.naver.com")
                    .deployedUrl("www.naver.com")
                    .thumbnailUrl("www.naver.com")
                    .build()
                    .writtenBy(LOGIN_USER);

    private static final List<FeedCardResponse> FEED_CARD_RESPONSES = FeedCardResponse.toList(Arrays.asList(FEED1, FEED2));
    private static final List<FeedCardResponse> PROGRESS_HELP_FEED_CARD_RESPONSES = FeedCardResponse.toList(Arrays.asList(FEED3, FEED1));

    private static final FeedResponse FEED_RESPONSE = new FeedResponse(AuthorResponse.of(LOGIN_USER), FEED1.getId(), FEED1.getTitle(), TechControllerTest.TECH_RESPONSES,
            FEED1.getContent(), FEED1.getStep().name(), FEED1.isSos(), FEED1.getStorageUrl(), FEED1.getDeployedUrl(),
            FEED1.getThumbnailUrl(), FEED1.getLikes().size(), FEED1.getViews(), true, LocalDateTime.now());

    private static final FieldDescriptor[] TECH = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ID"),
            fieldWithPath("text").type(JsonFieldType.STRING).description("?????? ?????????")};

    private static final FieldDescriptor[] FEED_CARD_RESPONSE = new FieldDescriptor[]{
            fieldWithPath("author").type(JsonFieldType.OBJECT).description("?????????"),
            fieldWithPath("author.id").type(JsonFieldType.NUMBER).description("????????? ID"),
            fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("????????? ?????????"),
            fieldWithPath("author.imageUrl").type(JsonFieldType.STRING).description("????????? ????????? URL"),
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ID"),
            fieldWithPath("title").type(JsonFieldType.STRING).description("?????? ??????"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("?????? ??????"),
            fieldWithPath("step").type(JsonFieldType.STRING).description("???????????? ??????"),
            fieldWithPath("sos").type(JsonFieldType.BOOLEAN).description("SOS ??????"),
            fieldWithPath("thumbnailUrl").type(JsonFieldType.STRING).description("????????? URL")
    };

    @MockBean
    private FeedService feedService;

    @MockBean
    private LikeService likeService;

    @DisplayName("?????? ???????????? ????????? ???????????????")
    @Test
    void create() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        given(feedService.create(any(User.class), any())).willReturn(FEED_ID);

        MockHttpServletRequestBuilder request = multipart("/feeds")
                .file("thumbnailImage", MOCK_MULTIPART_FILE.getBytes())
                .param("title", "?????? ??????")
                .param("techs", "1")
                .param("techs", "2")
                .param("content", "????????? 18??? ?????? ??????. ?????? TMI??? ??????. ???????????? ?????? ?????? ??????. ????????? ???(???)???. ???????????? ????????????. ?????? ??????.")
                .param("step", "PROGRESS")
                .param("sos", "true")
                .param("storageUrl", "https://github.com/woowacourse-teams/2021-nolto.git")
                .param("deployedUrl", "")
                .header("Authorization", "Bearer " + ACCESS_TOKEN);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/feeds/" + FEED_ID))
                .andDo(document("feed-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(
                                partWithName("thumbnailImage").description("????????? ?????????")
                        ),
                        requestParameters(
                                parameterWithName("title").description("??????"),
                                parameterWithName("techs").description("?????? ?????? ??????").optional(),
                                parameterWithName("content").description("??????"),
                                parameterWithName("step").description("???????????? ??????(?????????, ?????????)"),
                                parameterWithName("sos").description("sos ??????"),
                                parameterWithName("storageUrl").description("????????? URL").optional(),
                                parameterWithName("deployedUrl").description("?????? URL(???????????? ????????? ??????)").optional()
                        )
                ));
    }

    @DisplayName("????????? ??????????????????.")
    @Test
    void update() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        willDoNothing().given(feedService).update(any(User.class), any(Long.class), any(FeedRequest.class));

        MockHttpServletRequestBuilder request = multipart("/feeds/" + FEED_ID)
                .file("thumbnailImage", MOCK_MULTIPART_FILE.getBytes())
                .param("title", "????????? ??????")
                .param("techs", "2")
                .param("techs", "3")
                .param("content", "????????? ??? ???????????????. ????????? ??? ????????? ????????? ????????????.")
                .param("step", "COMPLETE")
                .param("sos", "false")
                .param("storageUrl", "https://github.com/woowacourse-teams/2021-nolto.git")
                .param("deployedUrl", "https://nolto.kro.kr")
                .header("Authorization", "Bearer " + ACCESS_TOKEN);

        request.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(document("feed-update",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(
                                partWithName("thumbnailImage").description("????????? ??? ?????? ????????? ?????????")
                        ),
                        requestParameters(
                                parameterWithName("title").description("????????? ??? ?????? ??????"),
                                parameterWithName("techs").description("????????? ??? ?????? ?????? ?????? ??????").optional(),
                                parameterWithName("content").description("????????? ??? ?????? ??????"),
                                parameterWithName("step").description("????????? ??? ?????? ???????????? ??????(?????????, ?????????)"),
                                parameterWithName("sos").description("????????? ??? ?????? sos ??????"),
                                parameterWithName("storageUrl").description("????????? ??? ?????? ????????? URL").optional(),
                                parameterWithName("deployedUrl").description("????????? ??? ?????? ?????? URL(???????????? ????????? ??????)").optional()
                        )
                ));
    }

    @DisplayName("????????? ????????????.")
    @Test
    void deleteFeed() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        willDoNothing().given(feedService).delete(any(User.class), any(Long.class));

        mockMvc.perform(
                        delete("/feeds/{feedId}", FEED_ID)
                                .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isNoContent())
                .andDo(document("feed-delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("????????? ?????? ID")
                        )
                ));
    }

    @DisplayName("?????? ?????? ??? ?????????????????? ????????? ??? ??????.")
    @Test
    void findById() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN_OPTIONAL)).willReturn(LOGIN_USER);
        given(feedService.viewFeed(any(User.class), any(), anyBoolean())).willReturn(FEED_RESPONSE);

        mockMvc.perform(
                        get("/feeds/{feedId}", FEED_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + ACCESS_TOKEN_OPTIONAL))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(FEED_RESPONSE)))
                .andDo(document("feed-findById",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("?????? ID")
                        ),
                        responseFields(
                                fieldWithPath("author").type(JsonFieldType.OBJECT).description("?????????"),
                                fieldWithPath("author.id").type(JsonFieldType.NUMBER).description("????????? ID"),
                                fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("????????? ?????????"),
                                fieldWithPath("author.imageUrl").type(JsonFieldType.STRING).description("????????? ????????? URL"),
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("techs").type(JsonFieldType.ARRAY).description("?????? ?????? ??????"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("step").type(JsonFieldType.STRING).description("???????????? ??????(?????????, ?????????)"),
                                fieldWithPath("sos").type(JsonFieldType.BOOLEAN).description("SOS ??????"),
                                fieldWithPath("storageUrl").type(JsonFieldType.STRING).description("????????? URL"),
                                fieldWithPath("deployedUrl").type(JsonFieldType.STRING).description("?????? URL(???????????? ????????? ??????)").optional(),
                                fieldWithPath("thumbnailUrl").type(JsonFieldType.STRING).description("????????? URL"),
                                fieldWithPath("likes").type(JsonFieldType.NUMBER).description("????????? ??????"),
                                fieldWithPath("createdDate").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("views").type(JsonFieldType.NUMBER).description("?????????"),
                                fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("?????? ???????????? ????????? ?????? ??? ????????? ??????")
                        ).andWithPrefix("techs.[].", TECH)
                ));
    }

    @DisplayName("?????? ????????? ????????? Filter??? ?????? ????????? ????????? ??? ??????.")
    @Test
    void recentResponse() throws Exception {
        FeedCardPaginationResponse feedCardPaginationResponse = new FeedCardPaginationResponse(PROGRESS_HELP_FEED_CARD_RESPONSES, null);
        given(feedService.findRecentFeeds("progress", true, 4, 2)).willReturn(feedCardPaginationResponse);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("step", "progress");
        params.add("help", "true");
        params.add("nextFeedId", "4");
        params.add("countPerPage", "2");

        mockMvc.perform(get("/feeds/recent")
                        .params(params))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(feedCardPaginationResponse)))
                .andDo(document("feed-recentResponse",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("step").description("???????????? ??????(?????????, ?????????)").optional(),
                                parameterWithName("help").description("sos ?????? ?????? ??????").optional(),
                                parameterWithName("nextFeedId").description("UI??? ????????? ????????? ????????? ID").optional(),
                                parameterWithName("countPerPage").description("???????????? ????????? ?????? ??????(default=15)").optional()
                        ),
                        responseFields(
                                fieldWithPath("feeds").type(JsonFieldType.ARRAY).description("?????? ?????? ??????"),
                                fieldWithPath("nextFeedId").type(JsonFieldType.NUMBER).description("?????? ???????????? ????????? ????????? Id").optional()
                        ).andWithPrefix("feeds.[]", FEED_CARD_RESPONSE)));
    }

    @DisplayName("?????? ????????? ??????(?????????)????????? ????????? ????????????.")
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
                                fieldWithPath("[]").type(JsonFieldType.ARRAY).description("HOT ?????? ??????")
                        ).andWithPrefix("[].", FEED_CARD_RESPONSE)));

    }

    @DisplayName("????????? ???????????? ?????????.")
    @Test
    void addLike() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);

        mockMvc.perform(post("/feeds/{feedId}/like", FEED_ID)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("feed-addLike",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("?????? ID")
                        )));
    }


    @DisplayName("????????? ???????????? ????????????.")
    @Test
    void deleteLike() throws Exception {
        given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
        willDoNothing().given(likeService).deleteLike(LOGIN_USER, FEED_ID);

        mockMvc.perform(post("/feeds/{feedId}/unlike", FEED_ID)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(document("feed-deleteLike",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("feedId").description("?????? ID")
                        )));

    }

    @DisplayName("?????? ????????? ??????+??????(query), ','??? ????????? ?????????(techs), ?????????(filter)??? ?????? ????????? ??? ??????.")
    @Test
    void searchResponse() throws Exception {
        FeedCardPaginationResponse feedCardPaginationResponse = new FeedCardPaginationResponse(PROGRESS_HELP_FEED_CARD_RESPONSES, null);
        given(feedService.search("title", "java,spring", "progress", true, 4, 2)).willReturn(feedCardPaginationResponse);

        MultiValueMap<String, String> searchParams = new LinkedMultiValueMap<>();
        searchParams.add("query", "title");
        searchParams.add("techs", "java,spring");
        searchParams.add("step", "progress");
        searchParams.add("help", "true");
        searchParams.add("nextFeedId", "4");
        searchParams.add("countPerPage", "2");

        mockMvc.perform(get("/feeds/search").params(searchParams))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(feedCardPaginationResponse)))
                .andDo(document("feed-searchResponse",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("query").description("??????+???????????? ????????? ?????? ?????????").optional(),
                                parameterWithName("techs").description("','??? ????????? ?????? ????????? ??????").optional(),
                                parameterWithName("step").description("???????????? ??????(?????????, ?????????)").optional(),
                                parameterWithName("help").description("sos ?????? ?????? ??????").optional(),
                                parameterWithName("nextFeedId").description("UI??? ????????? ????????? ????????? ID").optional(),
                                parameterWithName("countPerPage").description("???????????? ????????? ?????? ??????(default=15)").optional()
                        ),
                        responseFields(
                                fieldWithPath("feeds").type(JsonFieldType.ARRAY).description("?????? ?????? ??????"),
                                fieldWithPath("nextFeedId").type(JsonFieldType.NUMBER).description("?????? ???????????? ????????? ????????? Id").optional()
                        ).andWithPrefix("feeds.[]", FEED_CARD_RESPONSE)));
    }
}
