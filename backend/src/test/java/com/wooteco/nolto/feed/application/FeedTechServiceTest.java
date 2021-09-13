package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.Arrays;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class FeedTechServiceTest {

    @Autowired
    private FeedTechService feedTechService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private TechRepository techRepository;

    private User user;

    private Feed feed1;
    private Feed feed2;
    private Feed feed3;

    private Tech tech1 = new Tech("Java");
    private Tech tech2 = new Tech("Spring Boot");
    private Tech tech3 = new Tech("Spring");

    @BeforeEach
    void setUp() {
        user = new User("socialId", SocialType.GOOGLE, "nickName", "imageUrl");
        userRepository.save(user);

        feed1 = Feed.builder()
                .title("T1")
                .content("C1")
                .step(Step.PROGRESS)
                .isSos(true)
                .storageUrl("www.github.com/newWisdom")
                .thumbnailUrl("https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png")
                .build();
        feed2 = Feed.builder()
                .title("T2")
                .content("C2")
                .step(Step.PROGRESS)
                .isSos(true)
                .storageUrl("www.github.com/newWisdom")
                .thumbnailUrl("https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png")
                .build();
        feed3 = Feed.builder()
                .title("T3")
                .content("C1")
                .step(Step.PROGRESS)
                .isSos(true)
                .storageUrl("www.github.com/newWisdom")
                .thumbnailUrl("https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png")
                .build();

        feedRepository.saveAll(Arrays.asList(feed1.writtenBy(user), feed2.writtenBy(user), feed3.writtenBy(user)));

        techRepository.saveAll(Arrays.asList(tech1, tech2, tech3));
    }
}
