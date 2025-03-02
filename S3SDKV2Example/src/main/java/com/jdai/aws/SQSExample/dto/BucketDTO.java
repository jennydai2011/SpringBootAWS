package com.jdai.aws.SQSExample.dto;

import software.amazon.awssdk.services.s3.model.Bucket;

public class BucketDTO {
    private String name;
    private String creationDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public static BucketDTO fromBucket(Bucket bucket) {
        BucketDTO dto = new BucketDTO();
        dto.setName(bucket.name());
        dto.setCreationDate(bucket.creationDate().toString());
        return dto;
    }
}
