package com.wooteco.nolto.tech.ui.dto;

import com.wooteco.nolto.tech.domain.Tech;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TechResponse {

    private final Long id;
    private final String text;

    public TechResponse(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    public static List<TechResponse> toList(List<Tech> techs) {
        return techs.stream()
                .map(tech -> new TechResponse(tech.getId(), tech.getName()))
                .collect(Collectors.toList());
    }
}
