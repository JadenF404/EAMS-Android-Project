package com.group25.db.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group25.db.model.*;

@RestController
public class DatabaseController {
    @GetMapping("/test")
    public String testConnection() {
        return "Connected";
    }
}
