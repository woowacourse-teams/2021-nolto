package com.wooteco.nolto.acceptance;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.dto.ExceptionResponse;
import com.wooteco.nolto.feed.ui.dto.CommentRequest;
import com.wooteco.nolto.feed.ui.dto.CommentResponse;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.feed.ui.dto.ReplyResponse;
import com.wooteco.nolto.image.application.ImageKind;
import com.wooteco.nolto.image.application.ImageService;
import com.wooteco.nolto.user.domain.User;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

public class CommentAcceptanceTest extends AcceptanceTest {

    public FeedRequest 진행중_단계의_피드_요청 = new FeedRequest("title1", new ArrayList<>(), "content1", "PROGRESS", false,
            "www.github.com/woowacourse", null, null);
    private final File thumbnail = new File(new File("").getAbsolutePath() + "/src/test/resources/static/nolto-default-thumbnail.png");
    private User 댓글_작성자 = new User("멋진 GITHUB ID", SocialType.GITHUB, "찰리", "초콜릿 먹고있는 프로필.jpg");
    private Long 업로드한_피드의_ID;
    private TokenResponse 로그인된_댓글_작성자의_토큰;

    @MockBean
    private ImageService imageService;

    @BeforeEach
    public void setUp() {
        super.setUp();
        BDDMockito.given(imageService.upload(any(MultipartFile.class), any(ImageKind.class))).willReturn("https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png");

        User 회원_등록된_댓글_작성자 = 회원_등록되어_있음(댓글_작성자);
        로그인된_댓글_작성자의_토큰 = 가입된_유저의_토큰을_받는다(회원_등록된_댓글_작성자);
        업로드한_피드의_ID = 피드_업로드되어_있음(진행중_단계의_피드_요청, 로그인된_댓글_작성자의_토큰);
    }

    @DisplayName("로그인 하지않은 상태에서 댓글 작성을 요청하면 예외가 발생한다.")
    @Test
    public void createCommentWithoutLogin() {
        // given
        CommentRequest 일반_댓글_작성요청 = new CommentRequest("로그인 하지 않고 댓글을 쓰고싶어요", false);

        // when
        ExtractableResponse<Response> 실패한_댓글_작성_응답 = 로그인_하지않고_댓글을_작성한다(일반_댓글_작성요청);

        // then
        요청_실패함(실패한_댓글_작성_응답, HttpStatus.UNAUTHORIZED, ErrorType.TOKEN_NEEDED);
    }

    @DisplayName("유효하지 않은 토큰으로 댓글 작성을 요청하면 예외가 발생한다.")
    @Test
    public void createCommentWithInvalidToken() {
        // given
        CommentRequest 일반_댓글_작성요청 = new CommentRequest("로그인 하지 않고 댓글을 쓰고싶어요", false);

        // when
        ExtractableResponse<Response> 실패한_댓글_작성_응답 = 댓글을_작성한다(일반_댓글_작성요청, "유통기한이 지난 쉰내나는 토큰");

        // then
        요청_실패함(실패한_댓글_작성_응답, HttpStatus.UNAUTHORIZED, ErrorType.INVALID_TOKEN);
    }

    @DisplayName("피드에 도와줄게요가 아닌 일반 댓글을 작성한다.")
    @Test
    public void createComment() {
        // given
        CommentRequest 일반_댓글_작성요청 = new CommentRequest("와 너무 멋진 프로젝트네요", false);

        // when
        ExtractableResponse<Response> 일반_댓글_작성_응답 = 댓글을_작성한다(일반_댓글_작성요청, 로그인된_댓글_작성자의_토큰.getAccessToken());

        // then
        댓글_작성_성공함(일반_댓글_작성_응답, 일반_댓글_작성요청);
    }

    @DisplayName("피드에 도와줄게요 댓글을 작성한다.")
    @Test
    public void createCommentIsHelper() {
        // given
        CommentRequest 도와줄게요_댓글_작성요청 = new CommentRequest("와 너무 멋진 프로젝트네요", true);

        // when
        ExtractableResponse<Response> 도와줄게요_댓글_작성_응답 = 댓글을_작성한다(도와줄게요_댓글_작성요청, 로그인된_댓글_작성자의_토큰.getAccessToken());

        // then
        댓글_작성_성공함(도와줄게요_댓글_작성_응답, 도와줄게요_댓글_작성요청);
    }

    @DisplayName("댓글 내용은 1글자 이상이어야 한다.")
    @Test
    public void createCommentWithBlankContent() {
        // given
        CommentRequest 내용이_비어있는_댓글_작성요청 = new CommentRequest("", true);

        // when
        ExtractableResponse<Response> 실패한_댓글_작성_응답 = 댓글을_작성한다(내용이_비어있는_댓글_작성요청, 로그인된_댓글_작성자의_토큰.getAccessToken());

        // then
        요청_실패함(실패한_댓글_작성_응답, HttpStatus.BAD_REQUEST, ErrorType.DATA_BINDING_ERROR);
    }

    @DisplayName("로그인 하지않고 피드에 있는 모든 댓글을 최신순으로 조회한다. (대댓글은 응답에 포함되지 않음)")
    @Test
    public void findAllCommentsByFeedId() {
        // given
        CommentRequest 댓글_작성_요청1 = new CommentRequest("첫 댓글 달았어요 :)", false);
        CommentRequest 댓글_작성_요청2 = new CommentRequest("1등 아깝다..", false);
        CommentRequest 대댓글_작성_요청1 = new CommentRequest("첫 댓글 1등 대댓글임", false);
        CommentResponse commentResponse1 = 댓글_등록되어_있음(댓글_작성_요청1, 로그인된_댓글_작성자의_토큰.getAccessToken());
        CommentResponse commentResponse2 = 댓글_등록되어_있음(댓글_작성_요청2, 로그인된_댓글_작성자의_토큰.getAccessToken());
        대댓글을_작성한다(대댓글_작성_요청1, 로그인된_댓글_작성자의_토큰.getAccessToken(), commentResponse1.getId());
        댓글에_좋아요를_추가한다(로그인된_댓글_작성자의_토큰.getAccessToken(), commentResponse1.getId());

        // when
        ExtractableResponse<Response> response = 로그인_하지않고_댓글_목록_조회한다(업로드한_피드의_ID);

        // then
        댓글_목록_조회_성공(response, 2, commentResponse2, 0);
    }

    @DisplayName("로그인한 유저가 이미 좋아요를 누른 댓글을 조회시 liked가 true다")
    @Test
    public void findAllCommentsByFeedIdWithLikedComment() {
        // given
        CommentRequest 댓글_작성_요청1 = new CommentRequest("첫 댓글 달았어요 :)", false);
        CommentRequest 댓글_작성_요청2 = new CommentRequest("1등 아깝다..", false);
        CommentRequest 대댓글_작성_요청1 = new CommentRequest("첫 댓글 1등 대댓글임", false);
        CommentResponse commentResponse1 = 댓글_등록되어_있음(댓글_작성_요청1, 로그인된_댓글_작성자의_토큰.getAccessToken());
        댓글_등록되어_있음(댓글_작성_요청2, 로그인된_댓글_작성자의_토큰.getAccessToken());
        대댓글을_작성한다(대댓글_작성_요청1, 로그인된_댓글_작성자의_토큰.getAccessToken(), commentResponse1.getId());
        댓글에_좋아요를_추가한다(로그인된_댓글_작성자의_토큰.getAccessToken(), commentResponse1.getId());

        // when
        ExtractableResponse<Response> response = 로그인_하고_댓글_목록_조회한다(로그인된_댓글_작성자의_토큰.getAccessToken(), 업로드한_피드의_ID);

        // then
        댓글_목록_조회시_이미_좋아요누른_댓글은_좋아요를_누른것으로_표시된다(response, 2, commentResponse1, 1);
    }

    @DisplayName("댓글 작성자가 자신의 댓글(또는 대댓글)을 수정한다.")
    @Test
    public void updateCommentWithAuthor() {
        // given
        CommentRequest 일반_댓글_작성요청 = new CommentRequest("와 너무 멋진 프로젝트네요", false);
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(일반_댓글_작성요청, 로그인된_댓글_작성자의_토큰.getAccessToken());

        CommentRequest 댓글_수정_요청 = new CommentRequest("천천히 보다보니 수정할 부분이 보이네요", true);

        // when
        ExtractableResponse<Response> 댓글_수정_응답 = 댓글을_수정한다(댓글_수정_요청, 로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_댓글.getId());

        // then
        댓글_수정_성공함(댓글_수정_응답, 댓글_수정_요청);
    }

    @DisplayName("댓글의 내용을 빈값으로 수정요청하면 예외가 발생한다.")
    @Test
    public void updateCommentWithBlankContent() {
        // given
        CommentRequest 일반_댓글_작성요청 = new CommentRequest("와 너무 멋진 프로젝트네요", false);
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(일반_댓글_작성요청, 로그인된_댓글_작성자의_토큰.getAccessToken());

        CommentRequest 댓글내용_빈값으로_수정_요청 = new CommentRequest("", true);

        // when
        ExtractableResponse<Response> 댓글내용_빈값으로_수정_응답 = 댓글을_수정한다(댓글내용_빈값으로_수정_요청, 로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_댓글.getId());

        // then
        요청_실패함(댓글내용_빈값으로_수정_응답, HttpStatus.BAD_REQUEST, ErrorType.DATA_BINDING_ERROR);
    }

    @DisplayName("로그인 한 유저가 다른 유저의 댓글을 수정하려고 하면 예외가 발생한다.")
    @Test
    public void updateCommentWithOtherUser() {
        // given
        CommentRequest 일반_댓글_작성요청 = new CommentRequest("와 너무 멋진 프로젝트네요", false);
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(일반_댓글_작성요청, 로그인된_댓글_작성자의_토큰.getAccessToken());

        CommentRequest 댓글_수정_요청 = new CommentRequest("내 댓글은 아닌데 수정하고싶네요", true);

        User 댓글_작성자가_아닌_유저 = 회원_등록되어_있음(new User("악마같은 GITHUB ID", SocialType.GITHUB, "김악질", "사탄.jpg"));
        TokenResponse 댓글_작성자가_아닌_유저의_토큰 = 가입된_유저의_토큰을_받는다(댓글_작성자가_아닌_유저);

        // when
        ExtractableResponse<Response> 댓글_작성자가_아닌_유저의_댓글_수정_응답 = 댓글을_수정한다(댓글_수정_요청, 댓글_작성자가_아닌_유저의_토큰.getAccessToken(), 등록된_댓글.getId());

        // then
        요청_실패함(댓글_작성자가_아닌_유저의_댓글_수정_응답, HttpStatus.UNAUTHORIZED, ErrorType.UNAUTHORIZED_UPDATE_COMMENT);
    }

    @DisplayName("대댓글 작성자가 대댓글을 수정할 수 있다.")
    @Test
    public void updateReplyWithAuthor() {
        // given
        CommentRequest 댓글_작성_요청1 = new CommentRequest("첫 댓글 달았어요 :)", false);
        CommentRequest 대댓글_작성_요청1 = new CommentRequest("첫 댓글 1등 대댓글임", false);
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(댓글_작성_요청1, 로그인된_댓글_작성자의_토큰.getAccessToken());

        CommentResponse 등록된_대댓글 = 대댓글_등록되어_있음(대댓글_작성_요청1, 로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_댓글.getId());
        CommentRequest 대댓글_수정_요청 = new CommentRequest("대댓글도 수정이 되나요?", false);

        // when
        ExtractableResponse<Response> 대댓글_수정_응답 = 댓글을_수정한다(대댓글_수정_요청, 로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_대댓글.getId());

        // then
        댓글_수정_성공함(대댓글_수정_응답, 대댓글_수정_요청);
    }

    @DisplayName("대댓글을 '도와드릴게요'로 수정하려고 하면 예외가 발생한다.")
    @Test
    public void updateReplyWithHelperIsTrue() {
        // given
        CommentRequest 댓글_작성_요청1 = new CommentRequest("첫 댓글 달았어요 :)", false);
        CommentRequest 대댓글_작성_요청1 = new CommentRequest("첫 댓글 1등 대댓글임", false);
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(댓글_작성_요청1, 로그인된_댓글_작성자의_토큰.getAccessToken());

        CommentResponse 등록된_대댓글 = 대댓글_등록되어_있음(대댓글_작성_요청1, 로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_댓글.getId());
        CommentRequest 대댓글_수정_요청 = new CommentRequest("대댓글도 도와드릴게요 하고 싶은데요?", true);

        // when
        ExtractableResponse<Response> 도와드릴게요로_대댓글_수정_응답 = 댓글을_수정한다(대댓글_수정_요청, 로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_대댓글.getId());

        // then
        요청_실패함(도와드릴게요로_대댓글_수정_응답, HttpStatus.BAD_REQUEST, ErrorType.REPLY_NOT_SUPPORTED_HELPER);
    }

    @DisplayName("댓글을 작성한 유저가 댓글을 삭제할 수 있다.")
    @Test
    public void deleteCommentWithAuthor() {
        // given
        CommentRequest 댓글_작성_요청1 = new CommentRequest("첫 댓글 달았어요 :)", false);
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(댓글_작성_요청1, 로그인된_댓글_작성자의_토큰.getAccessToken());

        // when
        ExtractableResponse<Response> 댓글_삭제_응답 = 댓글을_삭제한다(로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_댓글.getId());

        // then
        댓글_삭제_성공함(댓글_삭제_응답);
    }

    @DisplayName("대댓글을 작성한 유저가 대댓글을 삭제할 수 있다.")
    @Test
    public void deleteReplyWithAuthor() {
        // given
        CommentRequest 댓글_작성_요청1 = new CommentRequest("첫 댓글 달았어요 :)", false);
        CommentRequest 대댓글_작성_요청1 = new CommentRequest("첫 댓글 1등 대댓글임", false);
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(댓글_작성_요청1, 로그인된_댓글_작성자의_토큰.getAccessToken());

        CommentResponse 등록된_대댓글 = 대댓글_등록되어_있음(대댓글_작성_요청1, 로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_댓글.getId());

        // when
        ExtractableResponse<Response> 대댓글_삭제_응답 = 댓글을_삭제한다(로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_대댓글.getId());

        // then
        댓글_삭제_성공함(대댓글_삭제_응답);
    }

    @DisplayName("작성자 이외의 다른 유저가 댓글을 삭제하려고 하면 예외가 발생한다.")
    @Test
    public void deleteCommentWithOtherUser() {
        // given
        CommentRequest 댓글_작성_요청1 = new CommentRequest("첫 댓글 달았어요 :)", false);
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(댓글_작성_요청1, 로그인된_댓글_작성자의_토큰.getAccessToken());

        User 댓글_작성자가_아닌_유저 = 회원_등록되어_있음(new User("악마같은 GITHUB ID", SocialType.GITHUB, "김악질", "사탄.jpg"));
        TokenResponse 댓글_작성자가_아닌_유저의_토큰 = 가입된_유저의_토큰을_받는다(댓글_작성자가_아닌_유저);

        // when
        ExtractableResponse<Response> 댓글_삭제_응답 = 댓글을_삭제한다(댓글_작성자가_아닌_유저의_토큰.getAccessToken(), 등록된_댓글.getId());

        // then
        요청_실패함(댓글_삭제_응답, HttpStatus.UNAUTHORIZED, ErrorType.UNAUTHORIZED_DELETE_COMMENT);
    }

    @DisplayName("로그인한 유저가 댓글에 좋아요를 추가할 수 있다.")
    @Test
    public void addCommentLikeWithLoginUser() {
        // given
        CommentRequest 댓글_작성_요청1 = new CommentRequest("첫 댓글 달았어요 :)", false);
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(댓글_작성_요청1, 로그인된_댓글_작성자의_토큰.getAccessToken());

        // when
        ExtractableResponse<Response> 댓글_좋아요_추가_응답 = 댓글에_좋아요를_추가한다(로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_댓글.getId());

        // then
        댓글에_좋아요_추가_성공(댓글_좋아요_추가_응답);
    }

    @DisplayName("로그인한 유저가 이미 좋아요를 누른 글에 좋아요를 추가하려고 하면 예외가 발생한다.")
    @Test
    public void addCommentLikeWithAlreadyLiked() {
        // given
        CommentRequest 댓글_작성_요청1 = new CommentRequest("첫 댓글 달았어요 :)", false);
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(댓글_작성_요청1, 로그인된_댓글_작성자의_토큰.getAccessToken());

        // when
        댓글에_좋아요를_추가한다(로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_댓글.getId());
        ExtractableResponse<Response> 이미_좋아요_누른_댓글에_댓글_좋아요_추가_응답 = 댓글에_좋아요를_추가한다(로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_댓글.getId());

        // then
        요청_실패함(이미_좋아요_누른_댓글에_댓글_좋아요_추가_응답, HttpStatus.BAD_REQUEST, ErrorType.ALREADY_LIKED_COMMENT);
    }

    @DisplayName("로그인 하지않은 유저가 댓글에 좋아요를 추가하려고 하면 예외가 발생한다.")
    @Test
    public void addCommentLikeWithoutLogin() {
        // given
        CommentRequest 댓글_작성_요청1 = new CommentRequest("첫 댓글 달았어요 :)", false);
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(댓글_작성_요청1, 로그인된_댓글_작성자의_토큰.getAccessToken());

        // when
        ExtractableResponse<Response> 로그인_하지않고_댓글_좋아요_추가_응답 = 로그인_하지않고_댓글에_좋아요를_추가한다(등록된_댓글.getId());

        // then
        요청_실패함(로그인_하지않고_댓글_좋아요_추가_응답, HttpStatus.UNAUTHORIZED, ErrorType.TOKEN_NEEDED);
    }

    @DisplayName("로그인한 유저가 좋아요를 눌렀던 댓글에 좋아요를 취소할 수 있다.")
    @Test
    public void deleteCommentLikeWithLoginUser() {
        // given
        CommentRequest 댓글_작성_요청1 = new CommentRequest("첫 댓글 달았어요 :)", false);
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(댓글_작성_요청1, 로그인된_댓글_작성자의_토큰.getAccessToken());
        댓글에_좋아요를_추가한다(로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_댓글.getId());

        // when
        ExtractableResponse<Response> 댓글에_좋아요_취소_응답 = 댓글에_좋아요를_취소한다(로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_댓글.getId());

        // then
        댓글에_좋아요_취소_성공(댓글에_좋아요_취소_응답);
    }

    @DisplayName("로그인 하지않은 유저가 좋아요를 취소를 요청하면 예외가 발생한다.")
    @Test
    public void deleteCommentLikeWithoutLogin() {
        // given
        CommentRequest 댓글_작성_요청1 = new CommentRequest("첫 댓글 달았어요 :)", false);
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(댓글_작성_요청1, 로그인된_댓글_작성자의_토큰.getAccessToken());
        댓글에_좋아요를_추가한다(로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_댓글.getId());

        // when
        ExtractableResponse<Response> 댓글에_좋아요_취소_응답 = 로그인_하지않고_댓글에_좋아요를_취소한다(등록된_댓글.getId());

        // then
        요청_실패함(댓글에_좋아요_취소_응답, HttpStatus.UNAUTHORIZED, ErrorType.TOKEN_NEEDED);
    }

    @DisplayName("로그인한 유저가 좋아요가 누르지 않는 댓글에 좋아요 취소를 요청하면 예외가 발생한다.")
    @Test
    public void deleteCommentLikeWithoutLiked() {
        // given
        CommentRequest 댓글_작성_요청1 = new CommentRequest("첫 댓글 달았어요 :)", false);
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(댓글_작성_요청1, 로그인된_댓글_작성자의_토큰.getAccessToken());

        // when
        ExtractableResponse<Response> 댓글에_좋아요_취소_응답 = 댓글에_좋아요를_취소한다(로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_댓글.getId());

        // then
        요청_실패함(댓글에_좋아요_취소_응답, HttpStatus.BAD_REQUEST, ErrorType.NOT_LIKED_COMMENT);
    }

    @DisplayName("로그인 하지않고 댓글에 대댓글 작성을 요청하면 예외가 발생한다.")
    @Test
    public void createReplyWithoutLogin() {
        // given
        CommentRequest 댓글_작성_요청 = new CommentRequest("첫 댓글 달았어요 :)", false);
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(댓글_작성_요청, 로그인된_댓글_작성자의_토큰.getAccessToken());
        CommentRequest 대댓글_작성요청 = new CommentRequest("로그인 안하고 대댓글 달고싶은데..", false);

        // when
        ExtractableResponse<Response> 로그인_하지않고_대댓글_작성_응답 = 로그인_하지않고_대댓글을_작성한다(대댓글_작성요청, 등록된_댓글.getId());

        // then
        요청_실패함(로그인_하지않고_대댓글_작성_응답, HttpStatus.UNAUTHORIZED, ErrorType.TOKEN_NEEDED);
    }

    @DisplayName("로그인한 유저가 댓글에 대댓글을 작성한다.")
    @Test
    public void createReplyWithLoginUser() {
        // given
        CommentRequest 댓글_작성_요청 = new CommentRequest("첫 댓글 달았어요 :)", false);
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(댓글_작성_요청, 로그인된_댓글_작성자의_토큰.getAccessToken());
        CommentRequest 대댓글_작성요청 = new CommentRequest("저도 첫 번째 댓글 하고싶었는데 ㅠㅠ", false);

        // when
        ExtractableResponse<Response> 대댓글_작성_응답 = 대댓글을_작성한다(대댓글_작성요청, 로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_댓글.getId());

        // then
        대댓글_작성_성공함(대댓글_작성_응답, 대댓글_작성요청);
    }

    @DisplayName("로그인한 유저가 댓글에 대댓글을 작성할 때 내용이 빈 값이라면 예외가 발생한다.")
    @Test
    public void createReplyWithBlankContent() {
        // given
        CommentRequest 댓글_작성_요청 = new CommentRequest("첫 댓글 달았어요 :)", false);
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(댓글_작성_요청, 로그인된_댓글_작성자의_토큰.getAccessToken());
        CommentRequest 대댓글_내용이_빈_값인_작성요청 = new CommentRequest("", false);

        // when
        ExtractableResponse<Response> 대댓글_내용이_빈_값인_작성 = 대댓글을_작성한다(대댓글_내용이_빈_값인_작성요청, 로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_댓글.getId());

        // then
        요청_실패함(대댓글_내용이_빈_값인_작성, HttpStatus.BAD_REQUEST, ErrorType.DATA_BINDING_ERROR);
    }

    @DisplayName("로그인한 유저가 댓글에 대댓글을 작성할 때 도와줄게요로 설정할 수 없다.")
    @Test
    public void createReplyWithHelperIsTrue() {
        // given
        CommentRequest 댓글_작성_요청 = new CommentRequest("첫 댓글 달았어요 :)", false);
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(댓글_작성_요청, 로그인된_댓글_작성자의_토큰.getAccessToken());
        CommentRequest 대댓글_내용이_빈_값인_작성요청 = new CommentRequest("대댓글로도 도와줄게요 설정하고 싶어요 ㅠㅠ", true);

        // when
        ExtractableResponse<Response> 대댓글_내용이_빈_값인_작성 = 대댓글을_작성한다(대댓글_내용이_빈_값인_작성요청, 로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_댓글.getId());

        // then
        요청_실패함(대댓글_내용이_빈_값인_작성, HttpStatus.BAD_REQUEST, ErrorType.REPLY_NOT_SUPPORTED_HELPER);
    }

    @DisplayName("로그인 하지않고 댓글에 있는 모든 대댓글을 최신순으로 조회한다.")
    @Test
    public void findAllRepliesByCommentIdWithoutLogin() {
        // given
        CommentRequest 댓글_작성_요청 = new CommentRequest("첫 댓글 달았어요 :)", false);
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(댓글_작성_요청, 로그인된_댓글_작성자의_토큰.getAccessToken());

        CommentRequest 대댓글_작성_요청1 = new CommentRequest("첫 댓글 1등 대댓글임", false);
        CommentRequest 대댓글_작성_요청2 = new CommentRequest("2등 대댓글 오히려 좋아", false);
        CommentResponse 등록된_대댓글1 = 대댓글_등록되어_있음(대댓글_작성_요청1, 로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_댓글.getId());
        CommentResponse 등록된_대댓글2 = 대댓글_등록되어_있음(대댓글_작성_요청2, 로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_댓글.getId());
        댓글에_좋아요를_추가한다(로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_대댓글1.getId());

        // when
        ExtractableResponse<Response> 대댓글_목록_조회_응답 = 로그인_하지않고_대댓글_목록_조회한다(등록된_댓글.getId());

        // then
        대댓글_목록_조회_성공(대댓글_목록_조회_응답, 2, 등록된_대댓글2, 등록된_대댓글1);
    }

    @DisplayName("로그인 하고 피드에 있는 모든 댓글을 최신순으로 조회한다.")
    @Test
    public void findAllRepliesByCommentIdWithLoginUser() {
        // given
        CommentRequest 댓글_작성_요청 = new CommentRequest("첫 댓글 달았어요 :)", false);
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(댓글_작성_요청, 로그인된_댓글_작성자의_토큰.getAccessToken());

        CommentRequest 대댓글_작성_요청1 = new CommentRequest("첫 댓글 1등 대댓글임", false);
        CommentRequest 대댓글_작성_요청2 = new CommentRequest("2등 대댓글 오히려 좋아", false);
        CommentResponse 등록된_대댓글1 = 대댓글_등록되어_있음(대댓글_작성_요청1, 로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_댓글.getId());
        CommentResponse 등록된_대댓글2 = 대댓글_등록되어_있음(대댓글_작성_요청2, 로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_댓글.getId());
        댓글에_좋아요를_추가한다(로그인된_댓글_작성자의_토큰.getAccessToken(), 등록된_대댓글1.getId());

        // when
        ExtractableResponse<Response> 대댓글_목록_조회_응답 = 로그인_하지않고_대댓글_목록_조회한다(등록된_댓글.getId());

        // then
        대댓글_목록_조회_성공(대댓글_목록_조회_응답, 2, 등록된_대댓글2, 등록된_대댓글1);
    }

    private void 댓글_작성_성공함(ExtractableResponse<Response> response, CommentRequest request) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();

        CommentResponse commentResponse = response.as(CommentResponse.class);
        assertThat(commentResponse.getContent()).isEqualTo(request.getContent());
        assertThat(commentResponse.isHelper()).isEqualTo(request.isHelper());
    }

    private void 요청_실패함(ExtractableResponse<Response> response, HttpStatus status, ErrorType errorType) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(response.statusCode()).isEqualTo(status.value());
        assertThat(exceptionResponse.getErrorCode()).isEqualTo(errorType.getErrorCode());
    }

    private void 댓글_목록_조회_성공(ExtractableResponse<Response> response, int commentCount, CommentResponse firstComment, int order) {
        CommentResponse[] commentResponses = response.as(CommentResponse[].class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(commentResponses).hasSize(commentCount);
        CommentResponse 댓글_응답 =  commentResponses[order];
        assertThat(댓글_응답.getId()).isEqualTo(firstComment.getId());
        assertThat(댓글_응답.isFeedAuthor()).isTrue();
    }

    private void 댓글_목록_조회시_이미_좋아요누른_댓글은_좋아요를_누른것으로_표시된다(ExtractableResponse<Response> response, int commentCount, CommentResponse commentResponse, int order) {
        CommentResponse[] commentResponses = response.as(CommentResponse[].class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(commentResponses).hasSize(commentCount);
        CommentResponse 이미_좋아요_누른_댓글 = commentResponses[order];
        assertThat(이미_좋아요_누른_댓글.getId()).isEqualTo(commentResponse.getId());
        assertThat(이미_좋아요_누른_댓글.isLiked()).isTrue();
    }

    private void 댓글_수정_성공함(ExtractableResponse<Response> 댓글_수정_응답, CommentRequest 댓글_수정_요청) {
        CommentResponse commentResponse = 댓글_수정_응답.as(CommentResponse.class);

        assertThat(댓글_수정_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(commentResponse.getContent()).isEqualTo(댓글_수정_요청.getContent());
        assertThat(commentResponse.isHelper()).isEqualTo(댓글_수정_요청.isHelper());
        assertThat(commentResponse.isModified()).isTrue();
    }

    private void 댓글에_좋아요_추가_성공(ExtractableResponse<Response> 댓글_좋아요_추가_응답) {
        assertThat(댓글_좋아요_추가_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 댓글에_좋아요_취소_성공(ExtractableResponse<Response> 댓글에_좋아요를_취소_응답) {
        assertThat(댓글에_좋아요를_취소_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 댓글_삭제_성공함(ExtractableResponse<Response> 댓글_삭제_응답) {
        assertThat(댓글_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 대댓글_작성_성공함(ExtractableResponse<Response> response, CommentRequest request) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();

        CommentResponse commentResponse = response.as(CommentResponse.class);
        assertThat(commentResponse.getContent()).isEqualTo(request.getContent());
        assertThat(commentResponse.isHelper()).isEqualTo(request.isHelper());
    }

    public ExtractableResponse<Response> 댓글을_작성한다(CommentRequest request, String accessToken) {
        return given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 업로드한_피드의_ID)
                .when().post("/feeds/{feedId}/comments")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 로그인_하지않고_댓글을_작성한다(CommentRequest request) {
        return given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 업로드한_피드의_ID)
                .when().post("/feeds/{feedId}/comments")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 로그인_하지않고_댓글_목록_조회한다(Long feedId) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", feedId)
                .when().get("/feeds/{feedId}/comments")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 로그인_하고_댓글_목록_조회한다(String accessToken, Long feedId) {
        return given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", feedId)
                .when().get("/feeds/{feedId}/comments")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 대댓글을_작성한다(CommentRequest request, String accessToken, Long 부모댓글ID) {
        return given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 업로드한_피드의_ID)
                .pathParam("commentId", 부모댓글ID)
                .when().post("/feeds/{feedId}/comments/{commentId}/replies")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 로그인_하지않고_대댓글을_작성한다(CommentRequest request, Long 부모댓글ID) {
        return given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 업로드한_피드의_ID)
                .pathParam("commentId", 부모댓글ID)
                .when().post("/feeds/{feedId}/comments/{commentId}/replies")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 댓글에_좋아요를_추가한다(String accessToken, Long 등록된_댓글_ID) {
        return given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 업로드한_피드의_ID)
                .pathParam("commentId", 등록된_댓글_ID)
                .when().post("/feeds/{feedId}/comments/{commentId}/like")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 로그인_하지않고_댓글에_좋아요를_추가한다(Long commentId) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 업로드한_피드의_ID)
                .pathParam("commentId", commentId)
                .when().post("/feeds/{feedId}/comments/{commentId}/like")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 댓글에_좋아요를_취소한다(String accessToken, Long 등록된_댓글_ID) {
        return given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 업로드한_피드의_ID)
                .pathParam("commentId", 등록된_댓글_ID)
                .when().post("/feeds/{feedId}/comments/{commentId}/unlike")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 로그인_하지않고_댓글에_좋아요를_취소한다(Long 등록된_댓글_ID) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 업로드한_피드의_ID)
                .pathParam("commentId", 등록된_댓글_ID)
                .when().post("/feeds/{feedId}/comments/{commentId}/unlike")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 댓글을_수정한다(CommentRequest 댓글_수정_요청, String accessToken, Long 수정할_피드_ID) {
        return given().log().all()
                .auth().oauth2(accessToken)
                .body(댓글_수정_요청)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 업로드한_피드의_ID)
                .pathParam("commentId", 수정할_피드_ID)
                .when().patch("/feeds/{feedId}/comments/{commentId}")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 댓글을_삭제한다(String accessToken, Long 삭제할_피드_ID) {
        return given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 업로드한_피드의_ID)
                .pathParam("commentId", 삭제할_피드_ID)
                .when().delete("/feeds/{feedId}/comments/{commentId}")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 로그인_하지않고_대댓글_목록_조회한다(Long 등록한_댓글_ID) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 업로드한_피드의_ID)
                .pathParam("commentId", 등록한_댓글_ID)
                .when().get("/feeds/{feedId}/comments/{commentId}/replies")
                .then().log().all()
                .extract();
    }

    private void 대댓글_목록_조회_성공(ExtractableResponse<Response> response, int replyCount, CommentResponse firstReply, CommentResponse secondReply) {
        ReplyResponse[] commentResponses = response.as(ReplyResponse[].class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(commentResponses).hasSize(replyCount);
        assertThat(commentResponses[0].getId()).isEqualTo(firstReply.getId());
        assertThat(commentResponses[1].getId()).isEqualTo(secondReply.getId());
    }

    private void 로그인_하고_대댓글_목록_조회_성공(ExtractableResponse<Response> response, int replyCount, CommentResponse firstReply, CommentResponse secondReply) {
        ReplyResponse[] commentResponses = response.as(ReplyResponse[].class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(commentResponses).hasSize(replyCount);
        assertThat(commentResponses[0].getId()).isEqualTo(firstReply.getId());
        assertThat(commentResponses[0].isLiked()).isFalse();
        assertThat(commentResponses[1].getId()).isEqualTo(secondReply.getId());
        assertThat(commentResponses[1].isLiked()).isTrue();

    }

    public CommentResponse 댓글_등록되어_있음(CommentRequest request, String accessToken) {
        return 댓글을_작성한다(request, accessToken).as(CommentResponse.class);
    }

    public CommentResponse 대댓글_등록되어_있음(CommentRequest request, String accessToken, Long commentId) {
        return 대댓글을_작성한다(request, accessToken, commentId).as(CommentResponse.class);
    }

    public Long 피드_업로드되어_있음(FeedRequest request, TokenResponse tokenResponse) {
        return Long.valueOf(피드를_작성한다(request, tokenResponse.getAccessToken()).header("Location").replace("/feeds/", ""));
    }

    public ExtractableResponse<Response> 피드를_작성한다(FeedRequest feedRequest, String token) {
        RequestSpecification requestSpecification = given().log().all()
                .auth().oauth2(token)
                .formParam("title", feedRequest.getTitle())
                .formParam("content", feedRequest.getContent())
                .formParam("step", feedRequest.getStep())
                .formParam("sos", feedRequest.isSos())
                .formParam("StorageUrl", feedRequest.getStorageUrl())
                .formParam("DeployedUrl", feedRequest.getDeployedUrl())
                .multiPart("thumbnailImage", thumbnail);

        feedRequest.getTechs()
                .forEach(techId -> requestSpecification.formParam("techs", techId));

        return requestSpecification
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .post("/feeds")
                .then().log().all()
                .extract();
    }
}
