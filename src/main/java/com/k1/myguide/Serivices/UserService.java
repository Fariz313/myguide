package com.k1.myguide.Serivices;

import java.io.FileInputStream;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.k1.myguide.Config.FirebaseConfig;
import com.k1.myguide.Models.User;

import jakarta.annotation.PostConstruct;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private FirebaseConfig applicationConfig;
    private String collection = "users";

    public UserService(FirebaseConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    public User getUser(String userId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(applicationConfig.getCollectionName())
                .document(userId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        User user;

        if (document.exists()) {
            user = document.toObject(User.class);
            return user;
        } else {
            return null;
        }
    }

    public User saveUser(User user) throws ExecutionException, InterruptedException {
        String UserId = UUID.randomUUID().toString();
        user.setId(UserId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(collection)
                .document(user.getId()).set(user);
        return user;

    }

    public User loginUser(User user) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference users = dbFirestore.collection("users");
        Query query = users.whereEqualTo("email", user.getEmail());
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        boolean found = false;
        User userFound = null;
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            userFound = document.toObject(User.class);
            found = true;
        }
        if(found){
            if(passwordEncoder.matches(user.getPassword(), userFound.getPassword())){
                return userFound;
            }
            return null;
        }else{
            return null;
        }
    }

    @PostConstruct
    public void initialize() {
        try {
            FileInputStream serviceAccount = new FileInputStream(applicationConfig.getAuthFileLocation());
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(applicationConfig.getDbBaseUrl())
                    .build();
            FirebaseApp.initializeApp(options);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
