package com.k1.myguide.Models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    private String id, email, password, role,created_at,updated_at;
}
