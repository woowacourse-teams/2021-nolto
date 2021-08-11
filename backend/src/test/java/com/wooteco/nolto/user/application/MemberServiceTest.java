package com.wooteco.nolto.user.application;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.exception.BadRequestException;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Like;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.domain.repository.CommentRepository;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.feed.domain.repository.LikeRepository;
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

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    FeedRepository feedRepository;

    @Autowired
    LikeRepository likeRepository;

    @MockBean
    private ImageService imageService;

    public static final String 존재하는_백신_영상_닉네임 = "존재하는 백신 영상 닉네임";
    public static final String 존재하지않는_닉네임 = "존재하지않는 닉네임";
    public static final String 수정할_닉네임 = "수정할 닉네임";

    public static final ProfileRequest 프로필_수정_요청 = new ProfileRequest(수정할_닉네임, "안녕하세용", null);
    public static final ProfileRequest 존재하는_닉네임으로_프로필_수정_요청 = new ProfileRequest(존재하는_백신_영상_닉네임, "안녕하세용", null);

    private final User 영상이 = new User(null, "1", SocialType.GITHUB, 존재하는_백신_영상_닉네임, "joel.jpg");
    private final User 아마찌 = new User(null, "2", SocialType.GITHUB, "AMAZZI", "ama.jpg");

    private final Feed 영상이_피드 = new Feed("joelFeed", "joelFeed", Step.PROGRESS, true, "", "", "");
    private final Feed 아마찌_피드 = new Feed("amaFeed", "amaFeed", Step.PROGRESS, true, "", "", "");

    private final Comment 영상이_피드에_영상이_댓글 = new Comment("joelFeedJoelComment", true);
    private final Comment 아마찌_피드에_영상이_댓글 = new Comment("amaFeedJoelComment", true);

    private final Like 영상이_피드에_영상이_좋아요 = new Like(영상이, 영상이_피드);
    private final Like 아마찌_피드에_영상이_좋아요 = new Like(영상이, 아마찌_피드);

    @BeforeEach
    void setUp() {
        given(imageService.upload(any(MultipartFile.class), any(ImageKind.class))).willReturn("image.jpg");
        userRepository.saveAndFlush(영상이);
        userRepository.saveAndFlush(아마찌);

        feedRepository.saveAndFlush(영상이_피드.writtenBy(영상이));
        feedRepository.saveAndFlush(아마찌_피드.writtenBy(아마찌));

        commentRepository.saveAndFlush(영상이_피드에_영상이_댓글.writtenBy(영상이, 영상이_피드));
        commentRepository.saveAndFlush(아마찌_피드에_영상이_댓글.writtenBy(영상이, 아마찌_피드));

        영상이.addLike(영상이_피드에_영상이_좋아요);
        likeRepository.saveAndFlush(영상이_피드에_영상이_좋아요);
        영상이.addLike(아마찌_피드에_영상이_좋아요);
        likeRepository.saveAndFlush(아마찌_피드에_영상이_좋아요);
    }

    @DisplayName("사용자의 히스토리(좋아요 한 글, 내가 작성한 글, 내가 남긴 댓글)를 최신순으로 조회할 수 있다.")
    @Test
    void findHistory() {
        //when
        MemberHistoryResponse memberHistoryResponse = memberService.findHistory(영상이);

        //then
        List<String> likedFeedsTitle = getFeedHistoryResponseTitle(memberHistoryResponse.getLikedFeeds());
        assertThat(likedFeedsTitle).containsExactly(아마찌_피드.getTitle(), 영상이_피드.getTitle());

        List<String> myFeedsTitle = getFeedHistoryResponseTitle(memberHistoryResponse.getMyFeeds());
        assertThat(myFeedsTitle).contains(영상이_피드.getTitle());

        List<String> myComments = getCommentResponseComment(memberHistoryResponse.getMyComments());
        assertThat(myComments).containsExactly(아마찌_피드에_영상이_댓글.getContent(), 영상이_피드에_영상이_댓글.getContent());
    }

    @DisplayName("닉네임의 중복 여부를 검사한다.")
    @Test
    void validateDuplicated() {
        // when
        NicknameValidationResponse 사용불가능한_닉네임 = memberService.validateDuplicated(존재하는_백신_영상_닉네임);
        NicknameValidationResponse 사용가능한_닉네임 = memberService.validateDuplicated(존재하지않는_닉네임);

        // then
        assertThat(사용불가능한_닉네임).extracting("isUsable").isEqualTo(false);
        assertThat(사용가능한_닉네임).extracting("isUsable").isEqualTo(true);
    }

    @DisplayName("멤버의 프로필을 조회한다.")
    @Test
    void findProfile() {
        ProfileResponse 프로필_조회_응답 = memberService.findProfile(영상이);

        멤버_프로필_정보가_같은지_확인(영상이, 프로필_조회_응답);
    }

    @DisplayName("멤버의 프로필을 수정한다.")
    @Test
    void updateProfile() {
        ProfileResponse 프로필_수정_응답 = memberService.updateProfile(영상이, 프로필_수정_요청);

        멤버_프로필_정보가_같은지_확인(프로필_수정_요청, 프로필_수정_응답);
    }

    @DisplayName("멤버의 프로필을 수정하는 경우 자신의 기존 닉네임과 동일하면 정상으로 동작한다.")
    @Test
    void updateProfileException() {
        ProfileResponse 프로필_수정_응답 = memberService.updateProfile(영상이, 존재하는_닉네임으로_프로필_수정_요청);

        멤버_프로필_정보가_같은지_확인(존재하는_닉네임으로_프로필_수정_요청, 프로필_수정_응답);
    }

    @DisplayName("멤버의 프로필을 수정하는 경우 이미 존재하는 닉네임일 경우 예외가 발생한다.")
    @Test
    void updateProfileNotException() {
        assertThatThrownBy(() -> memberService.updateProfile(아마찌, 존재하는_닉네임으로_프로필_수정_요청))
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
