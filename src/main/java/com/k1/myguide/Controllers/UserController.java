package com.k1.myguide.Controllers;

import org.springframework.http.ResponseEntity;
import com.k1.myguide.Models.User;
import com.k1.myguide.Serivices.UserService;
import com.k1.myguide.Utils.Response;

import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "user")
@CrossOrigin("*")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<Object> getUser(@RequestParam("empId") String empId)
            throws ExecutionException, InterruptedException {
        Response response = new Response();
        response.setService(this.getClass().getName());
        response.setMessage("Berhasil Membuat Data");
        User user = userService.getUser("empId");
        response.setData(user);
        // if (!StringUtils.isEmpty(user)) {
        // return ResponseHandler.response(user, "Current User", true, HttpStatus.OK);
        // }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping(value = "/ ", produces = "application/json")
    public ResponseEntity<Object> saveUser(@RequestBody User user)
            throws ExecutionException, InterruptedException {
        User saveUser = userService.saveUser(user);

        Response response = new Response();
        response.setService(this.getClass().getName());
        response.setMessage("Berhasil Membuat Data");
        response.setData(saveUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
