package com.be_uterace.service.impl;

import com.be_uterace.service.FileService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Base64;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {
    @Value("${max.file.upload}")
    private long MAX_IMAGE_SIZE;

    @Autowired
    private final ResourceLoader resourceLoader;

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

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
            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", cloudName,
                    "api_key", apiKey,
                    "api_secret", apiSecret
            ));

            Map<?, ?> uploadResult = cloudinary.uploader().upload(decodedBytes, ObjectUtils.emptyMap());
            // Lấy public ID của ảnh sau khi upload
            String publicId = (String) uploadResult.get("public_id");

            // Tạo đường dẫn đầy đủ đến ảnh
            String imageUrl = cloudinary.url().generate(publicId);

            return imageUrl;
        } catch (Exception e) {
            throw new RuntimeException("Could not save image", e);
        }
    }

    @Override
    public boolean deleteImage(String imageName) {
        try {
            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", cloudName,
                    "api_key", apiKey,
                    "api_secret", apiSecret
            ));
            URL url = new URL(imageName);
            String path = url.getPath();

            // Lấy public ID từ path
            String[] parts = path.split("/");
            String publicId = parts[parts.length - 1].split("\\.")[0];
            System.out.println(publicId);
            Map<?, ?> deletionResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            if (deletionResult.get("result").equals("ok")) {
                return true;
            } else {
                System.err.println("Error deleting image: " + deletionResult.get("result"));
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public ResponseEntity<Resource> getImage(String imageName) {
        try {
            Cloudinary cloudinary = new Cloudinary(Map.of(
                    "cloud_name", cloudName,
                    "api_key", apiKey,
                    "api_secret", apiSecret
            ));
            String imageUrl = cloudinary.url().generate(imageName);

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.LOCATION, imageUrl);

            return ResponseEntity.status(302) // Redirect status code
                    .headers(headers)
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
