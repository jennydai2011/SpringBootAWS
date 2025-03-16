package com.jdai.aws.SQSExample.introduction;

public record UserCreatedEvent(String id, String username, String email) {

}
