package com.jdai.aws.SQSExample.introduction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

//@SpringBootApplication
@EnableConfigurationProperties(EventQueuesProperties.class)
public class SpingCloudAwsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpingCloudAwsApplication.class, args);
    }
}
