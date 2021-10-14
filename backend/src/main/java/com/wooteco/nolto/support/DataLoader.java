package com.wooteco.nolto.support;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Like;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.domain.repository.CommentRepository;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.feed.domain.repository.LikeRepository;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Profile("local")
@Transactional
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final FeedRepository feedRepository;
    private final TechRepository techRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private static final String URL = "https://github.com/woowacourse-teams/2021-nolto";
    private static final String DEFAULT_IMAGE = "https://dksykemwl00pf.cloudfront.net/amazzi.jpeg";

    @Override
    public void run(ApplicationArguments args) {
        User mickey = new User("1", SocialType.GITHUB, "미키", DEFAULT_IMAGE);
        List<User> users = Arrays.asList(
                mickey,
                new User("2", SocialType.GITHUB, "아마찌", DEFAULT_IMAGE),
                new User("3", SocialType.GITHUB, "지그", DEFAULT_IMAGE),
                new User("4", SocialType.GITHUB, "포모", DEFAULT_IMAGE),
                new User("5", SocialType.GITHUB, "조엘", DEFAULT_IMAGE),
                new User("6", SocialType.GITHUB, "찰리", DEFAULT_IMAGE)
        );
        userRepository.saveAll(users);

        Tech tech1 = new Tech("Apollo Client");
        Tech tech2 = new Tech("WebGL");
        Tech saveTech1 = techRepository.save(tech1);
        Tech saveTech2 = techRepository.save(tech2);

        Feed feed1 = Feed.builder()
                .title("title1")
                .content("content1")
                .step(Step.PROGRESS)
                .isSos(true)
                .storageUrl(URL)
                .thumbnailUrl("https://dksykemwl00pf.cloudfront.net/KakaoTalk_Photo_2021-07-19-14-25-01.png")
                .build().writtenBy(mickey);

        Feed feed2 = Feed.builder()
                .title("title2")
                .content("content2")
                .step(Step.COMPLETE)
                .isSos(false)
                .storageUrl(URL)
                .deployedUrl(URL)
                .thumbnailUrl("https://dksykemwl00pf.cloudfront.net/KakaoTalk_Photo_2021-07-19-14-25-01.png")
                .build().writtenBy(mickey);

        feed1.changeTechs(Collections.singletonList(saveTech1));
        feed2.changeTechs(Collections.singletonList(saveTech1));
        feed2.changeTechs(Collections.singletonList(saveTech2));

        Feed saveFeed1 = feedRepository.save(feed1);
        feedRepository.save(feed2);

        Comment comment1 = new Comment("첫 댓글", false).writtenBy(mickey, feed1);
        Comment comment2 = new Comment("2등 댓글", false).writtenBy(mickey, feed1);
        Comment comment3 = new Comment("첫 댓글의 대댓글111", false).writtenBy(mickey, feed1);
        Comment comment4 = new Comment("첫 댓글의 대댓글222", false).writtenBy(mickey, feed1);
        Comment comment5 = new Comment("첫 댓글의 대댓글333", false).writtenBy(mickey, feed1);
        Comment comment6 = new Comment("2등 댓글의 대댓글111", false).writtenBy(mickey, feed1);

        commentRepository.save(comment1);

        commentRepository.save(comment2);

        comment1.addReply(comment3);
        commentRepository.save(comment3);

        comment1.addReply(comment4);
        commentRepository.save(comment4);

        comment1.addReply(comment5);
        commentRepository.save(comment5);

        comment2.addReply(comment6);
        commentRepository.save(comment6);

        likeRepository.save(new Like(mickey, saveFeed1));
    }
}
