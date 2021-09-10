package com.wooteco.nolto.image.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class ImageService {

    public static final String FILENAME_EXTENSION_DOT = ".";

    @Value("${application.bucket.name}")
    private String bucketName;

    @Value("${application.cloudfront.url}")
    private String cloudfrontUrl;

    private final AmazonS3 amazonS3Client;
    private final ImageResizeService imageResizeService;

    public String upload(MultipartFile multipartFile, ImageKind imageKind) {
        if (isEmpty(multipartFile)) {
            return cloudfrontUrl + imageKind.defaultName();
        }
        File file = convertToFile(multipartFile);
        String fileName = getFileName(file);
        File resizedFile = imageResizeService.resize(file, fileName);
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, resizedFile));
        try {
            Files.delete(Paths.get(file.getPath()));
            Files.delete(Paths.get(resizedFile.getPath()));
        } catch (Exception e) {
            return cloudfrontUrl + fileName;
        }
        return cloudfrontUrl + fileName;
    }

    public boolean isEmpty(MultipartFile multipartFile) {
        return Objects.isNull(multipartFile) || multipartFile.isEmpty();
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

    private String getFileName(File file) {
        String fileOriginName = file.getName();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String extension = FilenameUtils.getExtension(fileOriginName);
        return uuid + FILENAME_EXTENSION_DOT + extension;
    }

    public String update(String oldImageUrl, MultipartFile updateImage, ImageKind imageKind) {
        String imageName = oldImageUrl.replace(cloudfrontUrl, "");
        if (ImageKind.isDefault(imageName) && amazonS3Client.doesObjectExist(bucketName, imageName)) {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, imageName));
        }
        return upload(updateImage, imageKind);
    }
}

