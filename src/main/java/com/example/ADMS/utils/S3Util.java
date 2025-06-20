package com.example.ADMS.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class S3Util {

    private final String bucketName = "emss-store";

    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_SOUTHEAST_1).build();

    }

    public void uploadPhoto(String key, File file) {
        this.s3Client.putObject(bucketName, key, file);
    }

    public byte[] downloadPhoto(String key) {

        S3Object s3object = this.s3Client.getObject(bucketName, key);
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        byte[] byteArray = null;
        try {
            byteArray = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArray;
    }

    public void deleteFile(String key) {
        this.s3Client.deleteObject(bucketName, key);
    }

    public URL getPresignedUrl(String objectKey) {
        Date expirationTime = new Date(System.currentTimeMillis() + 3600000); // Set expiration time (1 hour)

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
                .withExpiration(expirationTime);

        return s3Client.generatePresignedUrl(generatePresignedUrlRequest);
    }

    public File convertMultiPartToFile(MultipartFile file) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp", null);
            Files.copy(
                    file.getInputStream(),
                    tempFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tempFile;
    }
}

