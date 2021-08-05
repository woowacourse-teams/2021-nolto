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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

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

        feed1 = new Feed("T1", "C1", Step.PROGRESS, true, "", "", "");
        feed2 = new Feed("T2", "C2", Step.PROGRESS, true, "", "", "");
        feed3 = new Feed("T3", "C3", Step.PROGRESS, true, "", "", "");
        feedRepository.saveAll(Arrays.asList(feed1.writtenBy(user), feed2.writtenBy(user), feed3.writtenBy(user)));

        techRepository.saveAll(Arrays.asList(tech1, tech2, tech3));
    }

    @DisplayName("테크 이름 목록을 가지고, 해당 테크를 사용한 피드를 조회할 수 있다.")
    @Test
    void findFeedUsingTech() {
        // given
        feedTechService.save(feed1, Arrays.asList(tech1.getId(), tech2.getId()));
        feedTechService.save(feed2, Arrays.asList(tech2.getId(), tech3.getId()));
        feedTechService.save(feed3, Arrays.asList(tech3.getId()));

        List<String> techNames123 = Arrays.asList(tech1.getName(), tech2.getName(), tech3.getName());
        List<String> techNames12 = Arrays.asList(tech1.getName(), tech2.getName());
        List<String> techNames2 = Arrays.asList(tech2.getName());
        List<String> techNames3 = Arrays.asList(tech3.getName());

        // when
        Set<Feed> usingTech123 = feedTechService.findFeedUsingTech(techNames123);
        Set<Feed> usingTech12 = feedTechService.findFeedUsingTech(techNames12);
        Set<Feed> usingTech2 = feedTechService.findFeedUsingTech(techNames2);
        Set<Feed> usingTech3 = feedTechService.findFeedUsingTech(techNames3);

        // then
        assertThat(usingTech123).hasSize(0);
        assertThat(usingTech12).contains(feed1);
        assertThat(usingTech2).contains(feed1, feed2);
        assertThat(usingTech3).contains(feed2, feed3);
    }
}
