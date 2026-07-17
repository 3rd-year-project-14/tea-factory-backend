package com.teafactory.pureleaf.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

// OOP Concept: Class Declaration (Encapsulation, Abstraction)
@Configuration
public class FirebaseConfig {

    @Value("${FIREBASE_CREDENTIALS:}")
    private String firebaseCredentials;

    // OOP Concept: Method Declaration (Encapsulation)
    @PostConstruct
    public void initFirebase() {
        try {
            InputStream serviceAccount;
            if (firebaseCredentials != null && !firebaseCredentials.isBlank()) {
                String trimmed = firebaseCredentials.trim();
                byte[] jsonBytes;
                if (trimmed.startsWith("{")) {
                    // FIREBASE_CREDENTIALS holds the raw service account JSON
                    jsonBytes = trimmed.getBytes(StandardCharsets.UTF_8);
                } else {
                    // FIREBASE_CREDENTIALS holds the service account JSON, base64-encoded
                    jsonBytes = Base64.getDecoder().decode(trimmed);
                }
                serviceAccount = new ByteArrayInputStream(jsonBytes);
            } else {
                // Local dev fallback: raw JSON file on disk (not present in deployed containers)
                serviceAccount = new FileInputStream("src/main/resources/firebase-service-account.json");
            }

            // OOP Concept: Builder Pattern (Abstraction, Encapsulation)
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("tea-factory-project-902e0.firebasestorage.app")
                    .build();

            // OOP Concept: Static Method & Singleton Pattern
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase Initialized");
            }
        } catch (Exception e) {
            System.err.println("❌ Firebase initialization failed");
            e.printStackTrace();
        }
    }
}
