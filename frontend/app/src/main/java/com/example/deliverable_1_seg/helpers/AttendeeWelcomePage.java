package com.example.deliverable_1_seg.helpers;

import android.content.Intent;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.deliverable_1_seg.MainActivity;
import com.example.deliverable_1_seg.R;


public class AttendeeWelcomePage extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_welcome_page);


        //log out button
        Button attendeeButton = findViewById(R.id.AttendeeLogOut_button);
        attendeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendeeWelcomePage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void logOut(View view){
        finish();
    }

}
