package com.wooteco.nolto.image.application;

import com.amazonaws.services.s3.AmazonS3;
import com.wooteco.nolto.config.LocalStackS3Config;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LocalStackS3Config.class)
class ImageServiceTest {

    public static final String FILE_PATH = "/src/test/resources/static/";
    public static final String 업로드할_이미지_이름 = "pretty_cat.png";
    public static final String 업데이트할_이미지_이름 = "amazzi.jpeg";

    @Value("${application.bucket.name}")
    private String bucketName;

    @Value("${application.cloudfront.url}")
    private String cloudfrontUrl;

    @Value("${application.default-image}")
    private String 기본_이미지_이름;

    @Autowired
    AmazonS3 amazonS3;

    @Autowired
    ImageService imageService;

    @BeforeEach
    void init() {
        amazonS3.createBucket(bucketName);
        amazonS3.putObject(bucketName, 기본_이미지_이름, new File(new File("").getAbsolutePath() + FILE_PATH + 기본_이미지_이름));
    }

    @DisplayName("이미지를 S3에 업로드한다.")
    @Test
    void upload() throws IOException {
        File 업로드할_이미지_파일 = new File(new File("").getAbsolutePath() + FILE_PATH + 업로드할_이미지_이름);
        MultipartFile 업로드할_멀티파트_파일 = generateMultiPartFile(업로드할_이미지_파일);

        String 업로드된_이미지_주소 = imageService.upload(업로드할_멀티파트_파일, ImageKind.FEED);
        String 업로드된_이미지_이름 = 업로드된_이미지_주소.replace(cloudfrontUrl, "");

        assertAll(
                () -> assertThat(업로드된_이미지_주소).isNotNull(),
                () -> assertThat(amazonS3.doesObjectExist(bucketName, 업로드된_이미지_이름)).isTrue()
        );
    }

    @DisplayName("이미지가 없을 시 기본 이미지 URL을 반환한다.")
    @Test
    void uploadDefaultImage() throws IOException {
        String 업로드된_이미지_주소 = imageService.upload(null, ImageKind.FEED);
        String 업로드된_이미지_이름 = 업로드된_이미지_주소.replace(cloudfrontUrl, "");

        assertAll(
                () -> assertThat(업로드된_이미지_주소).isEqualTo(cloudfrontUrl + 기본_이미지_이름),
                () -> assertThat(업로드된_이미지_이름).isEqualTo(기본_이미지_이름)
        );
    }

    @DisplayName("이미지 업데이트 경우 업데이트 전 이미지는 삭제한다.")
    @Test
    void update() throws IOException {
        // given
        File 미리_업로드할_이미지_파일 = new File(new File("").getAbsolutePath() + FILE_PATH + 업로드할_이미지_이름);
        MultipartFile 미리_업로드할_멀티파트_파일 = generateMultiPartFile(미리_업로드할_이미지_파일);
        String 미리_업로드한_이미지_주소 = imageService.upload(미리_업로드할_멀티파트_파일, ImageKind.FEED);
        String 미리_업로드한_이미지_이름 = 미리_업로드한_이미지_주소.replace(cloudfrontUrl, "");

        File 업데이트_테스트용_파일 = new File(new File("").getAbsolutePath() + FILE_PATH + 업데이트할_이미지_이름);
        MultipartFile 업데이트_테스트용_멀티파트파일 = generateMultiPartFile(업데이트_테스트용_파일);

        // when
        String 업데이트된_업로드_이미지_주소 = imageService.update(미리_업로드한_이미지_주소, 업데이트_테스트용_멀티파트파일, ImageKind.FEED);
        String 업데이트된_업로드_이미지_이름 = 업데이트된_업로드_이미지_주소.replace(cloudfrontUrl, "");

        assertAll(
                () -> assertThat(업데이트된_업로드_이미지_주소).isNotNull(),
                () -> assertThat(amazonS3.doesObjectExist(bucketName, 업데이트된_업로드_이미지_이름)).isTrue(),
                () -> assertThat(amazonS3.doesObjectExist(bucketName, 미리_업로드한_이미지_이름)).isFalse()
        );
    }

    @DisplayName("이미지 업데이트 경우 이전 이미지가 기본 이미지일 시 삭제하지 않는다.")
    @Test
    void updateNotDelete() throws IOException {
        // given
        String 기본_이미지_주소 = cloudfrontUrl + 기본_이미지_이름;

        File 업데이트_테스트용_파일 = new File(new File("").getAbsolutePath() + FILE_PATH + 업데이트할_이미지_이름);
        MultipartFile 업데이트_테스트용_멀티파트파일 = generateMultiPartFile(업데이트_테스트용_파일);

        // when
        String 업데이트된_업로드_이미지_주소 = imageService.update(기본_이미지_주소, 업데이트_테스트용_멀티파트파일, ImageKind.FEED);
        String 업데이트된_업로드_이미지_이름 = 업데이트된_업로드_이미지_주소.replace(cloudfrontUrl, "");

        assertAll(
                () -> assertThat(업데이트된_업로드_이미지_주소).isNotNull(),
                () -> assertThat(amazonS3.doesObjectExist(bucketName, 기본_이미지_이름)).isTrue(),
                () -> assertThat(amazonS3.doesObjectExist(bucketName, 업데이트된_업로드_이미지_이름)).isTrue()
        );
    }

    private MultipartFile generateMultiPartFile(File file) throws IOException {
        FileItem fileItem = new DiskFileItem(업로드할_이미지_이름, Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());

        InputStream input = new FileInputStream(file);
        OutputStream os = fileItem.getOutputStream();
        IOUtils.copy(input, os);
        return new CommonsMultipartFile(fileItem);
    }
}