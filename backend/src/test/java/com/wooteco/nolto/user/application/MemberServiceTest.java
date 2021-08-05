package com.wooteco.nolto.user.application;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.exception.BadRequestException;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Like;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.image.application.ImageKind;
import com.wooteco.nolto.image.application.ImageService;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import com.wooteco.nolto.user.ui.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @MockBean
    private ImageService imageService;

    public static final String 존재하는_닉네임 = "존재하는 닉네임";
    public static final String 존재하지않는_닉네임 = "존재하지않는 닉네임";
    public static final String 수정할_닉네임 = "수정할 닉네임";

    public static final ProfileRequest 프로필_수정_요청 = new ProfileRequest(수정할_닉네임, "안녕하세용", null);
    public static final ProfileRequest 존재하는_닉네임으로_프로필_수정_요청 = new ProfileRequest(존재하는_닉네임, "안녕하세용", null);

    private User 존재하는_유저 = new User(
            "1",
            SocialType.GITHUB,
            존재하는_닉네임,
            "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png");


    private final User joel = new User(null, "1", SocialType.GITHUB, "JOEL", "joel.jpg");
    private final User ama = new User(null, "2", SocialType.GITHUB, "AMAZZI", "ama.jpg");

    private final Feed joelFeed = new Feed("joelFeed", "joelFeed", Step.PROGRESS, true, "", "", "").writtenBy(joel);
    private final Feed amaFeed = new Feed("amaFeed", "amaFeed", Step.PROGRESS, true, "", "", "").writtenBy(ama);

    private final Comment joelCommentsJoelFeed = new Comment("joelCommentJoelFeed", true).writtenBy(joel);
    private final Comment joelCommentsAmaFeed = new Comment("joelCommentAmaFeed", true).writtenBy(joel);

    private final Like joelLikesJoelFeed = new Like(joel, joelFeed);
    private final Like joelLikesAmaFeed = new Like(joel, amaFeed);

    @BeforeEach
    void setUp() {
        given(imageService.upload(any(MultipartFile.class), any(ImageKind.class))).willReturn("image.jpg");

        joelFeed.addComment(joelCommentsJoelFeed);
        amaFeed.addComment(joelCommentsAmaFeed);
        joel.addLike(joelLikesJoelFeed);
        joel.addLike(joelLikesAmaFeed);
        userRepository.save(ama);
        userRepository.save(joel);
        userRepository.save(존재하는_유저);
    }

    @DisplayName("사용자의 히스토리(좋아요 한 글, 내가 작성한 글, 내가 남긴 댓글)를 조회할 수 있다.")
    @Test
    void findHistory() {
        //when
        MemberHistoryResponse memberHistoryResponse = userService.findHistory(joel);

        //then
        List<String> likedFeedsTitle = getFeedHistoryResponseTitle(memberHistoryResponse.getLikedFeeds());
        assertThat(likedFeedsTitle).containsExactly(joelFeed.getTitle(), amaFeed.getTitle());

        List<String> myFeedsTitle = getFeedHistoryResponseTitle(memberHistoryResponse.getMyFeeds());
        assertThat(myFeedsTitle).containsExactly(joelFeed.getTitle());

        List<String> myComments = getCommentResponseComment(memberHistoryResponse.getMyComments());
        assertThat(myComments).containsExactly(joelCommentsJoelFeed.getContent(), joelCommentsAmaFeed.getContent());
    }

    @DisplayName("닉네임의 중복 여부를 검사한다.")
    @Test
    void validateDuplicated() {
        // when
        NicknameValidationResponse 사용불가능한_닉네임 = userService.validateDuplicated(존재하는_닉네임);
        NicknameValidationResponse 사용가능한_닉네임 = userService.validateDuplicated(존재하지않는_닉네임);

        // then
        assertThat(사용불가능한_닉네임).extracting("isUsable").isEqualTo(false);
        assertThat(사용가능한_닉네임).extracting("isUsable").isEqualTo(true);
    }

    @DisplayName("멤버의 프로필을 조회한다.")
    @Test
    void findProfile() {
        ProfileResponse 프로필_조회_응답 = userService.findProfile(존재하는_유저);

        멤버_프로필_정보가_같은지_확인(존재하는_유저, 프로필_조회_응답);
    }

    @DisplayName("멤버의 프로필을 수정한다.")
    @Test
    void updateProfile() {
        ProfileResponse 프로필_수정_응답 = userService.updateProfile(존재하는_유저, 프로필_수정_요청);

        멤버_프로필_정보가_같은지_확인(프로필_수정_요청, 프로필_수정_응답);
    }

    @DisplayName("멤버의 프로필을 수정하는 경우 이미 존재하는 닉네임일 경우 예외가 발생한다..")
    @Test
    void updateProfileException() {
        assertThatThrownBy(() -> userService.updateProfile(존재하는_유저, 존재하는_닉네임으로_프로필_수정_요청))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorType.ALREADY_EXIST_NICKNAME.getMessage());
    }

    private void 멤버_프로필_정보가_같은지_확인(User user, ProfileResponse response) {
        assertThat(user.getNickName()).isEqualTo(response.getNickname());
        assertThat(user.getBio()).isEqualTo(response.getBio());
        assertThat(user.getImageUrl()).isEqualTo(response.getImageUrl());
    }

    private void 멤버_프로필_정보가_같은지_확인(ProfileRequest request, ProfileResponse response) {
        assertThat(request.getNickname()).isEqualTo(response.getNickname());
        assertThat(request.getBio()).isEqualTo(response.getBio());
    }

    public List<String> getFeedHistoryResponseTitle(List<FeedHistoryResponse> feedHistoryResponses) {
        return feedHistoryResponses.stream()
                .map(FeedHistoryResponse::getTitle)
                .collect(Collectors.toList());
    }

    public List<String> getCommentResponseComment(List<CommentHistoryResponse> commentHistoryResponses) {
        return commentHistoryResponses.stream()
                .map(CommentHistoryResponse::getText)
                .collect(Collectors.toList());
    }
}
