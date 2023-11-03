package com.be_uterace.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String saveImage(String base64String);
    boolean deleteImage(String imageName);
}
