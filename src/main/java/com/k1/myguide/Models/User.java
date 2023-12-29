package com.k1.myguide.Models;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.google.cloud.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    private String id, email, name, address, password, role, _token;
    private Timestamp created_at, updated_at;

    // public Map<String, Object> getUpdateMap() {
    //     Map<String, Object> updateMap = new HashMap<>();
    //     if (this.email != null) {
    //         updateMap.put("email", this.email);
    //     }
    //     if (this.password != null) {
    //         updateMap.put("password", this.password);
    //     }
    //     if (this.role != null) {
    //         updateMap.put("role", this.role);
    //     }

    //     updateMap.put("updated_at", Timestamp.now());

    //     return updateMap;
    // }
}
