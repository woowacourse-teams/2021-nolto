package com.wooteco.nolto.image.infrastructure;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.wooteco.nolto.image.application.domain.ProcessedImage;
import com.wooteco.nolto.image.application.domain.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AwsS3Repository implements ImageRepository {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Value("${application.cloudfront.url}")
    private String cloudfrontUrl;

    private final AmazonS3 amazonS3Client;

    @Override
    public String save(ProcessedImage processedImage) {
        amazonS3Client.putObject(new PutObjectRequest(bucketName, processedImage.getImageName(), processedImage.getFile()));
        return cloudfrontUrl + processedImage.getImageName();
    }
}
