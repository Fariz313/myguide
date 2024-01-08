package com.k1.myguide.Controllers;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.k1.myguide.Models.Transaction;
import com.k1.myguide.Models.User;
import com.k1.myguide.Serivices.TransactionService;
import com.k1.myguide.Serivices.UserService;
import com.k1.myguide.Serivices.TransactionService;
import com.k1.myguide.Utils.Response;

@RestController
@RequestMapping(value = "transaction")
@CrossOrigin("*")
public class TransactionController {
    private TransactionService TransactionService;

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<Object> getTransaction(@RequestHeader("Authorization") String authorizationHeader,@RequestParam(defaultValue = "1000") String limit)
            throws ExecutionException, InterruptedException {
        User loginUser = UserService.me(authorizationHeader);
        Response response = new Response();
        response.setService(TransactionService.getClass().getName());
        response.setMessage("Berhasil Mendapat Data");
        List<Transaction> destinations = TransactionService.getTransactionAll(loginUser,Integer.parseInt(limit));
        response.setData(destinations);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<Object> saveTransaction(@RequestHeader("Authorization") String authorizationHeader,@RequestParam(defaultValue = "1000") String limit)
            throws ExecutionException, InterruptedException {
        User loginUser = UserService.me(authorizationHeader);
        Response response = new Response();
        response.setService(TransactionService.getClass().getName());
        response.setMessage("Berhasil Mendapat Data");
        List<Transaction> destinations = TransactionService.getTransactionAll(loginUser,Integer.parseInt(limit));
        response.setData(destinations);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
