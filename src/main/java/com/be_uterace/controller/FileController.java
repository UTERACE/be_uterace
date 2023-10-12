package com.be_uterace.controller;

import com.be_uterace.payload.response.FileResponse;
import com.be_uterace.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private FileService fileService;

    @Value("${project.image}")
    private String path;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> fileUpload(@RequestParam("image")MultipartFile image) {
        String fileName = null;
        try {
            fileName = fileService.uploadImage(path,image);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new FileResponse(fileName,"Image is not uploaded due to error on server!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new FileResponse(fileName,"Image is successfully uploaded"), HttpStatus.OK);
    }

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> viewImage(@PathVariable String fileName) {
        try {
            Resource resource = fileService.loadImage(fileName);

            if (resource != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // Thay đổi MediaType tùy thuộc vào định dạng hình ảnh
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
