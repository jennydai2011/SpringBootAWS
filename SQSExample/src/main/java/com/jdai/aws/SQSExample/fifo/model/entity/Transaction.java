package com.jdai.aws.SQSExample.fifo.model.entity;

import java.util.UUID;

public record Transaction(UUID transactionId, UUID accountId, double amount, TransactionType type) {}
