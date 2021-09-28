package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.exception.NotFoundException;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.domain.repository.CommentRepository;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.feed.ui.dto.CommentRequest;
import com.wooteco.nolto.feed.ui.dto.CommentResponse;
import com.wooteco.nolto.feed.ui.dto.ReplyResponse;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentServiceTest extends CommentServiceFixture {

    public CommentServiceTest(FeedRepository feedRepository, UserRepository userRepository, CommentRepository commentRepository, CommentService commentService, CommentLikeService commentLikeService) {
        super(feedRepository, userRepository, commentRepository, commentService, commentLikeService);
    }

    @Autowired
    private EntityManager entityManager;

    private User 찰리1 = new User("socialId", SocialType.GOOGLE, "찰리1", "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png");
    private User 아마찌 = new User("socialId", SocialType.GITHUB, "아마찌", "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png");
    private User 조엘 = new User("socialId", SocialType.GITHUB, "조엘", "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png");
    private User 포모1 = new User("socialId", SocialType.GOOGLE, "포모1", "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png");

    private Feed 아마찌의_개쩌는_지하철_미션 = Feed.builder()
            .title("아마찌의 개쩌는 지하철 미션")
            .content("난 너무 잘해")
            .step(Step.COMPLETE)
            .isSos(false)
            .storageUrl("www.github.com/newWisdom")
            .deployedUrl("www.github.com/newWisdom")
            .thumbnailUrl("https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png")
            .build()
            .writtenBy(찰리1);


    @BeforeEach
    void setUp() throws InterruptedException {
        super.setUp();
        userRepository.save(찰리1);
        userRepository.save(아마찌);
        userRepository.save(조엘);
        userRepository.save(포모1);

        아마찌의_개쩌는_지하철_미션.writtenBy(아마찌);
        feedRepository.save(아마찌의_개쩌는_지하철_미션);
        entityManager.flush();
    }

    @DisplayName("댓글을 1개 저장한다.")
    @Test
    void create() {
        // given
        User 조회해온_찰리 = userRepository.findById(찰리.getId()).get();
        Feed 조회해온_찰리가_쓴_피드 = feedRepository.findById(찰리가_쓴_피드.getId()).get();

        // when
        CommentResponse response = commentService.createComment(조회해온_찰리, 조회해온_찰리가_쓴_피드.getId(), COMMENT_REQUEST_WITHOUT_HELPER);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getContent()).isEqualTo(COMMENT_REQUEST_WITHOUT_HELPER.getContent());
        assertThat(response.isHelper()).isEqualTo(COMMENT_REQUEST_WITHOUT_HELPER.isHelper());
        assertThat(response.getAuthor().getId()).isEqualTo(찰리.getId());
        assertThat(response.getLikes()).isZero();
        assertThat(response.isLiked()).isFalse();
        assertThat(response.isModified()).isFalse();
        assertThat(response.isFeedAuthor()).isTrue();
        assertThat(response.getCreatedAt()).isNotNull();
    }

    @DisplayName("특정 피드에 대한 댓글과 대댓글 전체를 조회한다. 댓글, 대댓글은 최신 순 정렬")
    @Test
    void findAllByFeedId2() {
        // when
        commentLikeService.addCommentLike(찰리가_쓴_피드에_찰리가_쓴_댓글.getId(), 찰리);

        List<CommentResponse> allByFeedId = commentService.findAllByFeedId(찰리가_쓴_피드.getId(), 찰리);

        // then
        checkSameCommentWithReplyResponse(allByFeedId.get(0), 찰리가_쓴_피드에_포모가_쓴_댓글, 찰리);
        checkSameCommentWithReplyResponse(allByFeedId.get(1), 찰리가_쓴_피드에_찰리가_쓴_댓글, 찰리);
    }

    @DisplayName("댓글을 수정할 수 있다.")
    @Test
    void updateComment() throws InterruptedException {
        // given
        CommentResponse response = commentService.createComment(찰리, 찰리가_쓴_피드.getId(), COMMENT_REQUEST_WITHOUT_HELPER);
        entityManager.flush();
        String updateContent = "수정된 댓글 내용";
        boolean updateHelper = true;

        // when
        CommentResponse commentResponse = commentService.updateComment(response.getId(), new CommentRequest(updateContent, updateHelper), 찰리);

        // then
        assertThat(commentResponse.getContent()).isEqualTo(updateContent);
        assertThat(commentResponse.isHelper()).isEqualTo(updateHelper);
        assertThat(commentResponse.isModified()).isTrue();
    }

    @DisplayName("댓글 수정 시 존재하지 않는 댓글 ID로 요청이 오면 예외가 발생한다.")
    @Test
    void updateCommentWithNonExistCommentId() {
        // given
        String updateContent = "수정된 댓글 내용";
        boolean updateHelper = true;

        // when then
        assertThatThrownBy(() -> commentService.updateComment(Long.MAX_VALUE, new CommentRequest(updateContent, updateHelper), 찰리))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 댓글입니다.");
    }

    @DisplayName("댓글 삭제시 댓글의 대댓글도 함께 삭제된다.")
    @Test
    void deleteComment() {
        // given
        entityManager.clear();

        // when
        User user = userRepository.findById(찰리.getId()).get();
        entityManager.flush();
        commentService.deleteComment(user, 찰리가_쓴_피드에_찰리가_쓴_댓글.getId());
        entityManager.flush();
        entityManager.clear();
        Feed 삭제_후_조회한_찰리가_쓴_피드 = feedRepository.findById(찰리가_쓴_피드.getId()).get();

        // then
        assertThatThrownBy(() -> commentService.findEntityById(찰리가_쓴_피드에_찰리가_쓴_댓글.getId()))
                .isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> commentService.findEntityById(찰리가_쓴_피드에_찰리가_쓴_댓글에_찰리가_쓴_대댓글.getId()))
                .isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> commentService.findEntityById(찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글.getId()))
                .isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> commentService.findEntityById(찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_두번째_대댓글.getId()))
                .isInstanceOf(NotFoundException.class);
        assertThat(삭제_후_조회한_찰리가_쓴_피드.getComments().size()).isOne();
    }

    @DisplayName("댓글 삭제시 댓글의 좋아요도 함께 삭제된다.")
    @Test
    void deleteCommentWithLike() {
        // given
        commentLikeService.addCommentLike(찰리가_쓴_피드에_찰리가_쓴_댓글.getId(), 찰리);
        entityManager.flush();
        entityManager.clear();
        User 조회한_찰리 = userRepository.findById(찰리.getId()).get();

        // when
        commentService.deleteComment(조회한_찰리, 찰리가_쓴_피드에_찰리가_쓴_댓글.getId());
        entityManager.flush();
        Feed 삭제_후_조회한_찰리가_쓴_피드 = feedRepository.findById(찰리가_쓴_피드.getId()).get();

        // then
        assertThatThrownBy(() -> commentService.findEntityById(찰리가_쓴_피드에_찰리가_쓴_댓글.getId()))
                .isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> commentService.findEntityById(찰리가_쓴_피드에_찰리가_쓴_댓글에_찰리가_쓴_대댓글.getId()))
                .isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> commentService.findEntityById(찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글.getId()))
                .isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> commentService.findEntityById(찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_두번째_대댓글.getId()))
                .isInstanceOf(NotFoundException.class);
        assertThat(삭제_후_조회한_찰리가_쓴_피드.getComments().size()).isOne();
    }

    @DisplayName("존재하지 않는 댓글 ID로 삭제를 요청하면 예외가 발생한다.")
    @Test
    void deleteCommentWithNonExistCommentId() {
        // when then
        assertThatThrownBy(() -> commentService.deleteComment(찰리, Long.MAX_VALUE))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 댓글입니다.");
    }

    private void checkSameCommentWithReplyResponse(CommentResponse response, Comment comment, User user) {
        CommentResponse responseFromComment = CommentResponse.of(comment, user.isCommentLiked(comment));
        assertThat(response.getId()).isEqualTo(responseFromComment.getId());
        assertThat(response.getContent()).isEqualTo(responseFromComment.getContent());
        assertThat(response.isHelper()).isEqualTo(responseFromComment.isHelper());
        assertThat(response.getLikes()).isEqualTo(responseFromComment.getLikes());
        assertThat(response.isLiked()).isEqualTo(responseFromComment.isLiked());
        assertThat(response.isFeedAuthor()).isEqualTo(responseFromComment.isFeedAuthor());
        assertThat(response.getCreatedAt()).isEqualTo(responseFromComment.getCreatedAt());
        assertThat(response.getId()).isEqualTo(responseFromComment.getId());
        assertThat(response.isModified()).isEqualTo(responseFromComment.isModified());
        assertThat(response.getAuthor().getId()).isEqualTo(responseFromComment.getAuthor().getId());
    }

    @DisplayName("대댓글을 작성한다. (글 작성자가 아닌 유저)")
    @Test
    void createReply() {
        // given
        Comment 찰리_댓글 = 댓글_생성("오 마찌 멋진데?", false, 찰리1, 아마찌의_개쩌는_지하철_미션);
        commentRepository.saveAndFlush(찰리_댓글);
        entityManager.clear();

        // when
        CommentRequest 조엘_대댓글 = new CommentRequest("찰리 나는..?", false);
        CommentResponse replyResponse = commentService.createReply(조엘, 아마찌의_개쩌는_지하철_미션.getId(), 찰리_댓글.getId(), 조엘_대댓글);

        // then
        assertThat(replyResponse.getId()).isNotNull();
        assertThat(조엘_대댓글.getContent()).isEqualTo(replyResponse.getContent());
        assertThat(replyResponse.getLikes()).isZero();
        assertThat(replyResponse.isFeedAuthor()).isFalse();
        assertThat(replyResponse.getCreatedAt()).isNotNull();
        assertThat(replyResponse.isModified()).isFalse();
        assertThat(replyResponse.getAuthor().getId()).isEqualTo(조엘.getId());
    }

    @DisplayName("대댓글을 작성한다. (글 작성자인 유저)")
    @Test
    void createReplyWithAuthor() {
        // given
        Comment 포모_댓글 = 댓글_생성("아마찌에게 '누난 내 여자라니까' 불러줄 사람 구합니다.", false, 포모1, 아마찌의_개쩌는_지하철_미션);
        commentRepository.saveAndFlush(포모_댓글);

        // when
        CommentRequest 아마찌_대댓글 = new CommentRequest("내 글에서 나가~~", false);
        CommentResponse replyResponse = commentService.createReply(
                아마찌,
                아마찌의_개쩌는_지하철_미션.getId(),
                포모_댓글.getId(),
                아마찌_대댓글);

        // then
        assertThat(replyResponse.getId()).isNotNull();
        assertThat(replyResponse.getContent()).isEqualTo(아마찌_대댓글.getContent());
        assertThat(replyResponse.getLikes()).isZero();
        assertThat(replyResponse.isFeedAuthor()).isTrue();
        assertThat(replyResponse.getCreatedAt()).isNotNull();
        assertThat(replyResponse.isModified()).isFalse();
        assertThat(replyResponse.getAuthor().getId()).isEqualTo(아마찌.getId());
    }

    @DisplayName("대댓글을 조회한다. 최신 순으로 대댓글이 나열 (아마찌 -> 포모 -> 조엘)")
    @Test
    void findAllById() throws InterruptedException {
        // given
        Comment 찰리_댓글 = 댓글_생성("내일 젠킨스 강의 있습니다. 제 강의 듣고 배포 자동화 해보시죠", false, 찰리1, 아마찌의_개쩌는_지하철_미션);
        commentRepository.saveAndFlush(찰리_댓글);

        Comment 조엘_대댓글 = 댓글_생성("저 듣고 싶어요!!! 도커도 알려주세요 우테코는 왜 도커를 안 알려주는 거야!!!!!!!", false, 조엘, 아마찌의_개쩌는_지하철_미션);
        조엘_대댓글.addParentComment(찰리_댓글);
        commentRepository.saveAndFlush(조엘_대댓글);

        Comment 포모_대댓글 = 댓글_생성("오오 젠킨스 강의 탑승해봅니다", false, 포모1, 아마찌의_개쩌는_지하철_미션);
        포모_대댓글.addParentComment(찰리_댓글);
        commentRepository.saveAndFlush(포모_대댓글);

        Comment 아마찌_대댓글 = 댓글_생성("내 글에서 광고하지마!!!", false, 아마찌, 아마찌의_개쩌는_지하철_미션);
        아마찌_대댓글.addParentComment(찰리_댓글);
        commentRepository.saveAndFlush(아마찌_대댓글);

        // when
        List<ReplyResponse> findReplies = commentService.findAllRepliesById(찰리1, 아마찌의_개쩌는_지하철_미션.getId(), 찰리_댓글.getId());

        // then
        assertThat(findReplies.get(0).getId()).isEqualTo(아마찌_대댓글.getId());
        assertThat(findReplies.get(1).getId()).isEqualTo(포모_대댓글.getId());
        assertThat(findReplies.get(2).getId()).isEqualTo(조엘_대댓글.getId());
        assertThat(아마찌.getComments().size()).isOne();
        assertThat(찰리1.getComments().size()).isOne();
        assertThat(조엘.getComments().size()).isOne();
        assertThat(포모1.getComments().size()).isOne();
    }

    @DisplayName("대댓글의 내용을 수정한다.")
    @Test
    void update() throws InterruptedException {
        // given
        Comment 조엘_댓글 = 댓글_생성("조엘의 웹 호스팅을 통해 배포해보실 생각은 없으신가요?", false, 조엘, 아마찌의_개쩌는_지하철_미션);
        Comment 아마찌_대댓글 = 댓글_생성("내 글에서 광고하지마!!!", false, 아마찌, 아마찌의_개쩌는_지하철_미션);
        아마찌_대댓글.addParentComment(조엘_댓글);
        commentRepository.saveAllAndFlush(Arrays.asList(조엘_댓글, 아마찌_대댓글));

        // when
        CommentRequest 아마찌_대댓글_수정_요청 = new CommentRequest("다시 생각해보니 괜찮은 거 같기도?", false);
        CommentResponse updateReply = commentService.updateComment(아마찌_대댓글.getId(), 아마찌_대댓글_수정_요청, 아마찌);

        // then
        assertThat(updateReply.getId()).isNotNull();
        assertThat(updateReply.getContent()).isEqualTo(아마찌_대댓글_수정_요청.getContent());
        assertThat(updateReply.getLikes()).isEqualTo(아마찌_대댓글.likesCount());
        assertThat(updateReply.isFeedAuthor()).isTrue();
        assertThat(updateReply.getCreatedAt()).isNotNull();
        assertThat(updateReply.isModified()).isTrue();
        assertThat(updateReply.getAuthor().getId()).isEqualTo(아마찌.getId());
    }

    @DisplayName("대댓글 작성자가 대댓글을 삭제한다.")
    @Test
    void deleteReply() {
        // given
        CommentRequest 포모_댓글_생성요청 = new CommentRequest("영 차 영 차 영 차 영 차 영 차 영 차", false);
        CommentRequest 아마찌_대댓글_생성요청 = new CommentRequest("영 차 영 차 영 차", false);
        CommentResponse 포모_댓글_생성_응답 = commentService.createComment(포모1, 아마찌의_개쩌는_지하철_미션.getId(), 포모_댓글_생성요청);
        CommentResponse 아마찌_대댓글_생성_응답 = commentService.createReply(아마찌, 아마찌의_개쩌는_지하철_미션.getId(), 포모_댓글_생성_응답.getId(), 아마찌_대댓글_생성요청);
        assertThat(포모1.getComments().size()).isOne();
        assertThat(아마찌.getComments().size()).isOne();
        entityManager.flush();

        // when
        Comment 아마찌_대댓글 = commentService.findEntityById(아마찌_대댓글_생성_응답.getId());
        commentService.deleteComment(아마찌, 아마찌_대댓글.getId());
        entityManager.flush();
        entityManager.clear();

        // then
        Feed 삭제후_조회한_아마찌의_개쩌는_지하철_미션 = feedRepository.findById(아마찌의_개쩌는_지하철_미션.getId()).get();
        User 삭제후_조회한_아마찌 = userRepository.findById(아마찌.getId()).get();
        assertThatThrownBy(() -> commentService.findEntityById(아마찌_대댓글.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 댓글입니다.");
        assertThat(삭제후_조회한_아마찌.getComments().size()).isZero();
        assertThat(삭제후_조회한_아마찌의_개쩌는_지하철_미션.getComments().size()).isOne();
    }

    @DisplayName("댓글 작성자가 좋아요가 있는 대댓글이 존재하는 좋아요가 있는 댓글을 삭제한다.")
    @Test
    void deleteCommentExistReply() {
        // given
        CommentRequest 포모_댓글_생성요청 = new CommentRequest("영 차 영 차 영 차 영 차 영 차 영 차", false);
        CommentRequest 아마찌_대댓글_생성요청 = new CommentRequest("영 차 영 차 영 차", false);
        CommentResponse 포모_댓글_생성_응답 = commentService.createComment(포모1, 아마찌의_개쩌는_지하철_미션.getId(), 포모_댓글_생성요청);
        CommentResponse 아마찌_대댓글_생성_응답 = commentService.createReply(아마찌, 아마찌의_개쩌는_지하철_미션.getId(), 포모_댓글_생성_응답.getId(), 아마찌_대댓글_생성요청);
        commentLikeService.addCommentLike(포모_댓글_생성_응답.getId(), 포모1);
        commentLikeService.addCommentLike(아마찌_대댓글_생성_응답.getId(), 아마찌);
        entityManager.flush();

        // when
        Comment 포모_댓글 = commentService.findEntityById(포모_댓글_생성_응답.getId());
        Comment 아마찌_대댓글 = commentService.findEntityById(아마찌_대댓글_생성_응답.getId());
        assertThat(포모1.getComments().size()).isOne();
        assertThat(아마찌.getComments().size()).isOne();
        assertThat(포모1.getCommentLikes().size()).isOne();
        assertThat(아마찌.getCommentLikes().size()).isOne();

        commentService.deleteComment(포모1, 포모_댓글.getId());
        entityManager.flush();
        entityManager.clear();

        // then
        Feed 삭제후_조회한_아마찌의_개쩌는_지하철_미션 = feedRepository.findById(아마찌의_개쩌는_지하철_미션.getId()).get();
        User 삭제후_조회한_포모 = userRepository.findById(포모1.getId()).get();
        User 삭제후_조회한_아마찌 = userRepository.findById(아마찌.getId()).get();
        assertThatThrownBy(() -> commentService.findEntityById(포모_댓글.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 댓글입니다.");
        assertThatThrownBy(() -> commentService.findEntityById(아마찌_대댓글.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 댓글입니다.");
        assertThat(삭제후_조회한_포모.getComments().size()).isZero();
        assertThat(삭제후_조회한_아마찌.getComments().size()).isZero();
        assertThat(삭제후_조회한_포모.getCommentLikes().size()).isZero();
        assertThat(삭제후_조회한_아마찌.getCommentLikes().size()).isZero();
        assertThat(삭제후_조회한_아마찌의_개쩌는_지하철_미션.getComments().size()).isZero();
    }

    private Comment 댓글_생성(String 댓글_내용, boolean 도움, User 댓글_유저, Feed 피드) {
        Comment 댓글 = new Comment(댓글_내용, 도움).writtenBy(댓글_유저, 피드);
        return 댓글;
    }
}