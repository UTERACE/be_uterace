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
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Value("${project.image}")
    private String path;
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        //File name
        String name = file.getOriginalFilename();
        //random name generate file
        String randomID = UUID.randomUUID().toString();
        assert name != null;
        String fileName1 = randomID.concat(name.substring(name.lastIndexOf(".")));

        //Fullpath
        String filePath = path + File.separator + fileName1;
        //create folder if not created
        File f = new File(path);
        if(!f.exists()){
            f.mkdir();

        }
        //File copy
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return name;
    }

    public Resource loadImage(String fileName) {
        // Thay đổi đường dẫn dựa trên cấu hình của bạn
        String imagePath = path + fileName;

        // Đọc tệp hình ảnh từ hệ thống tệp
        try {
            FileInputStream fileInputStream = new FileInputStream(imagePath);
            InputStreamResource resource = new InputStreamResource(fileInputStream);
            return resource;
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Trả về null nếu có lỗi
        }
    }


}
