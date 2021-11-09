package com.wooteco.nolto.admin.application;

import com.wooteco.nolto.admin.ui.dto.CommentsByFeedResponse;
import com.wooteco.nolto.admin.ui.dto.UserResponse;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.UnauthorizedException;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.repository.CommentRepository;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import com.wooteco.nolto.tech.ui.dto.TechResponse;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.wooteco.nolto.FeedFixture.진행중_단계의_피드_생성;
import static com.wooteco.nolto.TechFixture.*;
import static com.wooteco.nolto.UserFixture.조엘_생성;
import static com.wooteco.nolto.UserFixture.찰리_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TechRepository techRepository;

    @Autowired
    private EntityManager em;

    private User 조엘 = 조엘_생성();
    private User 찰리 = 찰리_생성();

    private Tech 자바 = 자바_생성();
    private Tech 스프링 = 스프링_생성();
    private Tech 리액트 = 리액트_생성();

    @BeforeEach
    void setUp() {
        userRepository.save(조엘);
        userRepository.save(찰리);

        techRepository.save(스프링);
        techRepository.save(자바);
        techRepository.save(리액트);
    }

    @DisplayName("어드민 유저는 전체 피드 조회를 할 수 있다")
    @Test
    void findAllFeeds() {
        //given
        Feed 피드1 = 진행중_단계의_피드_생성("피드1", "피드1").writtenBy(조엘);
        feedRepository.save(피드1);
        Feed 피드2 = 진행중_단계의_피드_생성("피드2", "피드2").writtenBy(조엘);
        feedRepository.save(피드2);
        Feed 피드3 = 진행중_단계의_피드_생성("피드3", "피드3").writtenBy(조엘);
        feedRepository.save(피드3);

        //when
        List<FeedResponse> allFeeds = adminService.findAllFeeds(User.ADMIN_USER);

        //then
        assertThat(allFeeds).hasSize(3);
    }

    @DisplayName("어드민 유저가 아니라면 전체 피드 조회를 할 수 없다")
    @Test
    void findAllNotAsAdmin() {
        //given
        Feed 피드1 = 진행중_단계의_피드_생성("피드1", "피드1").writtenBy(조엘);
        feedRepository.save(피드1);
        Feed 피드2 = 진행중_단계의_피드_생성("피드2", "피드2").writtenBy(조엘);
        feedRepository.save(피드2);
        Feed 피드3 = 진행중_단계의_피드_생성("피드3", "피드3").writtenBy(조엘);
        feedRepository.save(피드3);

        //when & then
        assertThatThrownBy(() -> adminService.findAllFeeds(찰리))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(ErrorType.ADMIN_ONLY.getMessage());

        assertThatThrownBy(() -> adminService.findAllFeeds(User.GUEST_USER))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(ErrorType.ADMIN_ONLY.getMessage());
    }

    @DisplayName("어드민 유저는 누구의 피드라도 수정할 수 있다.")
    @Test
    void updateFeed() {
        //given
        Feed 피드1 = 진행중_단계의_피드_생성("피드1", "피드1").writtenBy(조엘);
        feedRepository.save(피드1);

        //when
        FeedRequest SPRING_JAVA_FEED_REQUEST = new FeedRequest("수정된피드1", new ArrayList<>(), "수정된피드1", "PROGRESS", true,
                "www.github.com/woowacourse", "www.github.com/woowacourse", null);
        SPRING_JAVA_FEED_REQUEST.setTechs(Arrays.asList(스프링.getId(), 자바.getId()));
        adminService.updateFeed(User.ADMIN_USER, 피드1.getId(), SPRING_JAVA_FEED_REQUEST);

        //then
        Feed updatedFeed = feedRepository.findById(피드1.getId()).get();
        assertThat(updatedFeed.getTitle()).isEqualTo("수정된피드1");
        assertThat(updatedFeed.getContent()).isEqualTo("수정된피드1");
        assertThat(updatedFeed.getTechs().get(0).getId()).isEqualTo(스프링.getId());
        assertThat(updatedFeed.getTechs().get(1).getId()).isEqualTo(자바.getId());
    }

    @DisplayName("어드민 유저는 누구의 피드라도 삭제할 수 있다")
    @Test
    void deleteFeed() {
        //given
        Feed 피드1 = 진행중_단계의_피드_생성("피드1", "피드1").writtenBy(조엘);
        feedRepository.save(피드1);
        Feed 피드2 = 진행중_단계의_피드_생성("피드2", "피드2").writtenBy(조엘);
        feedRepository.save(피드2);
        Feed 피드3 = 진행중_단계의_피드_생성("피드3", "피드3").writtenBy(조엘);
        feedRepository.save(피드3);

        //when
        adminService.deleteFeed(User.ADMIN_USER, 피드1.getId());

        //then
        Optional<Feed> deletedFeed = feedRepository.findById(피드1.getId());
        assertThat(deletedFeed).isNotPresent();
    }

    @DisplayName("어드민 유저가 아니라면 어드민 권한으로 피드 삭제할 수 없다")
    @Test
    void deleteFeedNotAsAdmin() {
        //given
        Feed 피드1 = 진행중_단계의_피드_생성("피드1", "피드1").writtenBy(조엘);
        feedRepository.save(피드1);
        Feed 피드2 = 진행중_단계의_피드_생성("피드2", "피드2").writtenBy(조엘);
        feedRepository.save(피드2);
        Feed 피드3 = 진행중_단계의_피드_생성("피드3", "피드3").writtenBy(조엘);
        feedRepository.save(피드3);

        //when & then
        assertThatThrownBy(() -> adminService.deleteFeed(조엘, 피드1.getId()))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(ErrorType.ADMIN_ONLY.getMessage());
    }

    @DisplayName("어드민 권한이라면 모든 유저를 받아올 수 있다")
    @Test
    void findAllUsers() {
        List<UserResponse> allUsers = adminService.findAllUsers(User.ADMIN_USER);
        assertThat(allUsers).isNotEmpty();
    }

    @DisplayName("어드민 권한이 아니라면 모든 유저를 받아올 수 없다.")
    @Test
    void findAllUsersWithoutAdmin() {
        assertThatThrownBy(() -> adminService.findAllUsers(찰리))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(ErrorType.ADMIN_ONLY.getMessage());
    }

    @DisplayName("어드민 권한으로 어떠한 유저라도 삭제할 수 있다.")
    @Test
    void deleteUser() {
        //given
        Long userId = 조엘.getId();
        adminService.deleteUser(User.ADMIN_USER, userId);

        //when
        Optional<User> user = userRepository.findById(userId);

        //then
        assertThat(user).isNotPresent();
    }

    @DisplayName("어드민 권한이 아니라면 유저 삭제는 불가능하다.")
    @Test
    void deleteUsersWithoutAdmin() {
        assertThatThrownBy(() -> adminService.deleteUser(조엘, 조엘.getId()))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(ErrorType.ADMIN_ONLY.getMessage());
    }

    @Test
    void findAllComments() {
        //given
        Feed 피드1 = 진행중_단계의_피드_생성("피드1", "피드1").writtenBy(조엘);
        feedRepository.saveAndFlush(피드1);
        Feed 피드2 = 진행중_단계의_피드_생성("피드2", "피드2").writtenBy(조엘);
        feedRepository.saveAndFlush(피드2);
        Feed 피드3 = 진행중_단계의_피드_생성("피드3", "피드3").writtenBy(조엘);
        feedRepository.saveAndFlush(피드3);

        final Comment 피드1_댓글1 = new Comment("피드1_댓글1", false).writtenBy(찰리, 피드1);
        commentRepository.saveAndFlush(피드1_댓글1);
        final Comment 피드1_댓글2 = new Comment("피드1_댓글2", false).writtenBy(찰리, 피드1);
        commentRepository.saveAndFlush(피드1_댓글2);
        final Comment 피드1_댓글3 = new Comment("피드1_댓글3", false).writtenBy(찰리, 피드1);
        commentRepository.saveAndFlush(피드1_댓글3);

        final Comment 피드2_댓글1 = new Comment("피드2_댓글1", false).writtenBy(찰리, 피드2);
        commentRepository.saveAndFlush(피드2_댓글1);
        final Comment 피드2_댓글2 = new Comment("피드2_댓글2", false).writtenBy(찰리, 피드2);
        commentRepository.saveAndFlush(피드2_댓글2);

        //when
        List<CommentsByFeedResponse> allCommentsByFeed = adminService.findAllComments(User.ADMIN_USER);

        //then
        assertThat(allCommentsByFeed).hasSize(2);
        assertThat(allCommentsByFeed.get(0).getFeed().getId()).isEqualTo(피드1.getId());
        assertThat(allCommentsByFeed.get(0).getComments()).hasSize(3);
        assertThat(allCommentsByFeed.get(1).getFeed().getId()).isEqualTo(피드2.getId());
        assertThat(allCommentsByFeed.get(1).getComments()).hasSize(2);
    }

    @DisplayName("어드민 권한으로 댓글을 삭제할 수 있다")
    @Test
    void deleteComment() {
        //given
        Feed 피드1 = 진행중_단계의_피드_생성("피드1", "피드1").writtenBy(조엘);
        feedRepository.save(피드1);
        Feed 피드2 = 진행중_단계의_피드_생성("피드2", "피드2").writtenBy(조엘);
        feedRepository.save(피드2);
        Feed 피드3 = 진행중_단계의_피드_생성("피드3", "피드3").writtenBy(조엘);
        feedRepository.save(피드3);

        final Comment 피드1_댓글1 = new Comment("피드1_댓글1", false).writtenBy(찰리, 피드1);
        commentRepository.save(피드1_댓글1);
        final Comment 피드1_댓글2 = new Comment("피드1_댓글2", false).writtenBy(찰리, 피드1);
        commentRepository.save(피드1_댓글2);
        final Comment 피드1_댓글3 = new Comment("피드1_댓글3", false).writtenBy(찰리, 피드1);
        commentRepository.save(피드1_댓글3);

        final Comment 피드2_댓글1 = new Comment("피드2_댓글1", false).writtenBy(찰리, 피드2);
        commentRepository.save(피드2_댓글1);
        final Comment 피드2_댓글2 = new Comment("피드2_댓글2", false).writtenBy(찰리, 피드2);
        commentRepository.save(피드2_댓글2);
        em.flush();
        em.clear();

        //when
        adminService.deleteComment(User.ADMIN_USER, 피드1_댓글1.getId());
        em.flush();
        em.clear();

        //then
        List<CommentsByFeedResponse> allCommentsByFeed = adminService.findAllComments(User.ADMIN_USER);
        assertThat(allCommentsByFeed).hasSize(2);
        assertThat(allCommentsByFeed.get(0).getFeed().getId()).isEqualTo(피드1.getId());
        assertThat(allCommentsByFeed.get(0).getComments()).hasSize(2);
        assertThat(allCommentsByFeed.get(1).getFeed().getId()).isEqualTo(피드2.getId());
        assertThat(allCommentsByFeed.get(1).getComments()).hasSize(2);
    }

    @DisplayName("어드민 권한으로 테크를 전체 조회할 수 있다")
    @Test
    void findAllTechs() {
        //when
        List<TechResponse> allTechs = adminService.findAllTechs(User.ADMIN_USER);

        //then
        assertThat(allTechs).hasSize(3);
    }

    @DisplayName("어드민 권한으로 테크를 삭제할 수 있다")
    @Test
    void deleteTech() {
        //when
        adminService.deleteTech(User.ADMIN_USER, 리액트.getId());

        //then
        assertThat(adminService.findAllTechs(User.ADMIN_USER)).hasSize(2);
    }
}