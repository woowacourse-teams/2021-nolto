package com.wooteco.nolto;

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
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

@Profile("local")
@Component
@AllArgsConstructor
@Transactional
public class DataLoader implements ApplicationRunner {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final TechRepository techRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final EntityManager entityManager;

    @Override
    public void run(ApplicationArguments args) {
        User mickey = new User( "1", SocialType.GITHUB, "미키", "https://dksykemwl00pf.cloudfront.net/amazzi.jpeg");
        List<User> users = Arrays.asList(
                mickey,
                new User("2", SocialType.GITHUB, "아마찌", "https://dksykemwl00pf.cloudfront.net/amazzi.jpeg"),
                new User("3", SocialType.GITHUB, "지그", "https://dksykemwl00pf.cloudfront.net/amazzi.jpeg"),
                new User("4", SocialType.GITHUB, "포모", "https://dksykemwl00pf.cloudfront.net/amazzi.jpeg"),
                new User( "5", SocialType.GITHUB, "조엘", "https://dksykemwl00pf.cloudfront.net/amazzi.jpeg"),
                new User("6", SocialType.GITHUB, "찰리", "https://dksykemwl00pf.cloudfront.net/amazzi.jpeg")
        );
        userRepository.saveAll(users);

        Tech tech1 = new Tech("Apollo Client");
        Tech tech2 = new Tech("WebGL");
        Tech saveTech1 = techRepository.save(tech1);
        Tech saveTech2 = techRepository.save(tech2);

        Feed feed1 = new Feed("title1", "content1", Step.PROGRESS, true, "https://github.com/woowacourse-teams/2021-nolto", "", "https://dksykemwl00pf.cloudfront.net/KakaoTalk_Photo_2021-07-19-14-25-01.png").writtenBy(mickey);
        Feed feed2 = new Feed("title2", "content2", Step.COMPLETE, false, "https://github.com/woowacourse-teams/2021-nolto", "http://woowa.jofilm.com", "https://dksykemwl00pf.cloudfront.net/KakaoTalk_Photo_2021-07-19-14-25-01.png").writtenBy(mickey);
        feed1.changeTechs(Arrays.asList(saveTech1));
        feed2.changeTechs(Arrays.asList(saveTech1));
        feed2.changeTechs(Arrays.asList(saveTech2));

        Feed saveFeed1 = feedRepository.save(feed1);
        Feed saveFeed2 = feedRepository.save(feed2);

        Comment comment1 = new Comment("첫 댓글", false).writtenBy(mickey);
        Comment comment2 = new Comment("2등 댓글", false).writtenBy(mickey);
        Comment comment3 = new Comment("첫 댓글의 대댓글111", false).writtenBy(mickey);
        Comment comment4 = new Comment("첫 댓글의 대댓글222", false).writtenBy(mickey);
        Comment comment5 = new Comment("첫 댓글의 대댓글333", false).writtenBy(mickey);
        Comment comment6 = new Comment("2등 댓글의 대댓글111", false).writtenBy(mickey);

        comment1.setFeed(feed1);
        commentRepository.save(comment1);

        comment2.setFeed(feed1);
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
