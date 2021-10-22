package com.wooteco.nolto.acceptance;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.auth.ui.dto.AllTokenResponse;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.dto.ExceptionResponse;
import com.wooteco.nolto.feed.ui.dto.CommentRequest;
import com.wooteco.nolto.feed.ui.dto.CommentResponse;
import com.wooteco.nolto.feed.ui.dto.ReplyResponse;
import com.wooteco.nolto.user.domain.User;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.wooteco.nolto.UserFixture.찰리_생성;
import static com.wooteco.nolto.acceptance.FeedAcceptanceTest.진행중_단계의_피드_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("댓글 관련 기능")
class CommentAcceptanceTest extends AcceptanceTest {

    protected static final CommentRequest 일반_댓글_작성요청 = new CommentRequest("첫 댓글 달았어요 :)", false);
    protected static final CommentRequest 도와줄게요_댓글_작성요청 = new CommentRequest("1등 아깝다..", true);
    private static final CommentRequest 내용이_비어있는_댓글_작성요청 = new CommentRequest("", true);

    private static final CommentRequest 대댓글_작성_요청1 = new CommentRequest("첫 댓글 1등 대댓글임", false);
    private static final CommentRequest 대댓글_작성_요청2 = new CommentRequest("2등 대댓글 오히려 좋아", false);

    private User 댓글_작성자 = 찰리_생성();
    private Long 업로드한_피드의_ID;
    private AllTokenResponse 로그인된_댓글_작성자의_토큰;
    private AllTokenResponse 현재_로그인된_댓글_작성자의_토큰;

    @BeforeEach
    void setUpOnCommentAcceptanceTest() {
        super.setUp();
        로그인된_댓글_작성자의_토큰 = 댓글_작성자_로그인_되어있음();
        업로드한_피드의_ID = 피드_업로드되어_있음(진행중_단계의_피드_요청, 로그인된_댓글_작성자의_토큰.getAccessToken().getValue());
    }

    @AfterEach
    void clearOnCommentAcceptanceTest() {
        super.clear();
    }

    @DisplayName("게스트가 댓글 작성을 요청하면 예외가 발생한다.")
    @Test
    void createCommentWithoutLogin() {
        // given
        CommentRequest 일반_댓글_작성요청 = new CommentRequest("로그인 하지 않고 댓글을 쓰고싶어요", false);

        // when
        ExtractableResponse<Response> 실패한_댓글_작성_응답 = 로그인_하지않고_댓글을_작성한다(일반_댓글_작성요청, 업로드한_피드의_ID);

        // then
        요청_실패함(실패한_댓글_작성_응답, HttpStatus.UNAUTHORIZED, ErrorType.TOKEN_NEEDED);
    }

    @DisplayName("유효하지 않은 토큰으로 댓글 작성을 요청하면 예외가 발생한다.")
    @Test
    void createCommentWithInvalidToken() {
        // given
        CommentRequest 일반_댓글_작성요청 = new CommentRequest("로그인 하지 않고 댓글을 쓰고싶어요", false);

        // when
        ExtractableResponse<Response> 실패한_댓글_작성_응답 = 댓글을_작성한다(일반_댓글_작성요청, "유통기한이 지난 쉰내나는 토큰", 업로드한_피드의_ID);

        // then
        요청_실패함(실패한_댓글_작성_응답, HttpStatus.UNAUTHORIZED, ErrorType.INVALID_TOKEN);
    }

    @DisplayName("멤버가 피드에 도와줄게요가 아닌 일반 댓글을 작성한다.")
    @Test
    void createComment() {
        // given
        현재_로그인된_댓글_작성자의_토큰 = 댓글_작성자_로그인_되어있음();
        CommentRequest 일반_댓글_작성요청 = new CommentRequest("와 너무 멋진 프로젝트네요", false);

        // when
        ExtractableResponse<Response> 일반_댓글_작성_응답 = 댓글을_작성한다(일반_댓글_작성요청, 현재_로그인된_댓글_작성자의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID);

        // then
        댓글_작성_성공함(일반_댓글_작성_응답, 일반_댓글_작성요청);
    }

    @DisplayName("멤버가 피드에 도와줄게요 댓글을 작성한다. (댓글 내용은 1글자 이상이어야 한다.)")
    @Test
    void createCommentIsHelper() {
        // given
        현재_로그인된_댓글_작성자의_토큰 = 댓글_작성자_로그인_되어있음();

        // when
        ExtractableResponse<Response> 도와줄게요_댓글_작성_응답 = 댓글을_작성한다(도와줄게요_댓글_작성요청, 현재_로그인된_댓글_작성자의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID);
        ExtractableResponse<Response> 댓글내용이_비어있어_작성_실패한_응답 = 댓글을_작성한다(내용이_비어있는_댓글_작성요청, 현재_로그인된_댓글_작성자의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID);

        // then
        댓글_작성_성공함(도와줄게요_댓글_작성_응답, 도와줄게요_댓글_작성요청);
        요청_실패함(댓글내용이_비어있어_작성_실패한_응답, HttpStatus.BAD_REQUEST, ErrorType.DATA_BINDING_ERROR);
    }

    @DisplayName("게스트가 피드에 있는 모든 댓글을 최신순으로 조회한다. (대댓글은 응답에 포함되지 않음)")
    @Test
    void findAllCommentsByFeedId() {
        // given
        CommentResponse commentResponse1 = 댓글_등록되어_있음(일반_댓글_작성요청);
        CommentResponse commentResponse2 = 댓글_등록되어_있음(도와줄게요_댓글_작성요청);
        대댓글_등록되어_있음(대댓글_작성_요청1, commentResponse1.getId());
        댓글에_좋아요_추가_되어있음(commentResponse2.getId());

        // when
        ExtractableResponse<Response> response = 로그인_하지않고_댓글_목록_조회한다(업로드한_피드의_ID);

        // then
        로그인_하지않고_댓글_목록_조회_성공(response, 2, commentResponse2, 0);
    }

    @DisplayName("멤버가 이미 좋아요를 누른 댓글을 조회시 liked가 true다")
    @Test
    void findAllCommentsByFeedIdWithLikedComment() {
        // given
        CommentResponse commentResponse1 = 댓글_등록되어_있음(일반_댓글_작성요청);
        댓글_등록되어_있음(도와줄게요_댓글_작성요청);
        대댓글_등록되어_있음(대댓글_작성_요청1, commentResponse1.getId());
        댓글에_좋아요_추가_되어있음(commentResponse1.getId());

        현재_로그인된_댓글_작성자의_토큰 = 댓글_작성자_로그인_되어있음();

        // when
        ExtractableResponse<Response> response = 로그인_하고_댓글_목록_조회한다(현재_로그인된_댓글_작성자의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID);

        // then
        로그인하고_댓글_목록_조회_성공(response, 2, commentResponse1, 1);
    }

    @DisplayName("댓글 작성자가 자신의 댓글(또는 대댓글)을 수정한다.")
    @Test
    void updateCommentWithAuthor() {
        // given
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(일반_댓글_작성요청);

        현재_로그인된_댓글_작성자의_토큰 = 댓글_작성자_로그인_되어있음();
        final CommentRequest 댓글_수정_요청 = new CommentRequest("천천히 보다보니 수정할 부분이 보이네요", true);
        final CommentRequest 댓글내용_빈값으로_수정_요청 = new CommentRequest("", true);

        // when
        ExtractableResponse<Response> 댓글_수정_응답 = 댓글을_수정한다(댓글_수정_요청, 현재_로그인된_댓글_작성자의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID, 등록된_댓글.getId());
        ExtractableResponse<Response> 댓글내용_빈값으로_수정_응답 = 댓글을_수정한다(댓글내용_빈값으로_수정_요청, 현재_로그인된_댓글_작성자의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID, 등록된_댓글.getId());

        // then
        댓글_수정_성공함(댓글_수정_응답, 댓글_수정_요청);
        요청_실패함(댓글내용_빈값으로_수정_응답, HttpStatus.BAD_REQUEST, ErrorType.DATA_BINDING_ERROR);
    }

    @DisplayName("댓글 작성자가 아닌 멤버가 댓글을_수정한다 수정하려고 하면 예외가 발생한다.")
    @Test
    void updateCommentWithOtherUser() {
        // given
        final CommentRequest 댓글_수정_요청 = new CommentRequest("천천히 보다보니 수정할 부분이 보이네요", true);
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(일반_댓글_작성요청);
        AllTokenResponse 댓글_작성자가_아닌_유저의_토큰 = 댓글_작성자가_아닌_유저_로그인_되어있음();

        // when
        ExtractableResponse<Response> 댓글_작성자가_아닌_유저의_댓글_수정_응답 = 댓글을_수정한다(댓글_수정_요청, 댓글_작성자가_아닌_유저의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID, 등록된_댓글.getId());

        // then
        요청_실패함(댓글_작성자가_아닌_유저의_댓글_수정_응답, HttpStatus.UNAUTHORIZED, ErrorType.UNAUTHORIZED_UPDATE_COMMENT);
    }

    @DisplayName("대댓글 작성자가 대댓글을 수정할 수 있다.")
    @Test
    void updateReplyWithAuthor() {
        // given
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(일반_댓글_작성요청);
        CommentResponse 등록된_대댓글 = 대댓글_등록되어_있음(대댓글_작성_요청1, 등록된_댓글.getId());

        현재_로그인된_댓글_작성자의_토큰 = 댓글_작성자_로그인_되어있음();
        CommentRequest 대댓글_수정_요청 = new CommentRequest("대댓글도 수정이 되나요?", false);

        // when
        ExtractableResponse<Response> 대댓글_수정_응답 = 댓글을_수정한다(대댓글_수정_요청, 현재_로그인된_댓글_작성자의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID, 등록된_대댓글.getId());

        // then
        댓글_수정_성공함(대댓글_수정_응답, 대댓글_수정_요청);
    }

    @DisplayName("대댓글을 '도와드릴게요'로 수정하려고 하면 예외가 발생한다.")
    @Test
    void updateReplyWithHelperIsTrue() {
        // given
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(일반_댓글_작성요청);
        CommentResponse 등록된_대댓글 = 대댓글_등록되어_있음(대댓글_작성_요청1, 등록된_댓글.getId());

        현재_로그인된_댓글_작성자의_토큰 = 댓글_작성자_로그인_되어있음();
        CommentRequest 대댓글_도와드릴게요로_수정_요청 = new CommentRequest("대댓글도 수정이 되나요?", true);

        // when
        ExtractableResponse<Response> 도와드릴게요로_대댓글_수정_응답 = 댓글을_수정한다(대댓글_도와드릴게요로_수정_요청, 현재_로그인된_댓글_작성자의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID, 등록된_대댓글.getId());

        // then
        요청_실패함(도와드릴게요로_대댓글_수정_응답, HttpStatus.BAD_REQUEST, ErrorType.REPLY_NOT_SUPPORTED_HELPER);
    }

    @DisplayName("댓글을 작성한 멤버가 자신의 댓글을 삭제할 수 있다.")
    @Test
    void deleteCommentWithAuthor() {
        // given
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(일반_댓글_작성요청);

        현재_로그인된_댓글_작성자의_토큰 = 댓글_작성자_로그인_되어있음();

        // when
        ExtractableResponse<Response> 댓글_삭제_응답 = 댓글을_삭제한다(현재_로그인된_댓글_작성자의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID, 등록된_댓글.getId());

        // then
        댓글_삭제_성공함(댓글_삭제_응답);
    }

    @DisplayName("대댓글을 작성한 멤버가 자신의 대댓글을 삭제할 수 있다.")
    @Test
    void deleteReplyWithAuthor() {
        // given
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(일반_댓글_작성요청);
        CommentResponse 등록된_대댓글 = 대댓글_등록되어_있음(대댓글_작성_요청1, 등록된_댓글.getId());

        현재_로그인된_댓글_작성자의_토큰 = 댓글_작성자_로그인_되어있음();

        // when
        ExtractableResponse<Response> 대댓글_삭제_응답 = 댓글을_삭제한다(현재_로그인된_댓글_작성자의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID, 등록된_대댓글.getId());

        // then
        댓글_삭제_성공함(대댓글_삭제_응답);
    }

    @DisplayName("작성자 이외의 다른 멤버가 댓글을 삭제하려고 하면 예외가 발생한다.")
    @Test
    void deleteCommentWithOtherUser() {
        // given
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(일반_댓글_작성요청);

        AllTokenResponse 댓글_작성자가_아닌_유저의_토큰 = 댓글_작성자가_아닌_유저_로그인_되어있음();

        // when
        ExtractableResponse<Response> 댓글_삭제_응답 = 댓글을_삭제한다(댓글_작성자가_아닌_유저의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID, 등록된_댓글.getId());

        // then
        요청_실패함(댓글_삭제_응답, HttpStatus.UNAUTHORIZED, ErrorType.UNAUTHORIZED_DELETE_COMMENT);
    }

    @DisplayName("멤버가 댓글에 좋아요를 추가할 수 있다.")
    @Test
    void addCommentLikeWithLoginUser() {
        // given
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(일반_댓글_작성요청);

        현재_로그인된_댓글_작성자의_토큰 = 댓글_작성자_로그인_되어있음();

        // when
        ExtractableResponse<Response> 댓글_좋아요_추가_응답 = 댓글에_좋아요를_추가한다(현재_로그인된_댓글_작성자의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID, 등록된_댓글.getId());

        // then
        댓글에_좋아요_추가_성공(댓글_좋아요_추가_응답);
    }

    @DisplayName("멤버가 이미 좋아요를 누른 글에 좋아요를 추가하려고 하면 예외가 발생한다.")
    @Test
    void addCommentLikeWithAlreadyLiked() {
        // given
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(일반_댓글_작성요청);
        댓글에_좋아요_추가_되어있음(등록된_댓글.getId());

        현재_로그인된_댓글_작성자의_토큰 = 댓글_작성자_로그인_되어있음();

        // when
        ExtractableResponse<Response> 이미_좋아요_누른_댓글에_댓글_좋아요_추가_응답 = 댓글에_좋아요를_추가한다(현재_로그인된_댓글_작성자의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID, 등록된_댓글.getId());

        // then
        요청_실패함(이미_좋아요_누른_댓글에_댓글_좋아요_추가_응답, HttpStatus.BAD_REQUEST, ErrorType.ALREADY_LIKED_COMMENT);
    }

    @DisplayName("게스트가 댓글에 좋아요를 추가하려고 하면 예외가 발생한다.")
    @Test
    void addCommentLikeWithoutLogin() {
        // given
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(일반_댓글_작성요청);

        // when
        ExtractableResponse<Response> 로그인_하지않고_댓글_좋아요_추가_응답 = 로그인_하지않고_댓글에_좋아요를_추가한다(업로드한_피드의_ID, 등록된_댓글.getId());

        // then
        요청_실패함(로그인_하지않고_댓글_좋아요_추가_응답, HttpStatus.UNAUTHORIZED, ErrorType.TOKEN_NEEDED);
    }

    @DisplayName("멤버가 좋아요를 눌렀던 댓글에 좋아요를 취소할 수 있다.")
    @Test
    void deleteCommentLikeWithLoginUser() {
        // given
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(일반_댓글_작성요청);
        댓글에_좋아요_추가_되어있음(등록된_댓글.getId());

        현재_로그인된_댓글_작성자의_토큰 = 댓글_작성자_로그인_되어있음();

        // when
        ExtractableResponse<Response> 댓글에_좋아요_취소_응답 = 댓글에_좋아요를_취소한다(현재_로그인된_댓글_작성자의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID, 등록된_댓글.getId());

        // then
        댓글에_좋아요_취소_성공(댓글에_좋아요_취소_응답);
    }

    @DisplayName("게스트가 좋아요를 취소를 요청하면 예외가 발생한다.")
    @Test
    void deleteCommentLikeWithoutLogin() {
        // given
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(일반_댓글_작성요청);
        댓글에_좋아요_추가_되어있음(등록된_댓글.getId());

        // when
        ExtractableResponse<Response> 댓글에_좋아요_취소_응답 = 로그인_하지않고_댓글에_좋아요를_취소한다(업로드한_피드의_ID, 등록된_댓글.getId());

        // then
        요청_실패함(댓글에_좋아요_취소_응답, HttpStatus.UNAUTHORIZED, ErrorType.TOKEN_NEEDED);
    }

    @DisplayName("멤버가 좋아요를 누르지 않은 댓글에 좋아요 취소를 요청하면 예외가 발생한다.")
    @Test
    void deleteCommentLikeWithoutLiked() {
        // given
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(일반_댓글_작성요청);

        현재_로그인된_댓글_작성자의_토큰 = 댓글_작성자_로그인_되어있음();

        // when
        ExtractableResponse<Response> 댓글에_좋아요_취소_응답 = 댓글에_좋아요를_취소한다(현재_로그인된_댓글_작성자의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID, 등록된_댓글.getId());

        // then
        요청_실패함(댓글에_좋아요_취소_응답, HttpStatus.BAD_REQUEST, ErrorType.NOT_LIKED_COMMENT);
    }

    @DisplayName("게스트가 댓글에 대댓글 작성을 요청하면 예외가 발생한다.")
    @Test
    void createReplyWithoutLogin() {
        // given
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(일반_댓글_작성요청);

        // when
        ExtractableResponse<Response> 로그인_하지않고_대댓글_작성_응답 = 로그인_하지않고_대댓글을_작성한다(대댓글_작성_요청1, 업로드한_피드의_ID, 등록된_댓글.getId());

        // then
        요청_실패함(로그인_하지않고_대댓글_작성_응답, HttpStatus.UNAUTHORIZED, ErrorType.TOKEN_NEEDED);
    }

    @DisplayName("멤버가 댓글에 대댓글을 작성한다. (대댓글 내용이 빈값이면 예외가 발생)")
    @Test
    void createReplyWithLoginUser() {
        // given
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(일반_댓글_작성요청);

        현재_로그인된_댓글_작성자의_토큰 = 댓글_작성자_로그인_되어있음();
        CommentRequest 대댓글_작성요청 = new CommentRequest("저도 첫 번째 댓글 하고싶었는데 ㅠㅠ", false);
        CommentRequest 대댓글_내용이_빈_값인_작성요청 = new CommentRequest("", false);

        // when
        ExtractableResponse<Response> 대댓글_작성_응답 = 대댓글을_작성한다(대댓글_작성요청, 현재_로그인된_댓글_작성자의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID, 등록된_댓글.getId());
        ExtractableResponse<Response> 대댓글_내용이_빈_값인_작성 = 대댓글을_작성한다(대댓글_내용이_빈_값인_작성요청, 현재_로그인된_댓글_작성자의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID, 등록된_댓글.getId());

        // then
        대댓글_작성_성공함(대댓글_작성_응답, 대댓글_작성요청);
        요청_실패함(대댓글_내용이_빈_값인_작성, HttpStatus.BAD_REQUEST, ErrorType.DATA_BINDING_ERROR);
    }

    @DisplayName("게스트가 댓글에 있는 모든 대댓글을 최신순으로 조회한다.")
    @Test
    void findAllRepliesByCommentIdWithoutLogin() {
        // given
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(일반_댓글_작성요청);
        CommentResponse 등록된_대댓글1 = 대댓글_등록되어_있음(대댓글_작성_요청1, 등록된_댓글.getId());
        CommentResponse 등록된_대댓글2 = 대댓글_등록되어_있음(대댓글_작성_요청2, 등록된_댓글.getId());
        대댓글에_좋아요_추가_되어있음(등록된_대댓글1.getId());

        // when
        ExtractableResponse<Response> 대댓글_목록_조회_응답 = 로그인_하지않고_대댓글_목록_조회한다(업로드한_피드의_ID, 등록된_댓글.getId());

        // then
        로그인_하지않고_대댓글_목록_조회_성공(대댓글_목록_조회_응답, 2, 등록된_대댓글2, 등록된_대댓글1);
    }

    @DisplayName("멤버가 피드에 있는 모든 댓글을 최신순으로 조회한다.")
    @Test
    void findAllRepliesByCommentIdWithLoginUser() {
        // given
        CommentResponse 등록된_댓글 = 댓글_등록되어_있음(일반_댓글_작성요청);
        CommentResponse 등록된_대댓글1 = 대댓글_등록되어_있음(대댓글_작성_요청1, 등록된_댓글.getId());
        CommentResponse 등록된_대댓글2 = 대댓글_등록되어_있음(대댓글_작성_요청2, 등록된_댓글.getId());
        대댓글에_좋아요_추가_되어있음(등록된_대댓글1.getId());

        현재_로그인된_댓글_작성자의_토큰 = 댓글_작성자_로그인_되어있음();

        // when
        ExtractableResponse<Response> 대댓글_목록_조회_응답 = 로그인_하고_대댓글_목록_조회한다(현재_로그인된_댓글_작성자의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID, 등록된_댓글.getId());

        // then
        로그인_하고_대댓글_목록_조회_성공(대댓글_목록_조회_응답, 2, 등록된_대댓글2, 등록된_대댓글1);
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

    private void 로그인_하지않고_댓글_목록_조회_성공(ExtractableResponse<Response> response, int commentCount, CommentResponse firstComment, int order) {
        CommentResponse[] commentResponses = response.as(CommentResponse[].class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(commentResponses).hasSize(commentCount);
        CommentResponse 댓글_응답 = commentResponses[order];
        assertThat(댓글_응답.getId()).isEqualTo(firstComment.getId());
        assertThat(댓글_응답.isFeedAuthor()).isTrue();
        assertThat(댓글_응답.isLiked()).isFalse();
    }

    private void 로그인하고_댓글_목록_조회_성공(ExtractableResponse<Response> response, int commentCount, CommentResponse firstComment, int order) {
        CommentResponse[] commentResponses = response.as(CommentResponse[].class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(commentResponses).hasSize(commentCount);

        CommentResponse 댓글_응답 = commentResponses[order];
        assertThat(댓글_응답.getId()).isEqualTo(firstComment.getId());
        assertThat(댓글_응답.isFeedAuthor()).isTrue();
        assertThat(댓글_응답.isLiked()).isTrue();
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

    public static ExtractableResponse<Response> 댓글을_작성한다(CommentRequest request, String accessToken, Long 피드ID) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 피드ID)
                .when().post("/feeds/{feedId}/comments")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 로그인_하지않고_댓글을_작성한다(CommentRequest request, Long 피드ID) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 피드ID)
                .when().post("/feeds/{feedId}/comments")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 로그인_하지않고_댓글_목록_조회한다(Long 피드ID) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 피드ID)
                .when().get("/feeds/{feedId}/comments")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 로그인_하고_댓글_목록_조회한다(String accessToken, Long 피드ID) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 피드ID)
                .when().get("/feeds/{feedId}/comments")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 대댓글을_작성한다(CommentRequest request, String accessToken, Long 피드ID, Long 부모댓글ID) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 피드ID)
                .pathParam("commentId", 부모댓글ID)
                .when().post("/feeds/{feedId}/comments/{commentId}/replies")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 로그인_하지않고_대댓글을_작성한다(CommentRequest request, Long 피드ID, Long 부모댓글ID) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 피드ID)
                .pathParam("commentId", 부모댓글ID)
                .when().post("/feeds/{feedId}/comments/{commentId}/replies")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 댓글에_좋아요를_추가한다(String accessToken, Long 피드ID, Long 등록된_댓글_ID) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 피드ID)
                .pathParam("commentId", 등록된_댓글_ID)
                .when().post("/feeds/{feedId}/comments/{commentId}/like")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 로그인_하지않고_댓글에_좋아요를_추가한다(Long 피드ID, Long 등록된_댓글_ID) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 피드ID)
                .pathParam("commentId", 등록된_댓글_ID)
                .when().post("/feeds/{feedId}/comments/{commentId}/like")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 댓글에_좋아요를_취소한다(String accessToken, Long 피드ID, Long 등록된_댓글_ID) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 피드ID)
                .pathParam("commentId", 등록된_댓글_ID)
                .when().post("/feeds/{feedId}/comments/{commentId}/unlike")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 로그인_하지않고_댓글에_좋아요를_취소한다(Long 피드ID, Long 등록된_댓글_ID) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 피드ID)
                .pathParam("commentId", 등록된_댓글_ID)
                .when().post("/feeds/{feedId}/comments/{commentId}/unlike")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 댓글을_수정한다(CommentRequest 댓글_수정_요청, String accessToken, Long 피드ID, Long 수정할_댓글_ID) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .body(댓글_수정_요청)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 피드ID)
                .pathParam("commentId", 수정할_댓글_ID)
                .when().patch("/feeds/{feedId}/comments/{commentId}")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 댓글을_삭제한다(String accessToken, Long 피드ID, Long 삭제할_댓글_ID) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 피드ID)
                .pathParam("commentId", 삭제할_댓글_ID)
                .when().delete("/feeds/{feedId}/comments/{commentId}")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 로그인_하지않고_대댓글_목록_조회한다(Long 피드ID, Long 등록한_댓글_ID) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 피드ID)
                .pathParam("commentId", 등록한_댓글_ID)
                .when().get("/feeds/{feedId}/comments/{commentId}/replies")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 로그인_하고_대댓글_목록_조회한다(String accessToken, Long 피드ID, Long 등록한_댓글_ID) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("feedId", 피드ID)
                .pathParam("commentId", 등록한_댓글_ID)
                .when().get("/feeds/{feedId}/comments/{commentId}/replies")
                .then().log().all()
                .extract();
    }

    private void 로그인_하지않고_대댓글_목록_조회_성공(ExtractableResponse<Response> response, int replyCount, CommentResponse firstReply, CommentResponse secondReply) {
        ReplyResponse[] commentResponses = response.as(ReplyResponse[].class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(commentResponses).hasSize(replyCount);
        assertThat(commentResponses[0].getId()).isEqualTo(firstReply.getId());
        assertThat(commentResponses[0].isLiked()).isFalse();
        assertThat(commentResponses[0].isFeedAuthor()).isTrue();

        assertThat(commentResponses[1].getId()).isEqualTo(secondReply.getId());
        assertThat(commentResponses[1].isLiked()).isFalse();
        assertThat(commentResponses[1].isFeedAuthor()).isTrue();
    }

    private void 로그인_하고_대댓글_목록_조회_성공(ExtractableResponse<Response> response, int replyCount, CommentResponse firstReply, CommentResponse secondReply) {
        ReplyResponse[] commentResponses = response.as(ReplyResponse[].class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(commentResponses).hasSize(replyCount);
        assertThat(commentResponses[0].getId()).isEqualTo(firstReply.getId());
        assertThat(commentResponses[0].isLiked()).isFalse();
        assertThat(commentResponses[0].isFeedAuthor()).isTrue();

        assertThat(commentResponses[1].getId()).isEqualTo(secondReply.getId());
        assertThat(commentResponses[1].isLiked()).isTrue();
        assertThat(commentResponses[1].isFeedAuthor()).isTrue();
    }

    private CommentResponse 댓글_등록되어_있음(CommentRequest request) {
        return 댓글을_작성한다(request, 로그인된_댓글_작성자의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID).as(CommentResponse.class);
    }

    public static CommentResponse 댓글_등록되어_있음(CommentRequest request, String token, Long feedId) {
        return 댓글을_작성한다(request, token, feedId).as(CommentResponse.class);
    }

    private void 댓글에_좋아요_추가_되어있음(Long 등록된_댓글_ID) {
        댓글에_좋아요를_추가한다(로그인된_댓글_작성자의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID, 등록된_댓글_ID);
    }

    private void 대댓글에_좋아요_추가_되어있음(Long 등록된_댓글_ID) {
        댓글에_좋아요를_추가한다(로그인된_댓글_작성자의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID, 등록된_댓글_ID);
    }

    private CommentResponse 대댓글_등록되어_있음(CommentRequest request, Long commentId) {
        return 대댓글을_작성한다(request, 로그인된_댓글_작성자의_토큰.getAccessToken().getValue(), 업로드한_피드의_ID, commentId).as(CommentResponse.class);
    }

    private AllTokenResponse 댓글_작성자_로그인_되어있음() {
        User 회원_등록된_댓글_작성자 = 회원_등록되어_있음(댓글_작성자);
        return 유저의_토큰을_받는다(회원_등록된_댓글_작성자);
    }

    private AllTokenResponse 댓글_작성자가_아닌_유저_로그인_되어있음() {
        User 댓글_작성자가_아닌_유저 = 회원_등록되어_있음(new User("악마같은 GITHUB ID", SocialType.GITHUB, "김악질", "사탄.jpg"));
        return 유저의_토큰을_받는다(댓글_작성자가_아닌_유저);
    }
}
