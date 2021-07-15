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
        List<Tech> findTechs = techRepository.findByNameContainsIgnoreCase("j");

        // then
        assertThat(findTechs).containsExactly(TECH_JAVA, TECH_JAVASCRIPT);
    }

    @DisplayName("스택 이름의 중간 단어를 검색한다.")
    @Test
    void findByMiddleNameContains() {
        // when
        List<Tech> findTechs = techRepository.findByNameContainsIgnoreCase("va");

        // then
        assertThat(findTechs).containsExactly(TECH_JAVA, TECH_JAVASCRIPT);
    }

    @DisplayName("스택 이름의 마지막 단어를 검색한다.")
    @Test
    void findByLastNameContains() {
        // when
        List<Tech> findTechs = techRepository.findByNameContainsIgnoreCase("ing");

        // then
        assertThat(findTechs).containsExactly(TECH_SPRING);
    }
}