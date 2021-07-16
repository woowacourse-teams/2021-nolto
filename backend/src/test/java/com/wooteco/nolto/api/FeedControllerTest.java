package com.wooteco.nolto.api;

import com.wooteco.nolto.feed.application.FeedService;
import com.wooteco.nolto.feed.application.LikeService;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.ui.FeedController;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FeedController.class)
public class FeedControllerTest extends ControllerTest {

    private static final String TOKEN_PAYLOAD = "user@email.com";

    private static final User LOGIN_USER =
            new User(2L, "user2@email.com", "user2", "아마찌", "imageUrl");

    private static final MockMultipartFile MOCK_MULTIPART_FILE =
            new MockMultipartFile("thumbnailImage", "thumbnailImage.png", "image/png", "<<png data>>".getBytes());

    private static final long FEED_ID = 1L;

    private static final Feed FEED =
            new Feed("title1", "content1", Step.PROGRESS, true, "www.surl.com", "www.durl.com", "www.turl.com").writtenBy(LOGIN_USER);

    private static final FeedResponse FEED_RESPONSE = FeedResponse.of(LOGIN_USER, FEED, true);

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
                                parameterWithName("techs").description("기술 스택 목록"),
                                parameterWithName("content").description("내용"),
                                parameterWithName("step").description("단계"),
                                parameterWithName("sos").description("sos 여부"),
                                parameterWithName("storageUrl").description("저장소 URL"),
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
}
