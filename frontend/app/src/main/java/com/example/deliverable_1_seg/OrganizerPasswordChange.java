package com.example.deliverable_1_seg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OrganizerPasswordChange extends AppCompatActivity { // Correctly inherit from AppCompatActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_password_change); // Link to your layout file


        // Initialize views
        Button changePasswordButton = findViewById(R.id.Login_bttn);
        EditText newPassword = findViewById(R.id.NewPassword);
        EditText confirmPassword = findViewById(R.id.loginPassword);
        TextView passwordError = findViewById(R.id.passwordError);
        passwordError.setVisibility(View.GONE); // Initially hide the error message



        // Set the click listener for the Change Password button
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the password values entered by the user
                String newPass = newPassword.getText().toString();
                String confirmPass = confirmPassword.getText().toString();

                // Validate if the passwords match
                if (!newPass.equals(confirmPass)) {
                    // Show an AlertDialog popup for the mismatch
                    new AlertDialog.Builder(OrganizerPasswordChange.this) // Corrected to refer to the current class
                            .setTitle("Password Mismatch")
                            .setMessage("The passwords you entered do not match.  \nPlease try again! :)")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Clear the password fields
                                    newPassword.setText("");
                                    confirmPassword.setText("");
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                    // Optionally, you can also show the error message text if needed
                    passwordError.setVisibility(View.VISIBLE);
                } else {
                    // Hide the error message and proceed with password change logic
                    passwordError.setVisibility(View.GONE);

                    // TODO: Add your logic here to change the password (e.g., API call, database update, etc.)
                }
            }
        });
    }

    // Method for the back button to go back to the login page
    public void Cancel_org(View view) {
        finish(); // Ends the current activity and goes back to the previous one
    }
}
