package com.k1.myguide.Models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Destination {
    private String id,name,writer_id,created_at,updated_at;
    private double longitude,latitude;
}
