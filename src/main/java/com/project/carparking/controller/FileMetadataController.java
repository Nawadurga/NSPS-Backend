package com.project.carparking.controller;

import com.project.carparking.dto.ImageUrlWithCreationDate;
import com.project.carparking.service.FileMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
public class FileMetadataController {

    @Autowired
    private FileMetadataService imageService;


    @GetMapping("/{id}")
    public ResponseEntity<Resource> getImage(@PathVariable Long id) {
        return imageService.getImageResponse(id);
    }

    @GetMapping
    public ResponseEntity<List<ImageUrlWithCreationDate>> getAllImageMetadata() {
        List<ImageUrlWithCreationDate> fileUrls = imageService.getAllImageUrlsWithCreationDate();
        return ResponseEntity.ok(fileUrls);
    }

    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) {
        return imageService.uploadFile(file);
    }
}