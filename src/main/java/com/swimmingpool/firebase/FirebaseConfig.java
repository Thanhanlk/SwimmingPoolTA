package com.swimmingpool.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@ConditionalOnClass(FirebaseProperties.class)
@RequiredArgsConstructor
public class FirebaseConfig {

    private final FirebaseProperties properties;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        InputStream inputStream = FirebaseConfig.class.getResourceAsStream("/" + properties.getLocation());
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .build();
        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public Bucket bucket(FirebaseApp app) {
        return StorageClient
                .getInstance(app)
                .bucket(this.properties.getBucket());
    }
}