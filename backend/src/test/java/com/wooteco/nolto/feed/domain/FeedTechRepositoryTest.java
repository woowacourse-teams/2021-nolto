package com.wooteco.nolto.feed.domain;

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

import static com.wooteco.nolto.FeedFixture.진행중_단계의_피드_생성;
import static com.wooteco.nolto.TechFixture.*;
import static com.wooteco.nolto.UserFixture.조엘_생성;
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

    private User 조엘;

    private Feed feed1;
    private Feed feed2;

    private Tech 자바;
    private Tech 스프링;
    private Tech 리액트;

    @BeforeEach
    void setUp() {
        조엘 = 조엘_생성();
        feed1 = 진행중_단계의_피드_생성("조엘 프로젝트", "조엘의 환상적인 토이 프로젝트로 초대합니다 룰루랄라");
        feed2 = 진행중_단계의_피드_생성("놀토 프로젝트", "놀토는 정말 세계에서 제일가는 팀입니다. 우테코 최고 아웃풋이죠");

        자바 = 자바_생성();
        스프링 = 스프링_생성();
        리액트 = 리액트_생성();
    }

    @DisplayName("특정한 Tech를 사용한 FeedTech의 목록을 조회해올 수 있다.")
    @Test
    void findFeedTechByTech() {
        // given
        userRepository.save(조엘);
        feedRepository.save(feed1.writtenBy(조엘));
        feedRepository.save(feed2.writtenBy(조엘));
        techRepository.save(자바);
        techRepository.save(스프링);
        techRepository.save(리액트);

        final FeedTech feed1tech1 = feedTechRepository.save(new FeedTech(feed1, 자바));
        final FeedTech feed1tech3 = feedTechRepository.save(new FeedTech(feed1, 리액트));

        final FeedTech feed2tech2 = feedTechRepository.save(new FeedTech(feed2, 스프링));
        final FeedTech feed2tech3 = feedTechRepository.save(new FeedTech(feed2, 리액트));

        // when
        List<FeedTech> containsTech1 = feedTechRepository.findByTech(자바);
        List<FeedTech> containsTech2 = feedTechRepository.findByTech(스프링);
        List<FeedTech> containsTech3 = feedTechRepository.findByTech(리액트);

        // then
        assertThat(containsTech1).containsExactly(feed1tech1);
        assertThat(containsTech2).containsExactly(feed2tech2);
        assertThat(containsTech3).containsExactly(feed1tech3, feed2tech3);
    }
}