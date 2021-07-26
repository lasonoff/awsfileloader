package ru.yauroff.awsfileloader.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class S3Provider {

    private final String DELIMITER = "/";

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${s3.bucket}")
    private String bucketName;

    public void putObject(MultipartFile multipartFile, String fileLocation, String fileName) throws IOException {
        // Create temp file
        File tempFile = File.createTempFile(fileName, ".tmp");
        // Write data to temp file
        byte[] bytes = multipartFile.getBytes();
        BufferedOutputStream stream =
                new BufferedOutputStream(new FileOutputStream(tempFile));
        stream.write(bytes);
        stream.close();
        // Write to AWS S3
        amazonS3.putObject(
                bucketName,
                fileLocation + DELIMITER + fileName,
                tempFile
        );
        // Delete file
        tempFile.delete();
    }

    public File downloadObject(String fileLocation, String fileName) throws IOException {
        File tempFile = File.createTempFile(fileName, ".tmp");
        ObjectMetadata objectMetadata = amazonS3.getObject(new GetObjectRequest(bucketName,
                fileLocation + DELIMITER + fileName), tempFile);
        return tempFile;
    }

    public void deleteObject(String fileLocation, String fileName) {
        amazonS3.deleteObject(bucketName, fileLocation + DELIMITER + fileName);
    }
}
