package com.wooteco.nolto.tech.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TechRepositoryTest {

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

    @DisplayName("스택 이름의 첫 단어를 검색한다.")
    @Test
    void findByFirstNameContains() {
        // when
        List<Tech> findTechs = techRepository.findByNameStartsWithIgnoreCase("j");

        // then
        assertThat(findTechs).containsExactly(TECH_JAVA, TECH_JAVASCRIPT);
    }

    @DisplayName("스택 이름의 중간 단어를 검색 안 한다.")
    @Test
    void findByMiddleNameContains() {
        // when
        List<Tech> findTechs = techRepository.findByNameStartsWithIgnoreCase("va");

        // then
        assertThat(findTechs).doesNotContain(TECH_JAVA, TECH_JAVASCRIPT);
    }

    @DisplayName("스택 이름의 마지막 단어를 검색 안 한다.")
    @Test
    void findByLastNameContains() {
        // when
        List<Tech> findTechs = techRepository.findByNameStartsWithIgnoreCase("ing");

        // then
        assertThat(findTechs).doesNotContain(TECH_SPRING);
    }

    @DisplayName("테크 이름 리스트로 그에 대응하는 테크 목록을 받아올 수 있다.")
    @Test
    void findAllByName() {
        // when
        List<String> techNames = Arrays.asList("Java", "Javascript", "Spring");
        List<Tech> techs = techRepository.findAllByNameInIgnoreCase(techNames);

        // then
        assertThat(techs).containsExactly(TECH_JAVA, TECH_JAVASCRIPT, TECH_SPRING);
    }

    @DisplayName("테크 이름 리스트에 대응하는 테크가 없다면 가져오지 않는다.")
    @Test
    void findAllByNameNoMatching() {
        // when
        List<String> techNames = Arrays.asList("real-wow-tech");
        List<Tech> techs = techRepository.findAllByNameInIgnoreCase(techNames);

        // then
        assertThat(techs).hasSize(0);
    }

    @DisplayName("테크 이름 리스트에 대응하는 테크가 있기도 하고 없기도 하다면, 대응되는 테크만 가져온다")
    @Test
    void findAllByNameSeveralMatching() {
        // when
        List<String> techNames = Arrays.asList("real-wow-tech", "Java", "Javascript");
        List<Tech> techs = techRepository.findAllByNameInIgnoreCase(techNames);

        // then
        assertThat(techs).containsExactly(TECH_JAVA, TECH_JAVASCRIPT);
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
}