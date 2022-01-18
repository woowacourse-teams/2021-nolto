package com.wooteco.nolto.image.infrastructure;

import com.wooteco.nolto.image.domain.ProcessedImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.net.URL;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {LocalImageRepository.class})
@ActiveProfiles("local")
class LocalImageRepositoryTest {

    @Autowired
    private LocalImageRepository imageRepository;

    @DisplayName("ProcessedImage를 서버에 저장할 수 있다")
    @Test
    void save() {
        // given
        URL resource = getClass().getClassLoader().getResource("static/mickey.png");
        File file = new File(resource.getFile());
        String name = "test-name.png";
        ProcessedImage processedImage = new ProcessedImage(file, name);

        // when
        String savedUrl = imageRepository.save(processedImage);

        // then
        assertThat(savedUrl).isEqualTo("http://localhost:8080/images/test-name.png");
    }
}