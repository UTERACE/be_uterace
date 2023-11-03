package com.be_uterace.service.impl;

import com.be_uterace.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    @Override
    public String saveImage(String base64String) {
        try {
            String base64Image = base64String.split(",")[1];
            byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
            if (decodedBytes.length > MAX_IMAGE_SIZE) {
                throw new RuntimeException("Image size exceeds the maximum allowed size");
            }
            String imageName= UUID.randomUUID().toString()+".png";
            Path path = Paths.get(UPLOAD_DIR+imageName);
            Files.write(path,decodedBytes);
            return imageName;
        } catch (Exception e) {
            throw new RuntimeException("Could not save image", e);
        }
    }

    @Override
    public boolean deleteImage(String imageName) {
        try {
            Path path = Paths.get(UPLOAD_DIR+imageName.replace(PATH_IMAGE,""));
            if (Files.exists(path)) {
                Files.delete(path);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
