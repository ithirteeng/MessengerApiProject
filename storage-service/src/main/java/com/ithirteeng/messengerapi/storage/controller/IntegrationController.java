package com.ithirteeng.messengerapi.storage.controller;

import com.ithirteeng.messengerapi.storage.service.MinioFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/integration/file")
public class IntegrationController {

    private final MinioFileService minioFileService;

    @GetMapping("/check/{id}")
    public Boolean checkFileExisting(@PathVariable("id") String id) {
        return minioFileService.checkIfFileExists(id);
    }
}
