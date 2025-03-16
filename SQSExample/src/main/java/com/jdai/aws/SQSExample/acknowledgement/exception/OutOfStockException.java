package com.jdai.aws.SQSExample.acknowledgement.exception;

public class OutOfStockException extends RuntimeException {

    public OutOfStockException(String errorMessage) {
        super(errorMessage);
    }
}
