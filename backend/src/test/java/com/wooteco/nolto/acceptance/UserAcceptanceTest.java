package com.wooteco.nolto.acceptance;

import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import com.wooteco.nolto.exception.dto.ExceptionResponse;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Like;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.ui.dto.CommentHistoryResponse;
import com.wooteco.nolto.user.ui.dto.FeedHistoryResponse;
import com.wooteco.nolto.user.ui.dto.MemberHistoryResponse;
import com.wooteco.nolto.user.ui.dto.MemberResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("회원 관련 기능")
public class UserAcceptanceTest extends AcceptanceTest {

    @DisplayName("로그인 된 사용자라면 회원 정보를 받아올 수 있다.")
    @Test
    void getMemberInfoWithToken() {
        //given
        TokenResponse userToken = 가입된_유저의_토큰을_받는다();

        //when
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(userToken);

        //then
        알맞은_회원_정보_조회됨(response, 엄청난_유저);
    }

    @DisplayName("로그인 되지 않은 사용자라면 회원 정보를 받아올 수 없다.")
    @Test
    void cannotGetMemberInfoWithoutToken() {
        //when
        ExtractableResponse<Response> response = 토큰_없이_회원_정보_요청("/members/me");

        //then
        토큰_필요_예외_발생(response);
    }

    @DisplayName("로그인 된 사용자는 자신의 히스토리(좋아요 한 글, 내가 작성한 글, 내가 남긴 댓글)를 조회할 수 있다.")
    @Test
    void findMemberHistory() {
        //given
        Feed feed = new Feed("title", "content", Step.PROGRESS, true, "https://github.com/nolto", "", "https://cloudfront.net/image.png").writtenBy(엄청난_유저);
        Comment comment = new Comment("comment", true).writtenBy(엄청난_유저, feed);
        엄청난_유저.addLike(new Like(엄청난_유저, feed));
        userRepository.saveAndFlush(엄청난_유저);
        TokenResponse userToken = 가입된_유저의_토큰을_받는다(엄청난_유저);

        //when
        ExtractableResponse<Response> response = 내_히스토리_조회_요청(userToken);

        //then
        회원_히스토리_조회됨(response, 엄청난_유저);
    }

    @DisplayName("로그인 되지 않은 사용자라면 회원의 히스토리를 받아올 수 없다.")
    @Test
    void cannotFindMemberHistoryWithoutToken() {
        //when
        ExtractableResponse<Response> response = 토큰_없이_회원_정보_요청("/members/me/history");

        //then
        토큰_필요_예외_발생(response);
    }

    public ExtractableResponse<Response> 내_회원_정보_조회_요청(TokenResponse tokenResponse) {
        return given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public void 알맞은_회원_정보_조회됨(ExtractableResponse<Response> response, User expectedUser) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getNickname()).isEqualTo(expectedUser.getNickName());
        assertThat(memberResponse.getImageUrl()).isEqualTo(expectedUser.getImageUrl());
    }

    public ExtractableResponse<Response> 토큰_없이_회원_정보_요청(String urlPath) {
        return given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(urlPath)
                .then()
                .log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    public void 토큰_필요_예외_발생(ExtractableResponse<Response> response) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getErrorCode()).isEqualTo("auth-002");
        assertThat(exceptionResponse.getMessage()).isEqualTo("토큰이 필요합니다.");
    }

    private ExtractableResponse<Response> 내_히스토리_조회_요청(TokenResponse tokenResponse) {
        return given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me/history")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private void 회원_히스토리_조회됨(ExtractableResponse<Response> response, User expectedUser) {
        MemberHistoryResponse memberHistoryResponse = response.as(MemberHistoryResponse.class);
        알맞은_피드_조회됨(memberHistoryResponse.getLikedFeeds(), expectedUser.findLikedFeeds());
        알맞은_피드_조회됨(memberHistoryResponse.getMyFeeds(), expectedUser.getFeeds());
        알맞은_댓글_조회됨(memberHistoryResponse.getMyComments(), expectedUser.getComments());
    }

    private void 알맞은_피드_조회됨(List<FeedHistoryResponse> feedHistoryResponses, List<Feed> feeds) {
        List<String> responseTitles = feedHistoryResponses.stream().map(FeedHistoryResponse::getTitle).collect(Collectors.toList());
        List<String> feedTitles = feeds.stream().map(Feed::getTitle).collect(Collectors.toList());
        assertThat(responseTitles).isEqualTo(feedTitles);
    }

    private void 알맞은_댓글_조회됨(List<CommentHistoryResponse> commentHistoryResponses, List<Comment> comments) {
        List<String> responseTexts = commentHistoryResponses.stream().map(CommentHistoryResponse::getText).collect(Collectors.toList());
        List<String> contents = comments.stream().map(Comment::getContent).collect(Collectors.toList());
        assertThat(responseTexts).isEqualTo(contents);
    }
}
