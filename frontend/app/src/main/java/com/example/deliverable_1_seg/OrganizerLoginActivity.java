package com.example.deliverable_1_seg;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class OrganizerLoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_login_form); // Link to attandee_login_form.xml
    }

    //Back button to go back to login page
    public void backOrganizerForm (View view) {
        finish();
    }
}
