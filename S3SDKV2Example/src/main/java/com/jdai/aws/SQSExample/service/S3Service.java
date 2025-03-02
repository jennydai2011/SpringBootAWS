package com.jdai.aws.SQSExample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class S3Service {
    @Autowired
    S3Client s3Client;

    public void createBucket(String bucketName) {
        s3Client.createBucket(request -> request.bucket(bucketName));
    }

    public boolean bucketExists(String bucketName) {
        try {
            s3Client.headBucket(request -> request.bucket(bucketName));
            return true;
        } catch (NoSuchBucketException exception) {
            return false;
        }
    }

    public List<Bucket> listBuckets() {
        List<Bucket> allBuckets = new ArrayList<>();
        String nextToken = null;

        do {
            String continuationToken = nextToken;
            ListBucketsResponse listBucketsResponse = s3Client.listBuckets(
                    request -> request.continuationToken(continuationToken)
            );

            allBuckets.addAll(listBucketsResponse.buckets());
            nextToken = listBucketsResponse.continuationToken();
        } while (nextToken != null);

        return allBuckets;

    }

    public void deleteBucket(String bucketName) {

        s3Client.deleteBucket(request -> request.bucket(bucketName));
    }

    public void addFile(File file, String bucketName) {

        Map<String, String> metadata = new HashMap<>();
        metadata.put("company", "jennydai2025");
        metadata.put("environment", "development");

        s3Client.putObject(request ->
                        request
                                .bucket(bucketName)
                                .key(file.getName())
                                .metadata(metadata)
                                .ifNoneMatch("*"),
                file.toPath());
    }

    /**
     * @param bucketName
     * @param key        - file name
     */
    //TODO return byte[] or object?
    public void downloadFile(String bucketName, String key) {
        Path downloadPath = Paths.get("path-to-save-file");

        s3Client.getObject(request ->
                        request
                                .bucket(bucketName)
                                .key(key),
                ResponseTransformer.toFile(downloadPath));
    }

    public void copyFile(String sourceBucketName, String sourceKey, String destinationBucketName, String destinationKey){
        s3Client.copyObject(request ->
                request
                        .sourceBucket(sourceBucketName)
                        .sourceKey(sourceKey)
                        .destinationBucket(destinationBucketName)
                        .destinationKey(destinationKey));
    }

    public void deleteFile(String bucketName, String objectKey){
        s3Client.deleteObject(request ->
                request
                        .bucket(bucketName)
                        .key(objectKey));
    }

    public void deleteMultileFiles(List<String> objectKeys, String bucketName){
        List<ObjectIdentifier> objectsToDelete = objectKeys
                .stream()
                .map(key -> ObjectIdentifier
                        .builder()
                        .key(key)
                        .build())
                .toList();

        s3Client.deleteObjects(request ->
                request
                        .bucket(bucketName)
                        .delete(deleteRequest ->
                                deleteRequest
                                        .objects(objectsToDelete)));
    }

    public void listObjectsInBucket(Region AWS_REGION, String AWS_BUCKET) {
        S3Client s3Client = S3Client.builder()
                .region(AWS_REGION)
                .build();

        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                .bucket(AWS_BUCKET)
                .build();
        ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);

        List<S3Object> contents = listObjectsV2Response.contents();

        System.out.println("Number of objects in the bucket: " + contents.stream().count());
        contents.stream().forEach(System.out::println);

        s3Client.close();
    }

    /**
     * pagination of list of files
     * @param AWS_REGION
     * @param AWS_BUCKET
     */
    public void listAllObjectsInBucket(Region AWS_REGION, String AWS_BUCKET) {
        S3Client s3Client = S3Client.builder()
                .region(AWS_REGION)
                .build();
        String nextContinuationToken = null;
        long totalObjects = 0;

        do {
            ListObjectsV2Request.Builder requestBuilder = ListObjectsV2Request.builder()
                    .bucket(AWS_BUCKET)
                    .continuationToken(nextContinuationToken);

            ListObjectsV2Response response = s3Client.listObjectsV2(requestBuilder.build());
            nextContinuationToken = response.nextContinuationToken();

            totalObjects += response.contents().stream()
                    .peek(System.out::println)
                    .reduce(0, (subtotal, element) -> subtotal + 1, Integer::sum);
        } while (nextContinuationToken != null);
        System.out.println("Number of objects in the bucket: " + totalObjects);

        s3Client.close();
    }

    public void listAllObjectsInBucketPaginated(Region AWS_REGION, String AWS_BUCKET, int pageSize) {
        S3Client s3Client = S3Client.builder()
                .region(AWS_REGION)
                .build();

        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                .bucket(AWS_BUCKET )
                .maxKeys(pageSize) // Set the maxKeys parameter to control the page size
                .build();

        ListObjectsV2Iterable listObjectsV2Iterable = s3Client.listObjectsV2Paginator(listObjectsV2Request);
        long totalObjects = 0;

        for (ListObjectsV2Response page : listObjectsV2Iterable) {
            long retrievedPageSize = page.contents().stream()
                    .peek(System.out::println)
                    .reduce(0, (subtotal, element) -> subtotal + 1, Integer::sum);
            totalObjects += retrievedPageSize;
            System.out.println("Page size: " + retrievedPageSize);
        }
        System.out.println("Total objects in the bucket: " + totalObjects);

        s3Client.close();
    }

    void listAllObjectsInBucketPaginatedWithPrefix(Region AWS_REGION, String AWS_BUCKET, int pageSize, String prefix) {
        S3Client s3Client = S3Client.builder()
                .region(AWS_REGION)
                .build();
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                .bucket(AWS_BUCKET)
                .maxKeys(pageSize) // Set the maxKeys parameter to control the page size
                .prefix(prefix) // Set the prefix
                .build();

        ListObjectsV2Iterable listObjectsV2Iterable = s3Client.listObjectsV2Paginator(listObjectsV2Request);
        long totalObjects = 0;

        for (ListObjectsV2Response page : listObjectsV2Iterable) {
            long retrievedPageSize = page.contents().stream().count();
            totalObjects += retrievedPageSize;
            System.out.println("Page size: " + retrievedPageSize);
        }
        System.out.println("Total objects in the bucket: " + totalObjects);

        s3Client.close();
    }
}
