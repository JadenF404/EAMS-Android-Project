package com.example.deliverable_1_seg.user_actions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deliverable_1_seg.FirebaseHelper;
import com.example.deliverable_1_seg.R;
import com.example.deliverable_1_seg.helpers.welcomepages.AttendeeWelcomePage;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class AttendeeLoginActivity extends AppCompatActivity {
    private EditText loginUsername;
    private EditText loginPassword;

    private EditText signupFirstName;
    private EditText signupLastName;
    private EditText signupEmail;
    private EditText signupPassword;
    private EditText signupPhone;
    private EditText signupAddress;

    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_login_screen); // Link to attendee_login_form.xml
        // Initialize views
        loginUsername = findViewById(R.id.loginUsername);
        loginPassword = findViewById(R.id.loginPassword);
        firebaseHelper = new FirebaseHelper();
    }


    // Method to switch to the signup form
    public void switchToSignupAttendee(View view) {
        setContentView(R.layout.attendee_signup_form); // Link to attendee_signup_form.xml

        // Initialize signup views
        signupFirstName = findViewById(R.id.signupFirstName);
        signupLastName = findViewById(R.id.signupLastName);
        signupEmail = findViewById(R.id.signupEmail);
        signupPassword = findViewById(R.id.signupPassword);
        signupPhone = findViewById(R.id.signupPhone);
        signupAddress = findViewById(R.id.signupAddress);
    }

    public void notAnAttendee(View view) {
        finish();
    }

    public void loginAttendee(View view) {
        String email = loginUsername.getText().toString();
        String password = loginPassword.getText().toString();

        // Ensure all fields are filled
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase login
        firebaseHelper.signIn(email, password, new FirebaseHelper.SignInCallback() {
            @Override
            public void onSuccess(FirebaseUser user, String Status) {
                Intent intent = new Intent(AttendeeLoginActivity.this, AttendeeWelcomePage.class);


                startActivity(intent);
                finish(); // Optional: Call finish to remove the login activity from the back stack
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AttendeeLoginActivity.this, "Login Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signupAttendee(View view) {
        String email = signupEmail.getText().toString();
        String password = signupPassword.getText().toString();

        if (signupFirstName.getText().toString().isEmpty()
                || signupLastName.getText().toString().isEmpty()
                || email.isEmpty()
                || password.isEmpty()
                || signupPhone.getText().toString().isEmpty()
                || signupAddress.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create attendee data map
        Map<String, Object> attendeeData = new HashMap<>();
        attendeeData.put("firstName", signupFirstName.getText().toString());
        attendeeData.put("lastName", signupLastName.getText().toString());
        attendeeData.put("email", email);
        attendeeData.put("phoneNumber", signupPhone.getText().toString());
        attendeeData.put("address", signupAddress.getText().toString());
        attendeeData.put("status", "pending");
        attendeeData.put("userType", "organizer");


        // Firebase sign up
        firebaseHelper.signUp(email, password, attendeeData,false, new FirebaseHelper.SignUpCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(AttendeeLoginActivity.this, "Attendee signed up successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AttendeeLoginActivity.this, AttendeeWelcomePage.class);
                startActivity(intent);
                finish(); // Optional: Call finish to remove the signup activity from the back stack
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AttendeeLoginActivity.this, "Failed to sign up attendee: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Back button to go back to login page
    public void backAttendeeForm(View view) {
        finish();
    }
}
