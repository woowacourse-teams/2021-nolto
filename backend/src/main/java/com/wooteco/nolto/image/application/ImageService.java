package com.wooteco.nolto.image.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.Base64;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.InternalServerErrorException;
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

    private final AmazonS3 amazonS3Client;

    public ImageService(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public String upload(MultipartFile multipartFile, ImageKind imageKind) {
        if (isEmpty(multipartFile)) {
            return cloudfrontUrl + imageKind.defaultName();
        }
        File file = convertToFile(multipartFile);
        String fileName = getFileName(file);
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
        file.delete();
        return cloudfrontUrl + fileName;
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
            throw new InternalServerErrorException(ErrorType.MULTIPART_CONVERT_FAIL);
        }
        return convertedFile;
    }

    public String update(String oldImageUrl, MultipartFile updateImage, ImageKind imageKind) {
        String imageName = oldImageUrl.replace(cloudfrontUrl, "");
        if (ImageKind.isDefault(imageName) && amazonS3Client.doesObjectExist(bucketName, imageName)) {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, imageName));
        }
        return upload(updateImage, imageKind);
    }
}

