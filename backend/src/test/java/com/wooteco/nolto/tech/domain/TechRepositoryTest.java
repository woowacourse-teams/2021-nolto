package com.wooteco.nolto.tech.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
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
        List<Tech> techs = techRepository.findAllByNameIn(techNames);

        // then
        assertThat(techs).containsExactly(TECH_JAVA, TECH_JAVASCRIPT, TECH_SPRING);
    }

    @DisplayName("테크 이름 리스트에 대응하는 테크가 없다면 가져오지 않는다.")
    @Test
    void findAllByNameNoMatching() {
        // when
        List<String> techNames = Arrays.asList("real-wow-tech");
        List<Tech> techs = techRepository.findAllByNameIn(techNames);

        // then
        assertThat(techs).hasSize(0);
    }
    
    @DisplayName("테크 이름 리스트에 대응하는 테크가 있기도 하고 없기도 하다면, 대응되는 테크만 가져온다")
    @Test
    void findAllByNameSeveralMatching() {
        // when
        List<String> techNames = Arrays.asList("real-wow-tech", "Java", "Javascript");
        List<Tech> techs = techRepository.findAllByNameIn(techNames);

        // then
        assertThat(techs).containsExactly(TECH_JAVA, TECH_JAVASCRIPT);
    }
}