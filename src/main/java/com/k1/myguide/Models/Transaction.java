package com.k1.myguide.Models;

import java.util.Date;

import com.google.cloud.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Transaction {
    private String id, user_id, guide_id, destination_id,user_name,Destination_name;
    private int price_rate;    
    private int rating;
    private Date start_date, end_date;
    private Timestamp created_at, updated_at;
}
