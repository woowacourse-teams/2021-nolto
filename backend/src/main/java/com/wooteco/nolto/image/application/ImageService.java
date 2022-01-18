package com.wooteco.nolto.image.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.wooteco.nolto.exception.BadRequestException;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.InternalServerErrorException;
import com.wooteco.nolto.image.application.adapter.ImageHandlerAdapter;
import com.wooteco.nolto.image.domain.ProcessedImage;
import com.wooteco.nolto.image.infrastructure.AwsS3Repository;
import com.wooteco.nolto.image.infrastructure.LocalImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Value("${application.cloudfront.url}")
    private String cloudfrontUrl;

    private final AmazonS3 amazonS3Client;
    private final AwsS3Repository awsS3Repository;
    private final List<ImageHandlerAdapter> imageHandlerAdapters;
    private final LocalImageRepository localImageRepository;

    public String upload(MultipartFile multipartFile, ImageKind imageKind) {
        if (isEmpty(multipartFile)) {
            return cloudfrontUrl + imageKind.defaultName();
        }
        File file = convertToFile(multipartFile);
        ImageHandlerAdapter imageHandlerAdapter = findImageHandlerAdapter(file);
        ProcessedImage processedImage = imageHandlerAdapter.handle(file);
        String savedFileName = awsS3Repository.save(processedImage);
        deleteFilesAfterWork(file, processedImage);
        return savedFileName;
    }

    public boolean isEmpty(MultipartFile multipartFile) {
        return Objects.isNull(multipartFile) || multipartFile.isEmpty();
    }

    private ImageHandlerAdapter findImageHandlerAdapter(File file) {
        return imageHandlerAdapters.stream()
                .filter(imageHandlerAdapter -> imageHandlerAdapter.supported(file.getName()))
                .findAny().orElseThrow(() -> new BadRequestException(ErrorType.NOT_SUPPORTED_IMAGE));
    }

    private File convertToFile(MultipartFile multipartFile) {
        File convertedFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new InternalServerErrorException(ErrorType.MULTIPART_CONVERT_FAIL);
        }
        return convertedFile;
    }

    private void deleteFilesAfterWork(File file, ProcessedImage processedImage) {
        try {
            Files.delete(Paths.get(processedImage.getFile().getPath()));
            deleteOriginalFile(file, processedImage);
        } catch (Exception e) {
            log.error("파일 변환 후 잔여 파일 삭제 실패 -- 원본 파일: {}, 변경 파일: {}, 에러 메시지: {}",
                    file.getPath(), processedImage.getFile().getPath(), e.getMessage());
        }
    }

    private void deleteOriginalFile(File file, ProcessedImage processedImage) throws IOException {
        if (file != processedImage.getFile()) {
            Files.delete(Paths.get(file.getPath()));
        }
    }

    public String update(String oldImageUrl, MultipartFile updateImage, ImageKind imageKind) {
        String imageName = oldImageUrl.replace(cloudfrontUrl, "");
        if (ImageKind.isDefault(imageName) && amazonS3Client.doesObjectExist(bucketName, imageName)) {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, imageName));
        }
        return upload(updateImage, imageKind);
    }

    public byte[] getFile(String fileName) {
        return localImageRepository.findFile(fileName);
    }
}
