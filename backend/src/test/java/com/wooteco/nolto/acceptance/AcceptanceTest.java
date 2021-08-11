package com.wooteco.nolto.acceptance;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.auth.infrastructure.JwtTokenProvider;
import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import com.wooteco.nolto.image.application.ImageKind;
import com.wooteco.nolto.image.application.ImageService;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import static com.wooteco.nolto.acceptance.FeedAcceptanceTest.DEFAULT_IMAGE_URL;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public abstract class AcceptanceTest {

    public static final String DEFAULT_IMAGE = "nolto-default-thumbnail.png";
    public static final String DEFAULT_IMAGE_URL = "https://dksykemwl00pf.cloudfront.net/" + "nolto-default-thumbnail.png";

    @LocalServerPort
    int port;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private ImageService imageService;

    public User 존재하는_유저 = new User(
            "1",
            SocialType.GITHUB,
            "엄청난 유저",
            "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png");

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        BDDMockito.given(imageService.upload(any(MultipartFile.class), any(ImageKind.class))).willReturn(DEFAULT_IMAGE_URL);
        BDDMockito.given(imageService.update(any(String.class), any(MultipartFile.class), any(ImageKind.class))).willReturn(DEFAULT_IMAGE_URL);
    }

    public RequestSpecification given() {
        return RestAssured.given().port(port);
    }

    public TokenResponse 존재하는_유저의_토큰을_받는다() {
        return 존재하는_유저의_토큰을_받는다(존재하는_유저);
    }

    public TokenResponse 존재하는_유저의_토큰을_받는다(User user) {
        User 저장된_엄청난_유저 = 회원_등록되어_있음(user);

        String token = jwtTokenProvider.createToken(String.valueOf(저장된_엄청난_유저.getId()));
        return new TokenResponse(token);
    }

    public User 회원_등록되어_있음(User user) {
        return userRepository.save(user);
    }
}
