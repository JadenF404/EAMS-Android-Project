package com.group25.db.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group25.db.model.*;
import com.group25.db.service.DatabaseService;

@RestController
public class DatabaseController {

    private DatabaseService dbService;

    @GetMapping("/test")
    public String testConnection() {
        return "Connected";
    }

    @PostMapping("/add_attendee")
    public String addAttendee() {
        Attendee test = new Attendee();
        test.setFirstName("Miller");
        test.setLastName("Ding");
        test.setPassword("123");
        test.setAddress("1 John St");
        test.setPhone("123-456-7890");
        test.setUserId("mding022@uottawa.ca");

        dbService.signUpUser(test);
        return "Success";
    }
}
