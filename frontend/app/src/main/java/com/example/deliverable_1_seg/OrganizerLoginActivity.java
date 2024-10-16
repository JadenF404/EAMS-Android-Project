package com.example.deliverable_1_seg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class OrganizerLoginActivity extends AppCompatActivity {
    private EditText loginUsername;
    private EditText loginPassword;

    private EditText signupFirstName;
    private EditText signupLastName;
    private EditText signupEmail;
    private EditText signupPassword;
    private EditText signupPhone;
    private EditText signupAddress;
    private EditText signupOrgName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_login_screen); // Link to attandee_login_form.xml

        // Initialize views
        loginUsername = findViewById(R.id.loginUsername);
        loginPassword = findViewById(R.id.loginPassword);

        // Find the Attendee button and set its click listener
        Button ForgotPassword_bttn = findViewById(R.id.ForgotPassword_bttn);

        ForgotPassword_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch AttendeeLoginActivity when button is clicked
                Intent intent = new Intent(OrganizerLoginActivity.this, OrganizerPasswordChange.class);
                startActivity(intent);
            }

        });
    }






    // Method to switch to the signup form
    public void switchToSignupOrganizer(View view) {
        setContentView(R.layout.organizer_signup_form); // Link to attendee_signup_form.xml

        signupFirstName = findViewById(R.id.signupFirstName);
        signupLastName = findViewById(R.id.signupLastName);
        signupEmail = findViewById(R.id.signupEmail);
        signupPassword = findViewById(R.id.signupPassword);
        signupPhone = findViewById(R.id.signupPhone);
        signupAddress = findViewById(R.id.signupAddress);
        signupOrgName = findViewById(R.id.signupOrgName);
    }
    public void loginOrganizer(View view){
        DatabaseReference organizerRef = FirebaseDatabase.getInstance().getReference("organizer");
        String userKey = loginUsername.getText().toString().replace(".", ",");

        if (loginUsername.getText().toString().isEmpty()
                || loginPassword.getText().toString().isEmpty()){
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        organizerRef.child(userKey).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    // Retrieve the data as a Map, Hasmap more preciesely
                    Map<String, Object> attendeeData = (Map<String, Object>) dataSnapshot.getValue();

                    // Check if the password in the database matches the input password
                    if (attendeeData != null && attendeeData.containsKey("password") && attendeeData.get("password").equals(loginPassword.getText().toString())) {
                        Toast.makeText(this, "Organizer login successful", Toast.LENGTH_SHORT).show();
                        // Proceed to attendee dashboard or next activity
                    } else {
                        Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "No organizer found with this email", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to retrieve organizer data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signupOrganizer(View view){

        if (signupFirstName.getText().toString().isEmpty()
                || signupLastName.getText().toString().isEmpty()
                || signupEmail.getText().toString().isEmpty()
                || signupPassword.getText().toString().isEmpty()
                || signupPhone.getText().toString().isEmpty()
                || signupAddress.getText().toString().isEmpty()
                || signupOrgName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference organizerRef = FirebaseDatabase.getInstance().getReference("organizer");
        String userKey = signupEmail.getText().toString().replace(".", ",");

        organizerRef.child(userKey).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                Toast.makeText(this, "User with this email already exists", Toast.LENGTH_SHORT).show();
            } else {
                Map<String, Object> organizerData = new HashMap<>();
                organizerData.put("firstName", signupFirstName.getText().toString());
                organizerData.put("lastName", signupLastName.getText().toString());
                organizerData.put("email", signupEmail.getText().toString());
                organizerData.put("password", signupPassword.getText().toString());
                organizerData.put("phoneNumber", signupPhone.getText().toString());
                organizerData.put("address", signupAddress.getText().toString());
                organizerData.put("orgName", signupOrgName.getText().toString());

                organizerRef.child(userKey).setValue(organizerData).addOnCompleteListener(databaseTask -> {
                    if (databaseTask.isSuccessful()) {
                        Toast.makeText(this, "Organizer signed up successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to sign up organizer", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }




    public void notAnOrganizer(View view){
        finish();
    }




    //Back button to go back to login page
    public void backOrganizerForm (View view) {
        finish();
    }
}
