package com.k1.myguide.Models;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.google.cloud.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    private String id, email, name, address, password, role, _token, pathFoto,idGithub,idGoogle;
    private Timestamp created_at, updated_at;
    private boolean justCreated;
    // public Map<String, Object> getUpdateMap() {
    // Map<String, Object> updateMap = new HashMap<>();
    // if (this.email != null) {
    // updateMap.put("email", this.email);
    // }
    // if (this.password != null) {
    // updateMap.put("password", this.password);
    // }
    // if (this.role != null) {
    // updateMap.put("role", this.role);
    // }

    // updateMap.put("updated_at", Timestamp.now());

    // return updateMap;
    // }
    public String toQueryString() {
        StringBuilder queryString = new StringBuilder();

        appendQueryParam(queryString, "id", id);
        appendQueryParam(queryString, "email", email);
        appendQueryParam(queryString, "name", name);
        appendQueryParam(queryString, "address", address);
        appendQueryParam(queryString, "password", password);
        appendQueryParam(queryString, "role", role);
        appendQueryParam(queryString, "_token", _token);
        appendQueryParam(queryString, "pathFoto", pathFoto);
        appendQueryParam(queryString, "idGithub", idGithub);

        return queryString.toString();
    }

    private void appendQueryParam(StringBuilder queryString, String key, String value) {
        if (value != null && !value.isEmpty()) {
            if (queryString.length() > 0) {
                queryString.append("&");
            }
            try {
                queryString.append(key)
                        .append("=")
                        .append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace(); // Handle encoding exception
            }
        }
    }

    private void appendQueryParam(StringBuilder queryString, String key, Timestamp value) {
        if (value != null) {
            appendQueryParam(queryString, key, formatTimestamp(value));
        }
    }

    private String formatTimestamp(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(timestamp);
    }
}
