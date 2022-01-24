package com.wooteco.nolto.image.infrastructure;

import com.wooteco.nolto.image.domain.ProcessedImage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {LocalImageRepository.class})
@ActiveProfiles("local")
@Disabled
class LocalImageRepositoryTest {

    private static final String fileName = "test-mickey.png";

    @Value("${image.path}")
    private String path;

    @Autowired
    private LocalImageRepository imageRepository;

    @DisplayName("ProcessedImage를 서버에 저장할 수 있다")
    @Test
    @Order(1)
    void save() {
        // given
        URL resource = getClass().getClassLoader().getResource("static/mickey.png");
        File file = new File(resource.getFile());
        ProcessedImage processedImage = new ProcessedImage(file, fileName);

        // when
        String savedUrl = imageRepository.save(processedImage);

        // then
        assertThat(savedUrl).isEqualTo("http://localhost:8080/images/" + fileName);
    }

    @DisplayName("이미지를 삭제할 수 있다.")
    @Test
    @Order(2)
    void deleteFile() {
        // given
        imageRepository.deleteFile(fileName);

        // then
        Path filePath = Paths.get(path + fileName);
        assertThat(Files.notExists(filePath)).isTrue();
    }
}