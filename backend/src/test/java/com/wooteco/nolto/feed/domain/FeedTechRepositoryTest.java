package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.feed.domain.repository.FeedTechRepository;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FeedTechRepositoryTest {
    @Autowired
    private FeedTechRepository feedTechRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TechRepository techRepository;

    private User user;

    private Feed feed1;
    private Feed feed2;

    private Tech tech1;
    private Tech tech2;
    private Tech tech3;

    @BeforeEach
    void setUp() {
        user = new User("123456L", SocialType.GOOGLE, "아마찌", "imageUrl");
        feed1 = new Feed("조엘 프로젝트", "조엘의 환상적인 토이 프로젝트로 초대합니다 룰루랄라",
                Step.PROGRESS, true, "storageUrl", "", "http://thumbnailUrl.ppnngg");
        feed2 = new Feed("놀토 프로젝트", "놀토는 정말 세계에서 제일가는 팀입니다. 우테코 최고 아웃풋이죠",
                Step.PROGRESS, false, "", "deployUrl", "http://thumbnailUrl.pnggg");
        tech1 = new Tech("Spring");
        tech2 = new Tech("Django");
        tech3 = new Tech("MySql");
    }

    @DisplayName("특정한 Tech를 사용한 FeedTech의 목록을 조회해올 수 있다.")
    @Test
    void findFeedTechByTech() {
        // given
        userRepository.save(user);
        feedRepository.save(feed1.writtenBy(user));
        feedRepository.save(feed2.writtenBy(user));
        techRepository.save(tech1);
        techRepository.save(tech2);
        techRepository.save(tech3);

        final FeedTech feed1tech1 = feedTechRepository.save(new FeedTech(feed1, tech1));
        final FeedTech feed1tech3 = feedTechRepository.save(new FeedTech(feed1, tech3));

        final FeedTech feed2tech2 = feedTechRepository.save(new FeedTech(feed2, tech2));
        final FeedTech feed2tech3 = feedTechRepository.save(new FeedTech(feed2, tech3));

        // when
        List<FeedTech> containsTech1 = feedTechRepository.findByTech(tech1);
        List<FeedTech> containsTech2 = feedTechRepository.findByTech(tech2);
        List<FeedTech> containsTech3 = feedTechRepository.findByTech(tech3);

        // then
        assertThat(containsTech1).containsExactly(feed1tech1);
        assertThat(containsTech2).containsExactly(feed2tech2);
        assertThat(containsTech3).containsExactly(feed1tech3, feed2tech3);
    }
}