package com.k1.myguide.Models;

import com.google.cloud.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Destination {
    private String id, name, writer_id, rate, rangeHarga, alamat, pathFoto,description;
    private Timestamp created_at, updated_at;
    private double longitude, latitude,distance;
}
