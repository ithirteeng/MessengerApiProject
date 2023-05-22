package com.ithirteeng.messengerapi.storage.service;

import com.ithirteeng.messengerapi.common.exception.FileException;
import com.ithirteeng.messengerapi.storage.utils.MinioConfig;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioFileService {

    private final MinioClient minioClient;

    private final MinioConfig minioConfig;

    @PostConstruct
    public void init() {
        log.info("Minio configs: {}", minioConfig);
    }

    public String uploadFile(byte[] content) {
        try {
            var id = UUID.randomUUID().toString();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioConfig.getBucket())
                    .object(id)
                    .stream(new ByteArrayInputStream(content), content.length, -1)
                    .build());
            return id;
        } catch (Exception e) {
            throw new FileException("File Upload Error", e);
        }
    }

    public byte[] downloadFile(String id) {
        var args = GetObjectArgs.builder()
                .bucket(minioConfig.getBucket())
                .object(id)
                .build();
        try (var in = minioClient.getObject(args)) {
            return in.readAllBytes();
        } catch (Exception e) {
            throw new FileException("File Download Error ID: " + id, e);
        }
    }

    public Boolean checkIfFileExists(String id) {
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(minioConfig.getBucket())
                    .object(id).build());
            return true;
        } catch (ErrorResponseException e) {
            return false;
        } catch (Exception e) {
            throw new FileException("File Download Error ID: " + id, e);
        }
    }

}
