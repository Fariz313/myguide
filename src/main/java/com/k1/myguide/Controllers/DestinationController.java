package com.k1.myguide.Controllers;

import org.springframework.http.ResponseEntity;
import com.k1.myguide.Models.Destination;
import com.k1.myguide.Serivices.DestinationService;
import com.k1.myguide.Utils.Response;

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
    public ResponseEntity<Object> getDestination(@RequestParam("empId") String empId)
            throws ExecutionException, InterruptedException {
        Response response = new Response();
        response.setService(this.getClass().getName());
        response.setMessage("Berhasil Membuat Data");
        Destination Destination = DestinationService.getDestination("empId");
        response.setData(Destination);
        // if (!StringUtils.isEmpty(Destination)) {
        // return ResponseHandler.response(Destination, "Current Destination", true, HttpStatus.OK);
        // }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping(value = "/save", produces = "application/json")
    public ResponseEntity<Object> saveDestination(@RequestBody Destination Destination)
            throws ExecutionException, InterruptedException {
        Destination saveDestination = DestinationService.saveDestination(Destination);
        Response response = new Response();
        
        if(saveDestination == null) {
            response.setMessage("Destinasi gagal di daftarakan!");
            
            return ResponseEntity 
                    .status(400)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } else {
            response.setService(this.getClass().getName());
            response.setMessage("Berhasil Membuat Destinasi");
            response.setData(saveDestination);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
