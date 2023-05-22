package com.ithirteeng.messengerapi.storage.controller;

import com.ithirteeng.messengerapi.common.model.FileDataDto;
import com.ithirteeng.messengerapi.storage.service.MinioFileService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Контроллер для работы с файлами со стороны клиента
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class MinioController {
    private final MinioFileService minioFileService;

    /**
     * Метод для загрузки файла
     *
     * @param file файл в формате {@link MultipartFile}
     * @return {@link FileDataDto} - ДТО с данными для файла
     */
    @SneakyThrows
    @PostMapping("/upload")
    public FileDataDto upload(@RequestParam("file") MultipartFile file) {
        return minioFileService.uploadFile(file.getBytes(), file.getOriginalFilename());
    }

    /**
     * Метод для загрузки бинарного файла
     *
     * @param id идентификатор файла
     * @return {@link ResponseEntity} с хэдерами для скачивания
     */
    @GetMapping("/download/binary/{id}")
    public ResponseEntity<byte[]> downloadBinary(@PathVariable("id") String id) {
        var content = minioFileService.downloadFile(id);
        return ResponseEntity.ok()
                .header("Content-Disposition")
                .contentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .body(content);
    }

    /**
     * Метод для скачивания файла с его названием (если оно корректноеЖ filename.extension, то скачает и откроет нормально)
     *
     * @param id       идентификатор файла
     * @param fileName имя файла
     * @return {@link ResponseEntity} с хэдерами для скачивания
     */
    @GetMapping("/download/{id}/{file}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("id") String id, @PathVariable("file") String fileName) {
        var content = minioFileService.downloadFile(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "filename=" + fileName)
                .contentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .body(content);
    }

    /**
     * Метод для загрузки файла c его дефолтным именем
     *
     * @param id идентификатор файла
     * @return {@link ResponseEntity} с хэдерами для скачивания
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadDefault(@PathVariable("id") String id) {
        var data = minioFileService.getFileDataById(id);
        var content = minioFileService.downloadFile(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "filename=" + data.getFileName())
                .contentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .body(content);
    }

}
