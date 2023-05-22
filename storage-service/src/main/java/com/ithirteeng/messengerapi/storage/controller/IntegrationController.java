package com.ithirteeng.messengerapi.storage.controller;

import com.ithirteeng.messengerapi.common.model.FileDataDto;
import com.ithirteeng.messengerapi.storage.service.MinioFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для работы с интеграционными запросами
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/integration/file")
public class IntegrationController {

    private final MinioFileService minioFileService;

    /**
     * Метод для проверки файла на существование в хранилище
     *
     * @param id идентификатор файла в хранилище
     * @return {@link Boolean}
     */
    @GetMapping("/check/{id}")
    public Boolean checkFileExisting(@PathVariable("id") String id) {
        return minioFileService.checkIfFileExists(id);
    }

    /**
     * Метод для получения данных файла по его идентификатору
     *
     * @param id идентификатор файла в хранилище
     * @return {@link FileDataDto}
     */
    @GetMapping("/{id}")
    public FileDataDto getFileDataId(@PathVariable("id") String id) {
        return minioFileService.getFileDataById(id);
    }
}
