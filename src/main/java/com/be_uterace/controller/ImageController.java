package com.be_uterace.controller;

import com.be_uterace.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ImageController {

//    private final ResourceLoader resourceLoader;

//    public ImageController(ResourceLoader resourceLoader) {
//        this.resourceLoader = resourceLoader;
//    }

    @Autowired
    private FileService fileService;

    @GetMapping("/images/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
//        try {
//            Resource resource = resourceLoader.getResource("classpath:static/images/" + imageName);
//
//            if (resource.exists()) {
//                return ResponseEntity.ok()
//                        .contentType(MediaType.IMAGE_PNG)
//                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
//                        .body(resource);
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(null);
//        }
        return fileService.getImage(imageName);
    }
}
