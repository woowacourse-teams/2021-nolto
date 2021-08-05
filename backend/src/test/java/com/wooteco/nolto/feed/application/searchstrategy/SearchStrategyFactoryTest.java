package com.wooteco.nolto.feed.application.searchstrategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class SearchStrategyFactoryTest {

    @DisplayName("검색할 텍스트 query와 기술목록 techs를 넘겨주면, 알맞는 검색전략 팩토리가 반환된다.")
    @Test
    void of() {
        SearchStrategyFactory noQueryNoTechs = SearchStrategyFactory.of("", "");
        assertThat(noQueryNoTechs).isEqualTo(SearchStrategyFactory.NONE);

        SearchStrategyFactory yesQueryNoTechs = SearchStrategyFactory.of("멋진 프로젝트", "");
        assertThat(yesQueryNoTechs).isEqualTo(SearchStrategyFactory.QUERY_ONLY);

        SearchStrategyFactory noQueryYesTechs = SearchStrategyFactory.of("", "Java,Spring");
        assertThat(noQueryYesTechs).isEqualTo(SearchStrategyFactory.TECHS_ONLY);

        SearchStrategyFactory yesQueryYesTechs = SearchStrategyFactory.of("멋진 프로젝트", "Java,Spring");
        assertThat(yesQueryYesTechs).isEqualTo(SearchStrategyFactory.QUERY_AND_TECHS);
    }

    @DisplayName("검색전략 팩토리로 부터 검색 전략을 가져올 수 있다.")
    @Test
    void findStrategy() {
        SearchStrategyFactory noQueryNoTechs = SearchStrategyFactory.of("", "");
        assertThat(noQueryNoTechs.findStrategy()).isInstanceOf(NoneStrategy.class);

        SearchStrategyFactory yesQueryNoTechs = SearchStrategyFactory.of("멋진 프로젝트", "");
        assertThat(yesQueryNoTechs.findStrategy()).isInstanceOf(QueryOnlyStrategy.class);

        SearchStrategyFactory noQueryYesTechs = SearchStrategyFactory.of("", "Java,Spring");
        assertThat(noQueryYesTechs.findStrategy()).isInstanceOf(TechsOnlyStrategy.class);

        SearchStrategyFactory yesQueryYesTechs = SearchStrategyFactory.of("멋진 프로젝트", "Java,Spring");
        assertThat(yesQueryYesTechs.findStrategy()).isInstanceOf(QueryAndTechsStrategy.class);
    }
}