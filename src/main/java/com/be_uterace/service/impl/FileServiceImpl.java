package com.be_uterace.service.impl;

import com.be_uterace.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Value("${project.image}")
    private String UPLOAD_DIR;

    @Value("${max.file.upload}")
    private long MAX_IMAGE_SIZE;
    @Value("${path.image}")
    private String PATH_IMAGE;
    @Autowired
    private final ResourceLoader resourceLoader;

    public FileServiceImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public String saveImage(String base64String) {
        try {
            String base64Image = base64String.split(",")[1];
            byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
            if (decodedBytes.length > MAX_IMAGE_SIZE) {
                throw new RuntimeException("Image size exceeds the maximum allowed size");
            }
            String imageName= UUID.randomUUID().toString()+".png";
            String staticFolderPath = resourceLoader.getResource("classpath:static").getURI().getPath();

            // Create the path for the new image
            Path imagePath = Paths.get(staticFolderPath, "images", imageName);

            Files.write(imagePath,decodedBytes);
            return imageName;
        } catch (Exception e) {
            throw new RuntimeException("Could not save image", e);
        }
    }

    @Override
    public boolean deleteImage(String imageName) {
        try {
            String staticFolderPath = resourceLoader.getResource("classpath:static").getURI().getPath();

            Path path = Paths.get(staticFolderPath+imageName.replace(PATH_IMAGE,""));
            if (Files.exists(path)) {
                Files.delete(path);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
