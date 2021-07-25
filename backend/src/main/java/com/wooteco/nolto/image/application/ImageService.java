package com.wooteco.nolto.image.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Transactional
@Service
public class ImageService {

    public static final String FILENAME_EXTENSION_DOT = ".";

    @Value("${application.bucket.name}")
    private String bucketName;

    @Value("${application.cloudfront.url}")
    private String cloudfrontUrl;

    @Value("${application.default-image}")
    private String defaultImage;

    private final AmazonS3 amazonS3Client;

    public ImageService(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public String upload(MultipartFile multipartFile) {
        if (isEmpty(multipartFile)) {
            return cloudfrontUrl + "/" + defaultImage;
        }
        File file = convertToFile(multipartFile);
        String fileName = getFileName(file);
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
        file.delete();
        return cloudfrontUrl + "/" + fileName;
    }

    public boolean isEmpty(MultipartFile multipartFile) {
        return Objects.isNull(multipartFile) || multipartFile.isEmpty();
    }

    private String getFileName(File file) {
        String fileOriginName = file.getName();
        int pos = fileOriginName.lastIndexOf(FILENAME_EXTENSION_DOT);
        String ext = FILENAME_EXTENSION_DOT + fileOriginName.substring(pos + 1);
        return System.currentTimeMillis() + Base64.encodeAsString(file.getName().getBytes()) + ext;
    }

    private File convertToFile(MultipartFile multipartFile) {
        File convertedFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new IllegalStateException("MultipartFile 변환에 실패하였습니다.");
        }
        return convertedFile;
    }

    public String update(String oldImageUrl, MultipartFile updateImage) {
        String imageUrl = oldImageUrl.replace(cloudfrontUrl, "");
        if (!isDefault(imageUrl) && amazonS3Client.doesObjectExist(bucketName, imageUrl)) {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, imageUrl));
        }
        return upload(updateImage);
    }

    private boolean isDefault(String fileName) {
        return defaultImage.equals(fileName);
    }
}

