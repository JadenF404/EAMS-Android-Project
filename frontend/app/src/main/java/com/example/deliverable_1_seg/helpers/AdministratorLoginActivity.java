package com.example.deliverable_1_seg.helpers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deliverable_1_seg.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class AdministratorLoginActivity extends AppCompatActivity {

    private EditText loginUsername;
    private EditText loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrator_login_screen); // Link to administrator_login_screen.xml

        // Initialize views
        loginUsername = findViewById(R.id.loginUsername);
        loginPassword = findViewById(R.id.loginPassword);

    }

    public void notAnAdmin(View view){

        finish();
    }


    public void loginAdmin(View view){
        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference("administrator");
        String userKey = loginUsername.getText().toString().replace(".", ",");

        if (loginUsername.getText().toString().isEmpty()
                || loginPassword.getText().toString().isEmpty()){
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        adminRef.child(userKey).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    // Retrieve the data as a Map, Hasmap more preciesely
                    Map<String, Object> adminData = (Map<String, Object>) dataSnapshot.getValue();

                    // Check if the password in the database matches the input password
                    if (adminData != null && adminData.containsKey("password") && adminData.get("password").equals(loginPassword.getText().toString())) {
                        Toast.makeText(this, "Admin login successful", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdministratorLoginActivity.this, AdministratorWelcomePage.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "No admin found with this information", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to retrieve admin data", Toast.LENGTH_SHORT).show();
            }
        });
    }



    //Back button to go back to login page
    public void backAttendeeForm (View view) {
        finish();
    }
}
