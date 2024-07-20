package com.ripplereach.ripplereach.services.impl;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.SignUrlOption;
import com.google.firebase.cloud.StorageClient;
import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.services.ImageService;
import com.ripplereach.ripplereach.utilities.ImageUtil;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

  @Value("${storage.bucket}")
  private String bucketName;

  private final Storage storage;

  public ImageServiceImpl() {
    this.storage = StorageClient.getInstance().bucket().getStorage();
  }

  @Override
  public String saveImage(MultipartFile imageFile) {
    try {
      String imageFormat = getFormatName(imageFile.getOriginalFilename());
      byte[] compressedImage = ImageUtil.compressImage(imageFile.getBytes(), imageFormat);
      String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();

      BlobInfo blobInfo =
          BlobInfo.newBuilder(bucketName, "images/" + fileName)
              .setContentType(imageFile.getContentType())
              .build();

      storage.create(blobInfo, compressedImage);

      log.info("Image saved successfully with name: {}", fileName);

      return "images/" + fileName; // Return the relative URL (file name)
    } catch (IOException e) {
      log.error("Error while saving image", e);
      throw new RippleReachException("Error while saving image");
    }
  }

  @Override
  public byte[] retrieveImage(String fileName) {
    try {
      BlobId blobId = BlobId.of(bucketName, fileName);
      return storage.readAllBytes(blobId);
    } catch (Exception e) {
      log.error("Error while retrieving image from path: {}", fileName, e);
      throw new RippleReachException("Error while retrieving image");
    }
  }

  @Cacheable(value = "signedUrls", key = "#fileName")
  public String generateSignedUrl(String fileName) {
    try {
      BlobId blobId = BlobId.of(bucketName, fileName);
      BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

      return storage
          .signUrl(blobInfo, 7, TimeUnit.DAYS, SignUrlOption.withV4Signature())
          .toString();
    } catch (Exception e) {
      log.error("Error generating signed URL for file: {}", fileName, e);
      throw new RippleReachException("Error generating signed URL");
    }
  }

  private String getFormatName(String filename) {
    if (filename == null) {
      throw new IllegalArgumentException("Filename cannot be null");
    }

    Pattern pattern = Pattern.compile("\\.(\\w+)$");
    Matcher matcher = pattern.matcher(filename);

    if (matcher.find()) {
      return matcher.group(1).toLowerCase();
    } else {
      throw new IllegalArgumentException("Invalid file name: " + filename);
    }
  }
}
