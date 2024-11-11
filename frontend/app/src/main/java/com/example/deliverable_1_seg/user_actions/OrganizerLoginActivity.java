package com.example.deliverable_1_seg.user_actions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deliverable_1_seg.R;
import com.example.deliverable_1_seg.helpers.DataValidator;
import com.example.deliverable_1_seg.helpers.password.OrganizerPasswordChange;
import com.example.deliverable_1_seg.helpers.welcomepages.OrganizerPendingPage;
import com.example.deliverable_1_seg.helpers.welcomepages.OrganizerWelcomePage;
import com.example.deliverable_1_seg.FirebaseHelper;
import com.google.firebase.auth.FirebaseUser;

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

    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_login_screen); // Link to organizer_login_form.xml

        // Initialize views
        loginUsername = findViewById(R.id.loginUsername);
        loginPassword = findViewById(R.id.loginPassword);

        // Initialize FirebaseHelper
        firebaseHelper = new FirebaseHelper();

        // Find the Forgot Password button and set its click listener
        Button forgotPasswordButton = findViewById(R.id.ForgotPassword_bttn);
        forgotPasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerLoginActivity.this, OrganizerPasswordChange.class);
            startActivity(intent);
        });
    }

    // Method to switch to the signup form
    public void switchToSignupOrganizer(View view) {
        setContentView(R.layout.organizer_signup_form); // Link to organizer_signup_form.xml

        // Initialize signup views
        signupFirstName = findViewById(R.id.signupFirstName);
        signupLastName = findViewById(R.id.signupLastName);
        signupEmail = findViewById(R.id.signupEmail);
        signupPassword = findViewById(R.id.signupPassword);
        signupPhone = findViewById(R.id.signupPhone);
        signupAddress = findViewById(R.id.signupAddress);
        signupOrgName = findViewById(R.id.signupOrgName);
    }

    public void loginOrganizer(View view) {
        loginUsername = findViewById(R.id.loginUsername);
        loginPassword = findViewById(R.id.loginPassword);

        String email = loginUsername.getText().toString();
        String password = loginPassword.getText().toString();

        // Ensure all fields are filled
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase login using FirebaseHelper
        firebaseHelper.signIn(email, password, true, new FirebaseHelper.SignInCallback() {
            @Override
            public void onSuccess(FirebaseUser user, String status) {
                if (status.equals("approved")) {
                Intent intent = new Intent(OrganizerLoginActivity.this, OrganizerWelcomePage.class);
                    startActivity(intent);
                    finish();
                } else if(status.equals("rejected")){
                    Toast.makeText(OrganizerLoginActivity.this, "SIGNUP DENIED; CONTACT 613-911!", Toast.LENGTH_SHORT).show();
                }else if(status.equals("pending")){
                    Intent intent = new Intent(OrganizerLoginActivity.this, OrganizerPendingPage.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(OrganizerLoginActivity.this, "Login Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signupOrganizer(View view) {
        String email = signupEmail.getText().toString();
        String password = signupPassword.getText().toString();

        // Check if any fields are empty
        if (signupFirstName.getText().toString().isEmpty()
                || signupLastName.getText().toString().isEmpty()
                || email.isEmpty()
                || !DataValidator.signUpPassword(password)
                || signupPhone.getText().toString().isEmpty()
                || signupAddress.getText().toString().isEmpty()
                || signupOrgName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create organizer data map
        Map<String, Object> organizerData = new HashMap<>();
        organizerData.put("firstName", signupFirstName.getText().toString());
        organizerData.put("lastName", signupLastName.getText().toString());
        organizerData.put("email", email);
        organizerData.put("phoneNumber", signupPhone.getText().toString());
        organizerData.put("address", signupAddress.getText().toString());
        organizerData.put("orgName", signupOrgName.getText().toString());
        organizerData.put("status", "pending");
        organizerData.put("userType", "organizer");

        // Firebase sign up using FirebaseHelper
        firebaseHelper.signUp(email, password, organizerData, true, new FirebaseHelper.SignUpCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(OrganizerLoginActivity.this, "Organizer signed up successfully", Toast.LENGTH_SHORT).show();
                setContentView(R.layout.organizer_login_screen);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(OrganizerLoginActivity.this, "Failed to sign up organizer: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void notAnOrganizer(View view) {
        finish();
    }

    // Back button to go back to the login page
    public void backOrganizerForm(View view) {
        finish();
    }
}
