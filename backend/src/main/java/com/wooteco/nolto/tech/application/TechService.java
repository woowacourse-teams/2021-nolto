package com.wooteco.nolto.tech.application;

import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import com.wooteco.nolto.tech.ui.dto.TechResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TechService {

    private final TechRepository techRepository;

    public List<TechResponse> findByTechsContains(String searchWord) {
        List<Tech> findTechs = techRepository.findByNameContainsIgnoreCase(searchWord);
        return TechResponse.toList(findTechs);
    }
}
