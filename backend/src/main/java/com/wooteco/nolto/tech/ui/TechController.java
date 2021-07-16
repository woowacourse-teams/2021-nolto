package com.wooteco.nolto.tech.ui;

import com.wooteco.nolto.tech.application.TechService;
import com.wooteco.nolto.tech.ui.dto.TechResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tags/techs")
@AllArgsConstructor
public class TechController {

    private TechService techService;

    @GetMapping
    public ResponseEntity<List<TechResponse>> findByTechsContains(@RequestParam("auto_complete") String searchWord) {
        List<TechResponse> findTechs = techService.findByTechsContains(searchWord);
        return ResponseEntity.ok(findTechs);
    }
}
