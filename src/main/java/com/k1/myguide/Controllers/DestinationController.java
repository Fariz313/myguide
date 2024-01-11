package com.k1.myguide.Controllers;

import org.springframework.http.ResponseEntity;

import com.google.cloud.firestore.WriteResult;
import com.k1.myguide.Models.Destination;
import com.k1.myguide.Serivices.DestinationService;
import com.k1.myguide.Utils.Response;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "destination")
@CrossOrigin("*")
public class DestinationController {
    private DestinationService DestinationService;

    public DestinationController(DestinationService DestinationService) {
        this.DestinationService = DestinationService;
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<Object> getDestination(@RequestParam(defaultValue = "1000") String limit)
            throws ExecutionException, InterruptedException {
        Response response = new Response(); 
        response.setService(DestinationService.getClass().getName());
        response.setMessage("Berhasil Mendapat Data");
        List<Destination> destinations = DestinationService.getDestinationAll(Integer.parseInt(limit));
        response.setData(destinations);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Object> getDestinationById(@PathVariable String id)
            throws ExecutionException, InterruptedException {
        try {
            Response response = new Response();
            response.setService(DestinationService.getClass().getName());
            Destination destination = DestinationService.getDestination(id);
            if (destination != null) {
                response.setMessage("Berhasil Mendapat Data");
                response.setData(destination);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            } else {
                response.setMessage("Data tidak ditemukan");
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            }
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error");
        }
    }

    @PostMapping(value = "/save", produces = "application/json")
    public ResponseEntity<Object> saveDestination(@RequestBody Destination Destination)
            throws ExecutionException, InterruptedException {
        Destination saveDestination = DestinationService.saveDestination(Destination);
        Response response = new Response();

        if (saveDestination == null) {
            response.setMessage("Destinasi gagal di daftarakan!");

            return ResponseEntity
                    .status(400)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } else {
            response.setService(DestinationService.getClass().getName());
            response.setMessage("Berhasil Membuat Destinasi");
            response.setData(saveDestination);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping(value = "/update/{id}", produces = "application/json")
    public ResponseEntity<Object> updateDestination(@RequestBody Destination Destination, @PathVariable String id)
            throws ExecutionException, InterruptedException {

        Response response = new Response();
        response.setService(DestinationService.getClass().getName());
        WriteResult wr = DestinationService.updateDestination(id, Destination);
        response.setMessage("Berhasil mengupdate Data");
        response.setData(wr);
        // if (!StringUtils.isEmpty(destination)) {
        // return ResponseHandler.response(destination, "Current destination", true,
        // HttpStatus.OK);
        // }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
    
    @DeleteMapping(value = "/delete/{id}", produces = "application/json")
    public ResponseEntity<Object> deleteDestination(@PathVariable String id)
            throws ExecutionException, InterruptedException {

        Response response = new Response();
        response.setService(DestinationService.getClass().getName());
        DestinationService.deleteDestination(id);
        response.setMessage("Berhasil menhapus Data");
        response.setData(null);
        // if (!StringUtils.isEmpty(destination)) {
        // return ResponseHandler.response(destination, "Current destination", true,
        // HttpStatus.OK);
        // }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
