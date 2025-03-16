package com.jdai.aws.SQSExample;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

//configure local test environment using Testcontainers and LocalStack
@Configuration
public class SqsLiveTestConfiguration {

    private static final String LOCAL_STACK_VERSION = "localstack/localstack:3.4.0";

    @Bean
    @ServiceConnection
    LocalStackContainer localStackContainer() {
        return new LocalStackContainer(DockerImageName.parse(LOCAL_STACK_VERSION));
    }
}
