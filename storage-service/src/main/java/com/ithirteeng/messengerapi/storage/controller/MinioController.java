package com.ithirteeng.messengerapi.storage.controller;

import com.ithirteeng.messengerapi.storage.service.MinioFileService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class MinioController {
    private final MinioFileService minioFileService;

    @SneakyThrows
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        return minioFileService.uploadFile(file.getBytes()) + " " + file.getOriginalFilename();
    }

    @GetMapping("/download/{id}/{file}")
    public ResponseEntity<byte[]> download(@PathVariable("id") String id, @PathVariable("file") String fileName) {
        var content = minioFileService.downloadFile(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "filename=" + fileName)
                .contentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .body(content);
    }
}
