package com.wooteco.nolto.acceptance;

import com.wooteco.nolto.auth.infrastructure.CookieUtil;
import com.wooteco.nolto.auth.infrastructure.JwtTokenProvider;
import com.wooteco.nolto.auth.ui.dto.RefreshTokenResponse;
import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.image.application.ImageKind;
import com.wooteco.nolto.image.application.ImageService;
import com.wooteco.nolto.tech.domain.TechRepository;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import static com.wooteco.nolto.FeedFixture.DEFAULT_IMAGE_URL;
import static com.wooteco.nolto.UserFixture.깃헙_유저_생성;

import java.time.Duration;
import java.util.UUID;

import static com.wooteco.nolto.acceptance.FeedAcceptanceTest.피드_작성_요청;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected TechRepository techRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @MockBean
    private ImageService imageService;
    
    @MockBean
    private HttpServletResponse response;

    protected User 가입된_유저 = 깃헙_유저_생성();

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        given(imageService.upload(any(MultipartFile.class), any(ImageKind.class))).willReturn(DEFAULT_IMAGE_URL);
        given(imageService.update(any(String.class), any(MultipartFile.class), any(ImageKind.class))).willReturn(DEFAULT_IMAGE_URL);

        databaseCleanup.execute();
    }

    @AfterEach
    public void clear() {
        databaseCleanup.execute();
    }

    public TokenResponse 가입된_유저의_토큰을_받는다() {
        return 유저의_액세스_토큰을_받는다(가입된_유저);
    }

    public TokenResponse 유저의_액세스_토큰을_받는다(User user) {
        User 저장된_엄청난_유저 = 회원_등록되어_있음(user);
        
        String accessToken = jwtTokenProvider.createToken(String.valueOf(저장된_엄청난_유저.getId()));
        RefreshTokenResponse refreshTokenResponse = jwtTokenProvider.createRefreshToken(UUID.randomUUID().toString());
        CookieUtil.setCookie(response, CookieUtil.REFRESH_TOKEN_KEY, refreshTokenResponse.getToken(), Duration.ofSeconds(refreshTokenResponse.getExpiredIn()));
        return TokenResponse.of(accessToken, refreshTokenResponse.getToken(), refreshTokenResponse.getExpiredIn());
    }

    public User 회원_등록되어_있음(User user) {
        return userRepository.save(user);
    }

    Long 피드_업로드되어_있음(FeedRequest request) {
        TokenResponse tokenResponse = 가입된_유저의_토큰을_받는다();
        return Long.valueOf(피드_작성_요청(request, tokenResponse.getAccessToken()).header("Location").replace("/feeds/", ""));
    }

    Long 피드_업로드되어_있음(FeedRequest request, String token) {
        return Long.valueOf(피드_작성_요청(request, token).header("Location").replace("/feeds/", ""));
    }
}
