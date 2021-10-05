package com.wooteco.nolto.tech.application;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.FeedTech;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.feed.domain.repository.FeedTechRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static com.wooteco.nolto.FeedFixture.전시중_단계의_피드_생성;
import static com.wooteco.nolto.FeedFixture.진행중_단계의_피드_생성;
import static com.wooteco.nolto.TechFixture.*;
import static com.wooteco.nolto.UserFixture.조엘_생성;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class TechServiceTest {

    @Autowired
    private TechService techService;

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

    private final User 조엘 = 조엘_생성();

    private final Feed FEED1 = 진행중_단계의_피드_생성().writtenBy(조엘);
    private final Feed FEED2 = 전시중_단계의_피드_생성().writtenBy(조엘);
    private final Feed FEED3 = 진행중_단계의_피드_생성().writtenBy(조엘);
    private final Feed FEED4 = 진행중_단계의_피드_생성().writtenBy(조엘);
    private final Feed FEED5 = 전시중_단계의_피드_생성().writtenBy(조엘);

    @BeforeEach
    void setUp() {
        techRepository.saveAll(Arrays.asList(자바, 자바스크립트, 스프링, 리액트, 리액트_네이티브));
        userRepository.save(조엘);
        feedRepository.saveAll(Arrays.asList(FEED1, FEED2, FEED3, FEED4, FEED5));
    }

    @DisplayName("기술이름으로 검색했을 때 해당 문자열이 포함된 기술 스택을 전부 조회할 수 있다.")
    @Test
    void findByTechsContains() {
        // given
        String searchWord = "Java";

        // when
        List<TechResponse> findTechs = techService.findByTechsContains(searchWord);

        // then
        assertThat(findTechs).hasSize(2);
        assertThat(findTechs).extracting("text").contains(자바.getName(), 자바스크립트.getName());
    }

    @DisplayName("기술 이름중 일부 만으로 해당 문자열을 포함된 모든 기술 스택을 조회할 수 있다.")
    @Test
    void findByTechsContainsWithNonCompleteWord() {
        // given
        String searchWord = "Ja";

        // when
        List<TechResponse> findTechs = techService.findByTechsContains(searchWord);

        // then
        assertThat(findTechs).hasSize(2);
        assertThat(findTechs).extracting("text").contains(자바.getName(), 자바스크립트.getName());
    }

    @DisplayName("검색하는 기술 이름에 정확하게 일치하는 기술 스택만 조회할 수 있다. 단, 대소문자는 구분하지 않는다.")
    @Test
    void findAllByNameInIgnoreCase() {
        // given
        String searchWord1 = "Java";
        String searchWord2 = "sPrinG";

        // when
        List<TechResponse> findTechs1 = techService.findAllByNameInIgnoreCase(searchWord1);
        List<TechResponse> findTechs2 = techService.findAllByNameInIgnoreCase(searchWord2);

        // then
        assertThat(findTechs1).hasSize(1);
        assertThat(findTechs1).extracting("text").contains("Java");
        assertThat(findTechs2).hasSize(1);
        assertThat(findTechs2).extracting("text").contains("Spring");
    }

    @DisplayName("기술 이름을 ',' 로 구분하여 각각 정확하게 일치하는 여러개의 기술 스택을 검색행올 수 있다. 공백이 있을 시 자동으로 제거해준다.")
    @Test
    void findAllByNameInIgnoreCaseMultipleNames() {
        // given
        String searchWord1 = "Java,Spring";
        String searchWord2 = "Java,    Spring";

        // when
        List<TechResponse> findTechs1 = techService.findAllByNameInIgnoreCase(searchWord1);
        List<TechResponse> findTechs2 = techService.findAllByNameInIgnoreCase(searchWord2);

        // then
        assertThat(findTechs1).hasSize(2);
        assertThat(findTechs1).extracting("text").contains("Java", "Spring");
        assertThat(findTechs2).hasSize(2);
        assertThat(findTechs2).extracting("text").contains("Java", "Spring");
    }

    @DisplayName("검색하는 기술 이름에 정확하게 일치하지 않으면 조회할 수 없다.")
    @Test
    void findAllByNameInIgnoreCaseWithNotEqualString() {
        // given
        String searchWord1 = "Ja";
        String searchWord2 = "Spr";

        // when
        List<TechResponse> findTechs1 = techService.findAllByNameInIgnoreCase(searchWord1);
        List<TechResponse> findTechs2 = techService.findAllByNameInIgnoreCase(searchWord2);

        // then
        assertThat(findTechs1).isEmpty();
        assertThat(findTechs2).isEmpty();
    }

    @DisplayName("전체 피드에서 가장 사용이 많이 된 트렌드 테크 스택 4가지를 조회할 수 있다.")
    @Test
    void findTrendTechs() {
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
        List<TechResponse> techResponses = techService.findTrendTechs();

        //then
        assertThat(techResponses).extracting("text")
                .containsExactly(자바.getName(), 자바스크립트.getName(), 스프링.getName(), 리액트.getName());
    }

    @DisplayName("전체 피드에서 사용된 테크가 4개 미만이라면, 그것만 반환한다.")
    @Test
    void findTrendTechsUnderFourTechs() {
        //given
        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED3, 자바), new FeedTech(FEED3, 자바스크립트),
                new FeedTech(FEED3, 스프링)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED4, 자바), new FeedTech(FEED4, 자바스크립트)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED5, 자바)));

        //when
        List<TechResponse> techResponses = techService.findTrendTechs();

        //then
        assertThat(techResponses).extracting("text")
                .containsExactly(자바.getName(), 자바스크립트.getName(), 스프링.getName());
    }

    @DisplayName("기술을 사용한 피드가 전혀 없으면 빈 리스트를 반환한다.")
    @Test
    void findTrendTechsWhenFeedDontUseAnyTech() {
        //when
        List<TechResponse> techResponses = techService.findTrendTechs();

        //then
        assertThat(techResponses).isEmpty();
    }
}
