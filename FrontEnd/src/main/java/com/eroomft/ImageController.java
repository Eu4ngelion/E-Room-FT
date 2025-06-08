package com.eroomft;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
public class ImageController {
    private final String uploadDirectory;

    @Autowired
    public ImageController(UploadConfig uploadConfig) {
        this.uploadDirectory = uploadConfig.getDirectory();
    }

    @GetMapping("/Uploads/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable("filename") String filename) throws IOException {
        System.out.println("Received request for image: /Uploads/" + filename);
        File file = new File(uploadDirectory, filename);
        System.out.println("Serving image: " + file.getAbsolutePath());
        if (!file.exists() || !file.isFile() || file.length() == 0) {
            System.err.println("Image not found or empty: " + file.getAbsolutePath());
            return ResponseEntity.notFound().build();
        }
        if (!file.canRead()) {
            System.err.println("Cannot read image file: " + file.getAbsolutePath());
            return ResponseEntity.status(403).build();
        }
        Resource resource = new FileSystemResource(file);
        String contentType = "image/jpeg";
        if (filename.toLowerCase().endsWith(".png")) {
            contentType = "image/png";
        } else if (filename.toLowerCase().endsWith(".jpg")) {
            contentType = "image/jpg";
        }
        System.out.println("Serving image with content-type: " + contentType);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}