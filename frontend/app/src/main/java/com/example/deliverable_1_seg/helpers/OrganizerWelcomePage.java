package com.example.deliverable_1_seg.helpers;

import android.content.Intent;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.deliverable_1_seg.MainActivity;
import com.example.deliverable_1_seg.R;


public class OrganizerWelcomePage extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_welcome_page);


        //log out button
        Button organizerButton = findViewById(R.id.OrganizerLogOut_button);
        organizerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerWelcomePage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void logOut(View view){
        finish();
    }

}
