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
import com.k1.myguide.Models.Transaction;
import com.k1.myguide.Models.User;

import jakarta.annotation.PostConstruct;

@Service
public class TransactionService {

    @Autowired

    private FirebaseConfig applicationConfig;
    private String collection = "Transactions";

    public TransactionService(FirebaseConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    public Transaction getTransaction(String TransactionId) throws ExecutionException, InterruptedException {
        try {

            Firestore dbFirestore = FirestoreClient.getFirestore();
            DocumentReference documentReference = dbFirestore.collection(collection)
                    .document(TransactionId);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot document = future.get();
            Transaction Transaction;
            if (document.exists()) {
                Transaction = document.toObject(Transaction.class);
                return Transaction;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<Transaction> getTransactionAll(User user, int limit) throws ExecutionException, InterruptedException {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            CollectionReference transactions = dbFirestore.collection("transactions");
            Query query = transactions;
            if (user.getRole().equalsIgnoreCase("tourguide")) {
                query = query.whereEqualTo("guide_id", user.getId()).limit(limit);
            } else if (!user.getRole().equalsIgnoreCase("admin")) {
                query = query.whereEqualTo("guide_id", user.getId()).limit(limit);

            }
            ApiFuture<QuerySnapshot> querySnapshot = query.get();

            List<Transaction> transactionList = new ArrayList<>();

            return transactionList;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Transaction saveTransaction(User user, Transaction trx) throws ExecutionException, InterruptedException {
        try {

            Firestore dbFirestore = FirestoreClient.getFirestore();
            CollectionReference Transactions = dbFirestore.collection("Transactions");

            UUID uuid = UUID.randomUUID();
            trx.setId(uuid.toString());
            trx.setCreated_at(Timestamp.now());
            trx.setUser_id(user.getId());
            trx.setUser_name(user.getName());
            System.out.println("DONE 0");
            trx.calculateTotalPrice();
            System.out.println("DONE 1");
            DocumentReference documentReference = dbFirestore.collection(collection).document(uuid.toString());
            String guideId = trx.getGuide_id();
            System.out.println(guideId);
            CollectionReference guides = dbFirestore.collection("users");
            DocumentReference guideRef = guides.document(guideId);
            ApiFuture<DocumentSnapshot> guideSnapshot = guideRef.get();
            DocumentSnapshot guideDocument = guideSnapshot.get();
            if (guideDocument.exists()) {
                System.out.println("DONE 2");
                User guide = guideDocument.toObject(User.class);                
                
                
                String destinationId = guide.getGuideLocation();
                CollectionReference destinations = dbFirestore.collection("Destinations");
                DocumentReference destinationRef = destinations.document(destinationId);
                ApiFuture<DocumentSnapshot> destinationSnapshot = destinationRef.get();
                DocumentSnapshot destinationDocument = destinationSnapshot.get();
                System.out.println("DONE 3");
                if (destinationDocument.exists()) {
                    System.out.println("DONE 4");
                    Destination destination = destinationDocument.toObject(Destination.class);
                    trx.setDestination_id(destinationId);
                    trx.setGuide_name(guide.getName());
                    trx.setDestination_name(destination.getName());
                    ApiFuture<WriteResult> writeResultApiFuture = documentReference.set(trx);
                    System.out.println("DONE 5");
                    return trx;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteTransaction(String transactionId) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        try {
            ApiFuture<WriteResult> deleteFuture = dbFirestore.collection(collection).document(transactionId).delete();
            deleteFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete transaction with ID: " + transactionId, e);
        }
    }

    public WriteResult updateTransaction(String id, Transaction Transaction)
            throws ExecutionException, InterruptedException {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();

            Map<String, Object> updates = new HashMap<>();
            // updates.put("name", Transaction.getName());
            // updates.put("longitude", Transaction.getLongitude());
            // updates.put("latitude", Transaction.getLatitude());
            // updates.put("updated_at", Timestamp.now());
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
