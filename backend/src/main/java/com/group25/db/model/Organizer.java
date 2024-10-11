package com.group25.db.model;

import lombok.Data;

@Data
public class Organizer {
    private String firstName;
    private String lastName;
    private String userId; //This is the user's email too
    private String password;
    private String phone;
    private String address;
    private String org;
}
