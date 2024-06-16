package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.services.ImageService;
import com.ripplereach.ripplereach.utilities.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final String imageDirectory = "images/";

    public ImageServiceImpl() {
        createDirectoryIfNotExists();
    }

    public String saveImage(MultipartFile imageFile) {
        try {
            byte[] compressedImage = ImageUtil.compressImage(imageFile.getBytes());
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            String filePath = imageDirectory + fileName;

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(compressedImage);
            }

            log.info("Image saved successfully with name: {}", fileName);
            return filePath;
        } catch (IOException e) {
            log.error("Error while saving image", e);
            throw new RippleReachException("Error while saving image");
        }
    }

    public byte[] retrieveImage(String filePath) {
        try {
            byte[] compressedImage = Files.readAllBytes(Paths.get(filePath));
            return ImageUtil.decompressImage(compressedImage);
        } catch (IOException e) {
            log.error("Error while retrieving image from path: {}", filePath, e);
            throw new RippleReachException("Error while retrieving image");
        }
    }

    private void createDirectoryIfNotExists() {
        File directory = new File(imageDirectory);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                log.info("Image directory created at: {}", imageDirectory);
            } else {
                log.error("Failed to create image directory at: {}", imageDirectory);
                throw new RippleReachException("Failed to create image directory");
            }
        }
    }
}
