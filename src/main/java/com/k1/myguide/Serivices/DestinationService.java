package com.k1.myguide.Serivices;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;
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
            DocumentReference documentReference = dbFirestore.collection(collection)
                    .document(DestinationId);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot document = future.get();
            Destination Destination;
            if (document.exists()) {
                Destination = document.toObject(Destination.class);
                return Destination;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<Destination> getDestinationAll(int limit) throws ExecutionException, InterruptedException {
        try {
            List<Destination> destinations = new ArrayList<Destination>();
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<QuerySnapshot> future = dbFirestore.collection(collection).limit(10).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                destinations.add(document.toObject(Destination.class));
            }
            return destinations;
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
            Destination.setName(Destination.getName());
            Destination.setAlamat(Destination.getAlamat());
            Destination.setPathFoto(Destination.getPathFoto());
            Destination.setRangeHarga(Destination.getRangeHarga());
            Destination.setWriter_id(Destination.getWriter_id());
            Destination.setId(uuid.toString());
            Destination.setCreated_at(Timestamp.now());
            DocumentReference documentReference = dbFirestore.collection(collection).document(uuid.toString());
            ApiFuture<WriteResult> writeResultApiFuture = documentReference.set(Destination);
            return Destination;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteDestination(String destinationId) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        try {
            ApiFuture<WriteResult> deleteFuture = dbFirestore.collection(collection).document(destinationId).delete();
            deleteFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete destination with ID: " + destinationId, e);
        }
    }

    public WriteResult updateDestination(String id, Destination Destination)
            throws ExecutionException, InterruptedException {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();

            Map<String, Object> updates = new HashMap<>();
            updates.put("name", Destination.getName());
            updates.put("longitude", Destination.getLongitude());
            updates.put("latitude", Destination.getLatitude());
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

    // @PostConstruct
    // public void initialize() {
    // try {
    // FileInputStream serviceAccount = new
    // FileInputStream(applicationConfig.getAuthFileLocation());
    // FirebaseOptions options = new FirebaseOptions.Builder()
    // .setCredentials(GoogleCredentials.fromStream(serviceAccount))
    // .setDatabaseUrl(applicationConfig.getDbBaseUrl())
    // .build();
    // FirebaseApp myApp = FirebaseApp.initializeApp(options);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
}
