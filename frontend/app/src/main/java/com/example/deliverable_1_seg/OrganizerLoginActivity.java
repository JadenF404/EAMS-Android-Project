package com.example.deliverable_1_seg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

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

    private FirebaseAuth userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_login_screen); // Link to attandee_login_form.xml

        // Initialize views
        loginUsername = findViewById(R.id.loginUsername);
        loginPassword = findViewById(R.id.loginPassword);

        //Initialize firebase auth
        userAuth = FirebaseAuth.getInstance();

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

        String email = loginUsername.getText().toString();
        String password = loginPassword.getText().toString();

        //ensure all fields are filled
        if (loginUsername.getText().toString().isEmpty()
                || loginPassword.getText().toString().isEmpty()){
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //firebase auth login
        userAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task ->{
            if (task.isSuccessful()) {
                // Sign-in success
                FirebaseUser user = userAuth.getCurrentUser();
                Intent intent = new Intent(OrganizerLoginActivity.this, OrganizerWelcomePage.class);
                startActivity(intent);
            } else {
                // Sign-in failed
                Toast.makeText(this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Boolean emailChecker(String email) {
        // Check if the email contains an @ symbol
        if (email.contains("@")) {

            // Find the index of @ and check if there's a "," after it
            String afterAt = email.substring(email.indexOf("@") + 1);
            // Ensure there is a "," after the @
            if (afterAt.contains(",")) {
                return true; // Email is valid
            }
        }
        return false; //Email was not valid
    }

        public boolean isValidAddress(String address) {
            // Define the regex pattern for "number Word Word"
            String regex = "^\\d+\\s+[a-zA-Z0-9]+\\s+[a-zA-Z0-9]+$";
            // Compile the pattern
            Pattern pattern = Pattern.compile(regex);
            // Match the address against the pattern
            Matcher matcher = pattern.matcher(address);
            // Return true if it matches, false otherwise
            return matcher.matches();
        }

    public void signupOrganizer(View view){
        String email = signupEmail.getText().toString();
        String password = signupPassword.getText().toString();

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

        userAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    //create user
                    FirebaseUser user = userAuth.getCurrentUser();

                    //Save the additional attendee data
                    String userKey = user.getUid();
                    DatabaseReference organizerRef = FirebaseDatabase.getInstance().getReference("organizer/organizer_requests");

                    Map<String, Object> organizerData = new HashMap<>();
                    organizerData.put("firstName", signupFirstName.getText().toString());
                    organizerData.put("lastName", signupLastName.getText().toString());
                    organizerData.put("email", email);
                    organizerData.put("phoneNumber", signupPhone.getText().toString());
                    organizerData.put("address", signupAddress.getText().toString());
                    organizerData.put("address", signupAddress.getText().toString());
                    organizerData.put("orgName", signupOrgName.getText().toString());
                    organizerData.put("status", "pending");


                    organizerRef.child(userKey).setValue(organizerData).addOnCompleteListener(databaseTask -> {
                        if (databaseTask.isSuccessful()) {
                            Toast.makeText(this, "Organizer signed up successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(OrganizerLoginActivity.this, OrganizerWelcomePage.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "Failed to save user details", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    // Sign-up failed
                    Toast.makeText(this, "Failed to sign up organizer: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
