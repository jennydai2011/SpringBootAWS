package com.jdai.aws.SQSExample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdai.aws.SQSExample.dto.BucketDTO;
import com.jdai.aws.SQSExample.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class S3Controller {
    @Autowired
    private S3Service s3Service;

    @GetMapping("/s3/addBucket/{bucketName}")
    public ResponseEntity<String> addBucket(@PathVariable("bucketName") String bucketName){
        s3Service.createBucket(bucketName);
        return new ResponseEntity<>("add bucket successfully", HttpStatus.OK);
    }

    @GetMapping("/s3/bucketExists/{bucketName}")
    public ResponseEntity<String> bucketExists(@PathVariable("bucketName") String bucketName){
        boolean result = s3Service.bucketExists(bucketName);
        return new ResponseEntity<>("bucketExists result is:"+result, HttpStatus.OK);
    }

    @GetMapping("/s3/listBuckets")
    //public List<Bucket> listBuckets(){
    public List<BucketDTO> listBuckets(){
        //ERROR
//        Caused by: com.fasterxml.jackson.databind.exc.InvalidDefinitionException:
//        No serializer found for class software.amazon.awssdk.services.s3.model.Bucket
//        return s3Service.listBuckets();

        //ObjectMapper objectMapper = new ObjectMapper();
        //String json = objectMapper.writeValueAsString(bucketDTOs);

        //have to use a new DTO to return json object


        List<Bucket> buckets = s3Service.listBuckets();
        List<BucketDTO> bucketDTOs = buckets.stream()
                .map(BucketDTO::fromBucket)
                .collect(Collectors.toList());

        return bucketDTOs;

    }

    //TODO should use @DeleteMapping
    @GetMapping("/s3/deleteBucket/{bucketName}")
    public ResponseEntity<String> deleteBucket(@PathVariable("bucketName") String bucketName){
        try {
            s3Service.deleteBucket(bucketName);
            return new ResponseEntity<>("deleteBucket successfully", HttpStatus.OK);
        } catch (S3Exception exception) {
            return new ResponseEntity<>("deleteBucket failure:"+exception.getMessage(), HttpStatus.OK);
        }
    }
}
