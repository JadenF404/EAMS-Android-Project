package com.example.deliverable_1_seg;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AttendeeLoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_login_form); // Link to attandee_login_form.xml
    }

    //Back button to go back to login page
    public void backAttendeeForm (View view) {
        finish();
    }
}
