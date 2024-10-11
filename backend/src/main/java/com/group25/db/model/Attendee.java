package com.group25.db.model;

import lombok.Data;

@Data
public class Attendee {
    private String firstName;
    private String lastName;
    private Long userId; //This is the user's email too
    private String password;
    private String phone;
    private String address;
}
