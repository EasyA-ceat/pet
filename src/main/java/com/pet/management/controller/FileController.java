package com.pet.management.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private static final String PHOTO_UPLOAD_DIR = System.getProperty("user.home") + "/.pet-management-system/photos/";

    @GetMapping("/photos/{fileName}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(PHOTO_UPLOAD_DIR + fileName);
            File file = filePath.toFile();
            
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(file);
            
            // 确定文件的媒体类型
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/photos/thumb/{fileName}")
    public ResponseEntity<Resource> getThumbnail(@PathVariable String fileName) {
        try {
            String thumbFileName = "thumb_" + fileName;
            Path filePath = Paths.get(PHOTO_UPLOAD_DIR + thumbFileName);
            File file = filePath.toFile();
            
            if (!file.exists()) {
                // 如果缩略图不存在，尝试返回原图
                return getPhoto(fileName);
            }

            Resource resource = new FileSystemResource(file);
            
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
