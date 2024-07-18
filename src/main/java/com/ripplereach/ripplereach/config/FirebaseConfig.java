package com.ripplereach.ripplereach.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfig {

  @Value("${firebase.admin.sdk.json.path}")
  private String firebaseAdminSdkPath;

  private static boolean isInstanceExists = false;

  @PostConstruct
  public void initialize() {
    try {
      FileInputStream serviceAccount = new FileInputStream(firebaseAdminSdkPath);

      FirebaseOptions options =
          FirebaseOptions.builder()
              .setCredentials(GoogleCredentials.fromStream(serviceAccount))
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
