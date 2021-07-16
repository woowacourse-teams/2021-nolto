package com.wooteco.nolto.tech.application;

import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import com.wooteco.nolto.tech.ui.dto.TechResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
@AllArgsConstructor
public class TechService {

    private final TechRepository techRepository;

    public List<TechResponse> findByTechsContains(String searchWord) {
        List<Tech> findTechs = techRepository.findByNameStartsWithIgnoreCase(searchWord);
        return TechResponse.toList(findTechs);
    }
}
