package com.ripplereach.ripplereach.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

@Configuration
public class FirebaseConfig {

    private static boolean isInstanceExists = false;
    @PostConstruct
    public void initialize() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("src/main/resources/ripplereach-firebase-adminsdk.json");

            FirebaseOptions options = FirebaseOptions.builder()
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
