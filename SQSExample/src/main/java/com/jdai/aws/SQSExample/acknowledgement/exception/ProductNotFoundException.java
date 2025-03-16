package com.jdai.aws.SQSExample.acknowledgement.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
