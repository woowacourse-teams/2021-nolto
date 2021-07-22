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

@Transactional
@Service
public class ImageService {
    private String defaultImageUrl = "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png";

    @Value("${application.bucket.name}")
    private String bucketName;

    @Value("${application.cloudfront.url}")
    private String cloudfrontUrl;

    private final AmazonS3 amazonS3Client;

    public ImageService(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public String upload(MultipartFile multipartFile) {
        File file = convertToFile(multipartFile);
        String fileName = System.currentTimeMillis() + Base64.encodeAsString(multipartFile.getName().getBytes()) + multipartFile.getContentType();
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
        file.delete();
        return cloudfrontUrl + "/" + fileName;
    }

    private File convertToFile(MultipartFile multipartFile) {
        File convertedFile = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new IllegalStateException("파일 변환 실패!");
        }
        return convertedFile;
    }

    public String update(String oldImageUrl, MultipartFile updateImage) {
        String imageUrl = oldImageUrl.split(cloudfrontUrl)[1];
        if (!isDefault(imageUrl) && amazonS3Client.doesObjectExist(bucketName, imageUrl)) {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, imageUrl));
        }
        return upload(updateImage);
    }

    private boolean isDefault(String fileName) {
        return defaultImageUrl.equals(fileName);
    }
}

