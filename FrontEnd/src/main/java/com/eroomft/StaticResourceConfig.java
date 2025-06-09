package com.eroomft;

import org.springframework.beans.factory.annotation.Autowired;
import java.nio.file.Paths;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
    @Autowired
    private UploadConfig uploadConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = uploadConfig.getDirectory();
        uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize().toString().replace("\\", "/");
        if (!uploadDir.endsWith("/")) {
            uploadDir += "/";
        }
        String resourceLocation = "file:" + uploadDir;
        System.out.println("Mapping /Uploads/** to " + resourceLocation);
        registry.addResourceHandler("/Uploads/**")
                .addResourceLocations(resourceLocation)
                .setCachePeriod(0);
        registry.setOrder(Integer.MAX_VALUE);
    }
}