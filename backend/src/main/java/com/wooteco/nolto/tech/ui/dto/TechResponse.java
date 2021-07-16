package com.wooteco.nolto.tech.ui.dto;

import com.wooteco.nolto.tech.domain.Tech;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class TechResponse {
    private final Long id;
    private final String text;

    public static List<TechResponse> toList(List<Tech> techs) {
        return techs.stream()
                .map(tech -> new TechResponse(tech.getId(), tech.getName()))
                .collect(Collectors.toList());
    }
}
