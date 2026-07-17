package com.teafactory.pureleaf.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;

// OOP Concept: Class Declaration (Encapsulation, Abstraction)
@Configuration
public class FirebaseConfig {

    // OOP Concept: Method Declaration (Encapsulation)
    @PostConstruct
    public void initFirebase() {
        try {
            // OOP Concept: Object Instantiation
            FileInputStream serviceAccount =
                    new FileInputStream("src/main/resources/firebase-service-account.json");

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
