package com.example.deliverable_1_seg;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.example.deliverable_1_seg.helpers.db.Event;
import com.example.deliverable_1_seg.helpers.db.RegistrationRequest;

public class FirebaseEventHelperUnitTests {

    private FirebaseEventHelper firebaseHelper;

    @Before
    public void setUp() {
        firebaseHelper = new FirebaseEventHelper();
    }

    @Test
    public void testRegistrationRequest() {
        RegistrationRequest request = new RegistrationRequest(
                "12345",
                "John",
                "Doe",
                "firstlast@uottawa.ca",
                "123-456-7890",
                "75 Laurier Ave"
        );

        assertEquals("12345", request.getUserId());
        assertEquals("John", request.getFirstName());
        assertEquals("Doe", request.getLastName());
        assertEquals("firstlast@uottawa.ca", request.getEmail());
        assertEquals("123-456-7890", request.getPhoneNumber());
        assertEquals("75 Laurier Ave", request.getAddress());
    }

    @Test
    public void testEventId() {
        Event testEvent = new Event();
        String eventId = "1234567";
        testEvent.setEventId(eventId);

        assertEquals(eventId, testEvent.getEventId());
    }
}