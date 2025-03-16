package com.jdai.aws.SQSExample.acknowledgement.model;

import java.util.UUID;

public record OrderCreatedEvent(UUID id, UUID productId, int quantity) {

}