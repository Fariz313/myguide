package com.k1.myguide;

import java.io.FileInputStream;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.k1.myguide.Config.FirebaseConfig;

@SpringBootApplication
public class MyGuideApplication {
    private static FirebaseConfig applicationConfig;

	public static void main(String[] args) {
		try {
            FileInputStream serviceAccount = new FileInputStream(applicationConfig.getAuthFileLocation());
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(applicationConfig.getDbBaseUrl())
                    .build();
            FirebaseApp myApp = FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
		SpringApplication.run(MyGuideApplication.class, args);

	}

}
