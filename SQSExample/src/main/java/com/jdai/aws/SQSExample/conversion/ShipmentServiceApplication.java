package com.jdai.aws.SQSExample.conversion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
public class ShipmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ShipmentServiceApplication.class);
        app.setAdditionalProfiles("shipping");
        app.run(args);
    }
}