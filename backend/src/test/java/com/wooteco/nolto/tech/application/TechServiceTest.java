package com.wooteco.nolto.tech.application;

import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import com.wooteco.nolto.tech.ui.dto.TechResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TechServiceTest {

    @Autowired
    private TechService techService;

    @Autowired
    private TechRepository techRepository;

    private List<Tech> techs;
    private Tech TECH_JAVA = new Tech("Java");
    private Tech TECH_JAVASCRIPT = new Tech("Javascript");
    private Tech TECH_SPRING = new Tech("Spring");

    @BeforeEach
    void setUp() {
        techs = Arrays.asList(
                TECH_JAVA,
                TECH_JAVASCRIPT,
                TECH_SPRING
        );
        techRepository.saveAll(techs);
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
        assertThat(findTechs).extracting("text").contains("Java", "Javascript");
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
        assertThat(findTechs).extracting("text").contains("Java", "Javascript");
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
}