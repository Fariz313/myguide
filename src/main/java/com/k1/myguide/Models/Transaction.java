package com.k1.myguide.Models;

import java.util.Date;

import com.google.cloud.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Transaction {
    private String id, user_id, guide_id, destination_id,guide_name, user_name, Destination_name;
    private int price_rate, total_price;
    private int rating;
    private Date start_date, end_date;
    private Timestamp created_at, updated_at;

    public void calculateTotalPrice() {
        if (start_date != null && end_date != null) {
            long startTime = start_date.getTime();
            long endTime = end_date.getTime();
            long timeDifference = endTime - startTime;
            int numberOfDays = (int) (timeDifference / (1000 * 60 * 60 * 24));
            total_price = price_rate * numberOfDays;
        } else {
            total_price = 0;
        }
    }
}
