package com.jdai.aws.SQSExample.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {
    @Value("${accessKey}")
    private String accessKey;

    @Value("${secretKey}")
    private String secretKey;

    @Value("${region}")
    private String region;

    @Bean
    public AwsCredentials credentials(){
        AwsCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        return credentials;
    }

    @Bean
    public S3Client s3Client(){
        S3Client s3Client = S3Client
                .builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials()))
                .build();
        return s3Client;
    }
}
