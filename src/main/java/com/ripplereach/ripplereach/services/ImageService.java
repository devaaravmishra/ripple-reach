package com.ripplereach.ripplereach.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String saveImage(MultipartFile imageFile);
    byte[] retrieveImage(String filePath);
}
