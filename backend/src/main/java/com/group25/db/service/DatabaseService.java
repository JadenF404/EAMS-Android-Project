package com.group25.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group25.db.model.Attendee;

@Service
public class DatabaseService {
    @Autowired
    private AttendeeMapper attendeeMapper;

    public boolean signUpUser(Attendee attendee) {
        try {
            attendeeMapper.insertUser(attendee);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
