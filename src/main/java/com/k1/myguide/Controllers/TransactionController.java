package com.k1.myguide.Controllers;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.k1.myguide.Models.Transaction;
import com.k1.myguide.Models.User;
import com.k1.myguide.Serivices.TransactionService;
import com.k1.myguide.Serivices.UserService;
import com.k1.myguide.Serivices.TransactionService;
import com.k1.myguide.Utils.Response;
import com.google.cloud.Timestamp;
@RestController
@RequestMapping(value = "transaction")
@CrossOrigin("*")
public class TransactionController {
    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<Object> getTransaction(@RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(defaultValue = "1000") String limit)
            throws ExecutionException, InterruptedException {
        User loginUser = UserService.me(authorizationHeader);
        Response response = new Response();
        response.setService(transactionService.getClass().getName());
        response.setMessage("Berhasil Mendapat Data");
        List<Transaction> destinations = transactionService.getTransactionAll(loginUser, Integer.parseInt(limit));
        response.setData(destinations);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<Object> saveTransaction(@RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(defaultValue = "1000") String limit, @RequestBody Transaction trx)
            throws ExecutionException, InterruptedException {
        User loginUser = UserService.me(authorizationHeader);
        Response response = new Response();
        response.setService(transactionService.getClass().getName());
        response.setMessage("Berhasil Mendapat Data");
        Transaction transaction = transactionService.saveTransaction(loginUser, trx);
        response.setData(transaction);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/save")
    public Transaction saveTransaction(@RequestBody Transaction transaction) throws ExecutionException, InterruptedException {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            CollectionReference transactions = dbFirestore.collection("Transactions");

            UUID uuid = UUID.randomUUID();
            transaction.setId(uuid.toString());
            transaction.setCreated_at(Timestamp.now());

            DocumentReference documentReference = transactions.document(uuid.toString());
            ApiFuture<WriteResult> writeResultApiFuture = documentReference.set(transaction);

            // Tambahkan log atau penanganan lain jika diperlukan
            System.out.println("Transaction saved successfully");

            return transaction;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
