package com.ripplereach.ripplereach.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class FirebaseConfig {

  private static boolean isInstanceExists = false;

  @Autowired ResourceLoader resourceLoader;

  @Value("${storage.bucket}")
  private String storageBucket;

  @PostConstruct
  public void initialize() {
    try {
      Resource resource =
          resourceLoader.getResource("classpath:ripplereach-firebase-adminsdk.json");
      InputStream serviceAccountStream = resource.getInputStream();

      FirebaseOptions options =
          FirebaseOptions.builder()
              .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
              .setStorageBucket(storageBucket)
              .build();

      List<FirebaseApp> firebaseApps = FirebaseApp.getApps();

      for (FirebaseApp firebaseApp : firebaseApps) {
        if (firebaseApp.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
          isInstanceExists = true;
        }
      }

      if (!isInstanceExists) {
        FirebaseApp.initializeApp(options);
        FirebaseMessaging.getInstance(FirebaseApp.getInstance());
      }
    } catch (IOException e) {
      e.printStackTrace(new PrintStream(System.out));
    }
  }
}
