package com.jdai.aws.SQSExample.acknowledgement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderProcessingApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(OrderProcessingApplication.class);
        app.setAdditionalProfiles("acknowledgement");
        app.run(args);
    }

}
