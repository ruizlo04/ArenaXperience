package com.example.ApiArenaXperience.files.controller;

import com.example.ApiArenaXperience.files.service.StorageService;
import com.example.ApiArenaXperience.files.utils.MimeTypeDetector;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final StorageService storageService;
    private final MimeTypeDetector mimeTypeDetector;

    @GetMapping("/download/{id:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String id) {
        Resource resource = storageService.loadAsResource(id);

        String mimeType = mimeTypeDetector.getMimeType(resource);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", mimeType)
                .body(resource);
    }

}
