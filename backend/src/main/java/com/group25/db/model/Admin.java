package com.group25.db.model;

import lombok.Data;

@Data
public class Admin {
    private String userId; //also email
    private String password;
}
