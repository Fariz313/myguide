package com.k1.myguide.Controllers;

import org.springframework.http.ResponseEntity;

import com.google.cloud.firestore.WriteResult;
import com.k1.myguide.Models.Destination;
import com.k1.myguide.Models.User;
import com.k1.myguide.Serivices.DestinationService;
import com.k1.myguide.Serivices.UserService;
import com.k1.myguide.Utils.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.RedirectView;

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

    @GetMapping(value = "/OAuthGithub", produces = "application/json")
    public RedirectView getUserOAuth(@RequestParam("code") String oAuthGithub)
            throws ExecutionException, InterruptedException {
        Response response = new Response();
        response.setService(this.getClass().getName());
        response.setMessage("Berhasil Membuat Data");
        User user = userService.getUserOAuthGithub(oAuthGithub);
        response.setData(user);
        // if (!StringUtils.isEmpty(user)) {
        // return ResponseHandler.response(user, "Current User", true, HttpStatus.OK);
        // }
        return new RedirectView("http://localhost:3000/oauth/login?"+user.toQueryString());
    }


    @PostMapping(value = "/update/{id}", produces = "application/json")
    public ResponseEntity<Object> updateUser(@RequestBody User user, @PathVariable String id)
            throws ExecutionException, InterruptedException {

        Response response = new Response();
        response.setService(this.getClass().getName());
        WriteResult wr = userService.updateUser(id, user);
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

    @PostMapping(value = "/save", produces = "application/json")
    public ResponseEntity<Object> saveUser(@RequestBody User user)
            throws ExecutionException, InterruptedException {
        User saveUser = userService.saveUser(user);
        Response response = new Response();

        if (saveUser == null) {
            response.setMessage("Email telah terdaftar pada aplikasi!");

            return ResponseEntity
                    .status(400)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } else {
            response.setService(this.getClass().getName());
            response.setMessage("Berhasil Membuat Data");
            response.setData(saveUser);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<Object> loginUser(@RequestBody User user)
            throws ExecutionException, InterruptedException {
        User loginUser = userService.loginUser(user);
        int HS = 500;
        Response response = new Response();
        if (loginUser != null) {
            response.setService(this.getClass().getName());
            response.setMessage("Berhasil Login");
            HS = 200;
        } else {
            response.setService(this.getClass().getName());
            response.setMessage("Email atau Password Salah");

            HS = 400;
        }
        Map<String, Object> data = new HashMap<>();
        data.put("id", loginUser.getId());
        data.put("email", loginUser.getEmail());
        data.put("role", loginUser.getRole());
        data.put("_token", loginUser.get_token());
        data.put("Created At", loginUser.getCreated_at());
        data.put("Updated At", loginUser.getUpdated_at());
        response.setData(data);

        return ResponseEntity
                .status(HS)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping(value = "/me", produces = "application/json")
    public ResponseEntity<Object> me(@RequestHeader("Authorization") String authorizationHeader)
            throws ExecutionException, InterruptedException {
        User loginUser = userService.me(authorizationHeader);
        int HS = 500;
        Response response = new Response();
        if (loginUser != null) {
            response.setService(this.getClass().getName());
            response.setMessage("Berhasil Login");
            response.setData(loginUser);
            HS = 200;
        } else {
            response.setService(this.getClass().getName());
            response.setMessage("Data User Tidak Ditemukan");
            response.setData(loginUser);
            HS = 400;
        }

        return ResponseEntity
                .status(HS)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
