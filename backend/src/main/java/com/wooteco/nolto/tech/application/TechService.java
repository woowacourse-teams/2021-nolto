package com.wooteco.nolto.tech.application;

import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import com.wooteco.nolto.tech.ui.dto.TechResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
public class TechService {

    private static final String TECH_SEARCH_DELIMITER = ",";

    private final TechRepository techRepository;

    public List<TechResponse> findByTechsContains(String searchWord) {
        List<Tech> findTechs = techRepository.findByNameStartsWithIgnoreCase(searchWord);
        return TechResponse.toList(findTechs);
    }

    public List<TechResponse> findAllByNameInIgnoreCase(String searchWord) {
        List<String> techNames = Arrays.stream(searchWord.split(TECH_SEARCH_DELIMITER))
                .map(String::trim)
                .collect(Collectors.toList());
        List<Tech> findTechs = techRepository.findAllByNameInIgnoreCase(techNames);
        return TechResponse.toList(findTechs);
    }

    public List<TechResponse> findTrendTechs() {
        Pageable pageable = PageRequest.of(0, 4);
        List<Tech> trendTech = techRepository.findTrendTech(pageable);
        return TechResponse.toList(trendTech);
    }
}
