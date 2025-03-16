package com.jdai.aws.SQSExample.fifo.model.event.event;

import com.jdai.aws.SQSExample.fifo.model.entity.Transaction;
import com.jdai.aws.SQSExample.fifo.model.entity.TransactionType;

import java.util.UUID;

public record TransactionEvent(UUID transactionId, UUID accountId, double amount, TransactionType type) {

    public Transaction toEntity() {
        return new Transaction(transactionId, accountId, amount, type);
    }

}
