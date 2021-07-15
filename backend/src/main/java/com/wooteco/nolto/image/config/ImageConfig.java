package com.wooteco.nolto.image.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageConfig {
    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonS3 generateS3Client() {
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .build();
    }
}
