package com.wooteco.nolto.tech.domain;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.FeedTech;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.feed.domain.repository.FeedTechRepository;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.wooteco.nolto.FeedFixture.진행중_단계의_피드_생성;
import static com.wooteco.nolto.TechFixture.*;
import static com.wooteco.nolto.UserFixture.아마찌_생성;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TechRepositoryTest {

    @Autowired
    private TechRepository techRepository;

    @Autowired
    private FeedTechRepository feedTechRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    private final Tech 자바 = 자바_생성();
    private final Tech 자바스크립트 = 자바스크립트_생성();
    private final Tech 스프링 = 스프링_생성();
    private final Tech 리액트 = 리액트_생성();
    private final Tech 리액트_네이티브 = 리액트_네이티브_생성();

    private final User 아마찌 = 아마찌_생성();

    private final Feed FEED1 = 진행중_단계의_피드_생성().writtenBy(아마찌);
    private final Feed FEED2 = 진행중_단계의_피드_생성().writtenBy(아마찌);
    private final Feed FEED3 = 진행중_단계의_피드_생성().writtenBy(아마찌);
    private final Feed FEED4 = 진행중_단계의_피드_생성().writtenBy(아마찌);
    private final Feed FEED5 = 진행중_단계의_피드_생성().writtenBy(아마찌);

    @BeforeEach
    void setUp() {
        techRepository.saveAll(Arrays.asList(자바, 자바스크립트, 스프링, 리액트, 리액트_네이티브));
        userRepository.save(아마찌);
        feedRepository.saveAll(Arrays.asList(FEED1, FEED2, FEED3, FEED4, FEED5));
    }

    @DisplayName("스택 이름의 첫 단어를 검색한다.")
    @Test
    void findByFirstNameContains() {
        // when
        List<Tech> findTechs = techRepository.findByNameStartsWithIgnoreCase("j");

        // then
        assertThat(findTechs).containsExactly(자바, 자바스크립트);
    }

    @DisplayName("스택 이름의 중간 단어를 검색 안 한다.")
    @Test
    void findByMiddleNameContains() {
        // when
        List<Tech> findTechs = techRepository.findByNameStartsWithIgnoreCase("va");

        // then
        assertThat(findTechs).doesNotContain(자바, 자바스크립트);
    }

    @DisplayName("스택 이름의 마지막 단어를 검색 안 한다.")
    @Test
    void findByLastNameContains() {
        // when
        List<Tech> findTechs = techRepository.findByNameStartsWithIgnoreCase("ing");

        // then
        assertThat(findTechs).doesNotContain(스프링);
    }

    @DisplayName("테크 이름 리스트로 그에 대응하는 테크 목록을 받아올 수 있다.")
    @Test
    void findAllByName() {
        // when
        List<String> techNames = Arrays.asList("Java", "Javascript", "Spring");
        List<Tech> techs = techRepository.findAllByNameInIgnoreCase(techNames);

        // then
        assertThat(techs).containsExactly(자바, 자바스크립트, 스프링);
    }

    @DisplayName("테크 이름 리스트에 대응하는 테크가 없다면 가져오지 않는다.")
    @Test
    void findAllByNameNoMatching() {
        // when
        List<String> techNames = Arrays.asList("real-wow-tech");
        List<Tech> techs = techRepository.findAllByNameInIgnoreCase(techNames);

        // then
        assertThat(techs).isEmpty();
    }

    @DisplayName("테크 이름 리스트에 대응하는 테크가 있기도 하고 없기도 하다면, 대응되는 테크만 가져온다")
    @Test
    void findAllByNameSeveralMatching() {
        // when
        List<String> techNames = Arrays.asList("real-wow-tech", "Java", "Javascript");
        List<Tech> techs = techRepository.findAllByNameInIgnoreCase(techNames);

        // then
        assertThat(techs).containsExactly(자바, 자바스크립트);
    }

    @DisplayName("테크 이름이 정확히 일치하는 테크들만 가져온다.(단, 대소문자는 구분하지 않는다.)")
    @Test
    void findAllByNameInIgnoreCase() {
        // given
        String techWord1 = "Java";
        String techWord2 = "jaVA";
        String techWord3 = "Java,JavaScr,UnidentifiedTech";

        String[] splitTechWord1 = techWord1.split(",");
        String[] splitTechWord2 = techWord2.split(",");
        String[] splitTechWord3 = techWord3.split(",");

        // when
        List<Tech> findTechs1 = techRepository.findAllByNameInIgnoreCase(Arrays.asList(splitTechWord1));
        List<Tech> findTechs2 = techRepository.findAllByNameInIgnoreCase(Arrays.asList(splitTechWord2));
        List<Tech> findTechs3 = techRepository.findAllByNameInIgnoreCase(Arrays.asList(splitTechWord3));

        // then
        assertThat(findTechs1).extracting("name").contains("Java");
        assertThat(findTechs2).extracting("name").contains("Java");
        assertThat(findTechs3).extracting("name").contains("Java");
    }

    @DisplayName("테크 이름이 정확히 일치하는 테크들만 가져온다.(단, 대소문자는 구분하지 않는다.)")
    @Test
    void findAllByNameInIgnoreCaseWithNonExistTech() {
        // given
        String techWord = "UnidentifiedTech";

        // when
        List<Tech> findTechs1 = techRepository.findAllByNameInIgnoreCase(Collections.singletonList(techWord));

        // then
        assertThat(findTechs1).isEmpty();
    }

    @DisplayName("전체 피드 중에서 현재 어떤 테크가 가장 많이 쓰이고 있는지 조회할 수 있다.")
    @Test
    void findTrendTech() {
        //given
        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED1, 자바), new FeedTech(FEED1, 자바스크립트),
                new FeedTech(FEED1, 스프링), new FeedTech(FEED1, 리액트), new FeedTech(FEED1, 리액트_네이티브)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED2, 자바), new FeedTech(FEED2, 자바스크립트),
                new FeedTech(FEED2, 스프링), new FeedTech(FEED2, 리액트)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED3, 자바), new FeedTech(FEED3, 자바스크립트),
                new FeedTech(FEED3, 스프링)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED4, 자바), new FeedTech(FEED4, 자바스크립트)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED5, 자바)));

        //when
        Pageable topTwoTechs = PageRequest.of(0, 2);
        List<Tech> topTwoTrendTechs = techRepository.findTrendTech(topTwoTechs);

        Pageable topThreeTechs = PageRequest.of(0, 3);
        List<Tech> TopThreeTrendTechs = techRepository.findTrendTech(topThreeTechs);

        Pageable topFourTechs = PageRequest.of(0, 4);
        List<Tech> TopFourTrendTechs = techRepository.findTrendTech(topFourTechs);

        //then
        assertThat(topTwoTrendTechs).containsExactly(자바, 자바스크립트);
        assertThat(TopThreeTrendTechs).containsExactly(자바, 자바스크립트, 스프링);
        assertThat(TopFourTrendTechs).containsExactly(자바, 자바스크립트, 스프링, 리액트);
    }

    @DisplayName("트렌트 테크 조회 시, 동점의 경우 tech_id의 오름차순으로 결과를 정렬해 조회해 올 수 있다.")
    @Test
    void findTrendTechWhenEven() {
        //given
        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED1, 자바), new FeedTech(FEED1, 자바스크립트),
                new FeedTech(FEED1, 스프링), new FeedTech(FEED1, 리액트), new FeedTech(FEED1, 리액트_네이티브)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED2, 자바), new FeedTech(FEED2, 자바스크립트),
                new FeedTech(FEED2, 스프링), new FeedTech(FEED2, 리액트), new FeedTech(FEED2, 리액트_네이티브)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED3, 자바), new FeedTech(FEED3, 자바스크립트),
                new FeedTech(FEED3, 스프링), new FeedTech(FEED3, 리액트), new FeedTech(FEED3, 리액트_네이티브)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED4, 자바), new FeedTech(FEED4, 자바스크립트),
                new FeedTech(FEED4, 스프링), new FeedTech(FEED4, 리액트), new FeedTech(FEED4, 리액트_네이티브)));

        //when
        Pageable topFourTechs = PageRequest.of(0, 4, Sort.by("tech").ascending());
        List<Tech> TopFourTrendTechs = techRepository.findTrendTech(topFourTechs);

        //then
        assertThat(TopFourTrendTechs).containsExactly(자바, 자바스크립트, 스프링, 리액트);
    }

    @DisplayName("트렌트 테크 조회 시, 동점의 경우 tech_id의 오름차순으로 결과를 조회해 올 수 있다2.")
    @Test
    void findTrendTechWhenEven2() {
        //given (JAVA - 3개 | JAVASCRIPT - 4개 | SPRING - 2개 | JPA - 3개)
        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED1, 자바), new FeedTech(FEED1, 자바스크립트),
                new FeedTech(FEED1, 스프링), new FeedTech(FEED1, 리액트)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED2, 자바), new FeedTech(FEED2, 자바스크립트),
                new FeedTech(FEED2, 스프링), new FeedTech(FEED2, 리액트)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED3, 자바), new FeedTech(FEED3, 자바스크립트),
                new FeedTech(FEED3, 리액트)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED4, 자바스크립트)));

        //when
        Pageable topFourTechs = PageRequest.of(0, 4, Sort.by("tech").ascending());
        List<Tech> TopFourTrendTechs = techRepository.findTrendTech(topFourTechs);

        //then
        assertThat(TopFourTrendTechs).containsExactly(자바스크립트, 자바, 리액트, 스프링);
    }
}
