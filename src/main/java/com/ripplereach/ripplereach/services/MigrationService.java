package com.ripplereach.ripplereach.services;

import com.google.cloud.storage.BlobInfo;
import com.google.firebase.cloud.StorageClient;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MigrationService {

  @Value("${storage.bucket}")
  private String storageBucket;

  public void migrateImages(String localImageDirectory, String dir) {
    try {
      Files.walk(Paths.get(localImageDirectory))
          .filter(Files::isRegularFile)
          .forEach(
              filePath -> {
                try {
                  byte[] content = Files.readAllBytes(filePath);
                  String fileName = filePath.getFileName().toString();

                  BlobInfo blobInfo =
                      BlobInfo.newBuilder(storageBucket, fileName)
                          .setContentType("image/jpeg")
                          .build();
                  StorageClient.getInstance()
                      .bucket()
                      .create(dir + fileName, content, blobInfo.getContentType());

                  log.info("Migrated image: {}", fileName);
                } catch (IOException e) {
                  log.error("Error migrating image: {}", filePath, e);
                }
              });
    } catch (IOException e) {
      log.error("Error reading image directory", e);
    }
  }
}
