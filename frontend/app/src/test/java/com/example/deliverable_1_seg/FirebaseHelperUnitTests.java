package com.example.deliverable_1_seg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.deliverable_1_seg.helpers.db.Event;
import com.example.deliverable_1_seg.helpers.db.RegistrationRequest;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class FirebaseHelperUnitTests {

    private FirebaseHelper firebaseHelper;

    @Before
    public void setup() {
        firebaseHelper = new FirebaseHelper();
    }

    @Test
    public void testFirebaseHelperInitialization() {
        assertNotNull("FirebaseHelper should not be null", firebaseHelper);
    }

    @Test
    public void testCreateRegistrationRequest() {
        RegistrationRequest request = new RegistrationRequest();
        request.setUserId("12456");
        request.setEmail("test@uottawa.ca");
        request.setStatus("pending");

        assertEquals("User ID should match", "12456", request.getUserId());
        assertEquals("Email should match", "test@uottawa.ca", request.getEmail());
        assertEquals("Status should be pending", "pending", request.getStatus());
    }

    @Test
    public void testRegistrationRequestStatus() {
        RegistrationRequest request = new RegistrationRequest();

        request.setStatus("pending");
        assertEquals("Status should be pending", "pending", request.getStatus());

        request.setStatus("approved");
        assertEquals("Status should be approved", "approved", request.getStatus());

        request.setStatus("rejected");
        assertEquals("Status should be rejected", "rejected", request.getStatus());
    }
}