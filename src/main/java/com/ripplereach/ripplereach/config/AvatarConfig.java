package com.ripplereach.ripplereach.config;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@DependsOn("firebaseConfig")
public class AvatarConfig {

  // The avatarList is initialized as a synchronized list to ensure thread-safety when accessing or
  // modifying the list.
  @Getter
  private static final List<String> avatarList = Collections.synchronizedList(new ArrayList<>());

  @Value("${storage.bucket}")
  private String storageBucket;

  private final Storage storage;

  public AvatarConfig() {
    this.storage = StorageClient.getInstance().bucket().getStorage();
  }

  @PostConstruct
  @Async
  // Async Initialization: The @Async annotation on the loadAvatarList method allows the avatars to
  // be loaded asynchronously after the application starts, preventing startup delays.
  public void loadAvatarList() {
    try {
      Bucket bucket = storage.get(storageBucket);
      for (Blob blob : bucket.list().iterateAll()) {
        if (blob.getName().startsWith("avatars/")) {
          avatarList.add(blob.getName());
        }
      }
    } catch (RuntimeException e) {
      throw new RuntimeException("Failed to load avatar list", e);
    }
  }

  @Bean
  public List<String> avatarList() {
    return avatarList;
  }
}
