package com.eroomft;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
@ConfigurationProperties(prefix = "upload")
public class UploadConfig {
    private String directory;

    public String getDirectory() {
        if (directory == null || directory.isEmpty()) {
            directory = Paths.get("/app/uploads").toAbsolutePath().toString();
        }
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    @PostConstruct
    public void init() {
        try {
            File uploadDir = new File(getDirectory());
            if (!uploadDir.exists()) {
                Files.createDirectories(Paths.get(uploadDir.getAbsolutePath()));
                System.out.println("Created upload directory: " + uploadDir.getAbsolutePath());
            }
            if (!uploadDir.canWrite()) {
                System.err.println("Upload directory is not writable: " + uploadDir.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize upload directory: " + e.getMessage());
        }
    }
}