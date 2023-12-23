package com.k1.myguide.Serivices;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.k1.myguide.Config.FirebaseConfig;
import com.k1.myguide.Models.Destination;
import com.k1.myguide.Models.User;

import jakarta.annotation.PostConstruct;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private FirebaseConfig applicationConfig;
    private String collection = "users";
    private Algorithm algorithm = Algorithm.HMAC256("rahasia");

    public UserService(FirebaseConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    public User getUser(String userId) throws ExecutionException, InterruptedException {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            DocumentReference documentReference = dbFirestore.collection(applicationConfig.getCollectionName())
                    .document(userId);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot document = future.get();
            User user;

            if (document.exists()) {
                user = document.toObject(User.class);
                return user;
                // return null;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public User saveUser(User user) throws ExecutionException, InterruptedException {
        try {

            Firestore dbFirestore = FirestoreClient.getFirestore();
            CollectionReference users = dbFirestore.collection("users");
            Query query = users.whereEqualTo("email", user.getEmail());
            boolean found = false;
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            User userFound = null;
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                userFound = document.toObject(User.class);
                found = true;
            }
            if (!found) {
                UUID uuid = UUID.randomUUID();
                user.setId(uuid.toString());
                user.setCreated_at(Timestamp.now());
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                DocumentReference documentReference = dbFirestore.collection(collection).document(uuid.toString());
                documentReference.set(user);
                // DocumentReference dr = collectionsApiFuture.get();
                return user;

            }
            return null;

            // return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteUser(String userId) throws ExecutionException, InterruptedException {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            dbFirestore.collection(applicationConfig.getCollectionName()).document(userId).delete().get();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public WriteResult updateUser(String id, User user)
            throws ExecutionException, InterruptedException {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();

            Map<String, Object> updates = new HashMap<>();
            if (user.getEmail() != null) {
                updates.put("email", user.getEmail());
            }
            if (user.getPassword() != null) {
                updates.put("password", passwordEncoder.encode(user.getPassword())); // Replace with the new longitude
            }
            if (user.getRole() != null) {
                updates.put("role", user.getRole()); // Replace with the new latitude
            }
            updates.put("updated_at", Timestamp.now());
            DocumentReference documentReference = dbFirestore.collection(collection).document(id);

            // Update the document with the specified fields
            ApiFuture<WriteResult> updateResult = documentReference.set(updates, SetOptions.merge());

            // Wait for the update operation to complete
            WriteResult writeResult = updateResult.get();
            return writeResult;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
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
            if (userFound != null) {
                found = true;
                break;
            }
        }
        if (found) {
            if (passwordEncoder.matches(user.getPassword(), userFound.getPassword())) {
                String jwtToken = JWT.create()
                        .withIssuer("rahasia")
                        .withSubject("Rahasia Details")
                        .withClaim("userId", userFound.getId())
                        .withIssuedAt(new Date())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000L))
                        .withJWTId(UUID.randomUUID()
                                .toString())
                        .withNotBefore(new Date(System.currentTimeMillis() + 1000L))
                        .sign(algorithm);
                userFound.set_token(jwtToken);
                return userFound;
            }
            return null;
        } else {
            return null;
        }
    }

    public User me(String authorizationToken) throws ExecutionException, InterruptedException {
        try {
            if (authorizationToken != null && authorizationToken.startsWith("Bearer ")) {
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("rahasia")
                        .build();
                String bearerToken = authorizationToken.substring(7);
                DecodedJWT decodedJWT = verifier.verify(bearerToken);
                Claim claim = decodedJWT.getClaim("userId");
                String userId = claim.asString();
                Firestore dbFirestore = FirestoreClient.getFirestore();
                DocumentReference documentReference = dbFirestore.collection(collection)
                        .document(userId);
                ApiFuture<DocumentSnapshot> future = documentReference.get();
                DocumentSnapshot document = future.get();
                User user;

                if (document.exists()) {
                    user = document.toObject(User.class);
                    return user;
                    // return null;
                } else {
                    return null;
                }

            } else {
                return null;

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
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
            FirebaseApp myApp = FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/**
 * UserToken
 */
class UserToken {
    User user;
    String token;
}
