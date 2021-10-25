package com.wooteco.nolto.infrastructure.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/infra")
@RequiredArgsConstructor
public class InfraController {

    private final Environment env;

    @GetMapping("/health")
    public String health() {
        return "UP";
    }
}
