package com.runwithme.runwithme.global.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Utils {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(File file, String fileType, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fullPath(fileType, fileName), file).withCannedAcl(CannedAccessControlList.PublicRead));

        file.delete();

        return amazonS3Client.getUrl(bucket, fullPath(fileType, fileName)).toString();
    }

    public Resource download(String fileType, String fileName) {
        S3ObjectInputStream ois = amazonS3Client.getObject(bucket, fullPath(fileType, fileName)).getObjectContent();

        return new InputStreamResource(ois);
    }

    public void delete(String fileType, String fileName) {
        amazonS3Client.deleteObject(bucket, fullPath(fileType, fileName));
    }

    private String fullPath(String fileType, String fileName) {
        return fileType + "/" + fileName;
    }
}