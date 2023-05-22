package com.ithirteeng.messengerapi.storage.service;

import com.ithirteeng.messengerapi.common.exception.FileException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.model.FileDataDto;
import com.ithirteeng.messengerapi.storage.entity.FilesEntity;
import com.ithirteeng.messengerapi.storage.repository.FilesRepository;
import com.ithirteeng.messengerapi.storage.utils.MinioConfig;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.UUID;

/**
 * Сервис для работы с файлами
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MinioFileService {

    private final MinioClient minioClient;

    private final MinioConfig minioConfig;

    private final FilesRepository filesRepository;

    /**
     * Метод для загрузки файла
     *
     * @param content сам файл в виде массива байтов
     * @return идентификатор файла в minio
     * @throws FileException в случае возникновения каких-либо ошибок при загрузке
     */
    public FileDataDto uploadFile(byte[] content, String fileName) {
        try {
            var id = UUID.randomUUID().toString();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioConfig.getBucket())
                    .object(id)
                    .stream(new ByteArrayInputStream(content), content.length, -1)
                    .build());
            filesRepository.save(createEntity(content, fileName, id));
            return FileDataDto.builder()
                    .fileName(fileName)
                    .fileId(id)
                    .fileSize(content.length)
                    .build();
        } catch (Exception e) {
            throw new FileException("File Upload Error", e);
        }
    }

    /**
     * Метод для создания объекта {@link FilesEntity}
     *
     * @param content  массив байтов файла
     * @param fileName имя файла
     * @param id       идентификатор файла
     * @return {@link FilesEntity}
     */
    private FilesEntity createEntity(byte[] content, String fileName, String id) {
        return FilesEntity.builder()
                .id(id)
                .fileSize(content.length)
                .name(fileName)
                .build();
    }

    /**
     * Метод для скачивания файла по его идентификатору в minio
     *
     * @param id идентификатор файла
     * @return массив байтов
     * @throws FileException в случае каких-либо ошибок при скачивании
     */
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

    /**
     * Метод для проверки на существование файла в хранилище
     *
     * @param id идентификатор файла
     * @return {@link Boolean}
     * @throws FileException в случае возникновения каких-либо ошибок при проверке
     */
    public Boolean checkIfFileExists(String id) {
        return filesRepository.existsById(id);
    }

    /**
     * Метод для получения данных о файле по его идентификатору
     *
     * @param id идентификатор файла в хранилище
     * @return {@link FileDataDto}
     */
    public FileDataDto getFileDataById(String id) {
        var entity = filesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Такого файла не существует"));
        return FileDataDto.builder()
                .fileSize(entity.getFileSize())
                .fileName(entity.getName())
                .fileId(entity.getId())
                .build();
    }


}
