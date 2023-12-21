package com.k1.myguide.Serivices;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.k1.myguide.Models.Destination;


import jakarta.annotation.PostConstruct;

@Service
public class DestinationService {

    @Autowired

    private FirebaseConfig applicationConfig;
    private String collection = "Destinations";

    public DestinationService(FirebaseConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    public Destination getDestination(String DestinationId) throws ExecutionException, InterruptedException {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            DocumentReference documentReference = dbFirestore.collection(applicationConfig.getCollectionName())
                    .document(DestinationId);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot document = future.get();
            Destination Destination;
    
            if (document.exists()) {
                Destination = document.toObject(Destination.class);
                return Destination;
                // return null;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Destination saveDestination(Destination Destination) throws ExecutionException, InterruptedException {
        try {

            Firestore dbFirestore = FirestoreClient.getFirestore();
            CollectionReference Destinations = dbFirestore.collection("Destinations");
            
                UUID uuid = UUID.randomUUID();
                Destination.setId(uuid.toString());
                Destination.setCreated_at(Calendar.getInstance().getTime().toString());
                ApiFuture<DocumentReference> collectionsApiFuture = dbFirestore.collection("Destinations").add(Destination);
                //DocumentReference dr = collectionsApiFuture.get();
                return Destination;
            
            
            
            //return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteDestination(String DestinationId) throws ExecutionException, InterruptedException {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            dbFirestore.collection(applicationConfig.getCollectionName()).document(DestinationId).delete().get();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // public Destination updateDestination(Destination Destination) throws ExecutionException, InterruptedException {
    //     try {
    //         Firestore dbFirestore = FirestoreClient.getFirestore();
    //         ApiFuture<WriteResult> writeResult = dbFirestore.collection(applicationConfig.getCollectionName())
    //                 .document(Destination.getId()).update(Destination.getUpdateMap());
    //         return Destination;
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         throw e;
    //     }
    // }


//     @PostConstruct
//     public void initialize() {
//     try {
//         FileInputStream serviceAccount = new FileInputStream(applicationConfig.getAuthFileLocation());
//         FirebaseOptions options = new FirebaseOptions.Builder()
//                 .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                 .setDatabaseUrl(applicationConfig.getDbBaseUrl())
//                 .build();
//         FirebaseApp myApp = FirebaseApp.initializeApp(options);
//     } catch (Exception e) {
//         e.printStackTrace();
//     }
// }
}
