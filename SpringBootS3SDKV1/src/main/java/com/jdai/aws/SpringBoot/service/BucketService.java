package com.jdai.aws.SpringBoot.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BucketService {

    @Autowired
    private FileStore fileStore;

    Logger logger = LogManager.getLogger(BucketService.class);

    public void downloadFile(String fileName, AmazonS3 amazonS3, String bucketName){
        logger.info("File to be fetched from S3 {}", fileName);

        try {
            S3Object s3object = amazonS3.getObject(bucketName, fileName);
            InputStream objectContent = s3object.getObjectContent();
            String content = IOUtils.toString(objectContent);

            logger.info("Content {}", content);
        } catch (IOException e) {
            logger.error("Error in reading file content {}", e.getMessage());
        } catch (AmazonS3Exception s3Exception) {
            logger.error("Some error occured", s3Exception.getMessage());
        }
    }

    /**
     * Calls FileStore.java class to create bucket on AWS S3
     *
     * @param bucketName
     * @return
     */
    public String createBucket(String bucketName) {
        return fileStore.createBucket(bucketName);
    }

    /**
     * Calls FileStore.java upload file on AWS S3, first validates file is empty if
     * then throw Excepiton
     *
     * @param file
     * @return
     */
    public String uploadFile(MultipartFile file, String bucketName) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file");
        }
        try {
            fileStore.uploadFiletoBucket(file, bucketName);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to upload file", e);
        }
        return "File Uploaded Successfully";
    }

    public String deleteBucket(String bucketName) {
        fileStore.deleteBucket(bucketName);
        return "Bucket deleted successfully";
    }

    public String deleteFile(String bucketName, String fileName) {
        fileStore.deletFile(bucketName,fileName);
        return "File deleted successfully";
    }
}
