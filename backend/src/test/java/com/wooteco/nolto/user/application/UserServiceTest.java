package com.wooteco.nolto.user.application;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Like;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import com.wooteco.nolto.user.ui.dto.CommentHistoryResponse;
import com.wooteco.nolto.user.ui.dto.FeedHistoryResponse;
import com.wooteco.nolto.user.ui.dto.MemberHistoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private EntityManager em;

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
        joelFeed.addComment(joelCommentsJoelFeed);
        amaFeed.addComment(joelCommentsAmaFeed);
        joel.addLike(joelLikesJoelFeed);
        joel.addLike(joelLikesAmaFeed);
        userRepository.save(ama);
        userRepository.save(joel);

        em.flush();
        em.clear();
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
