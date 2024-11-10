package com.example.deliverable_1_seg.helpers.welcomepages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deliverable_1_seg.MainActivity;
import com.example.deliverable_1_seg.R;
import com.example.deliverable_1_seg.helpers.Organizer_After_login.Create_Event ;
import com.example.deliverable_1_seg.helpers.Organizer_After_login.ApproveAttendeesActivity;
import com.example.deliverable_1_seg.helpers.Organizer_After_login.EventListActivity;

public class OrganizerWelcomePage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_welcome_page);

        // Log Out Button
        Button organizerLogOutButton = findViewById(R.id.OrganizerLogOut_button);
        organizerLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerWelcomePage.this, MainActivity.class);
                startActivity(intent);
                finish();  // Close the current activity after logging out
            }
        });

        // Create New Event Button
        Button createEventButton = findViewById(R.id.new_event);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerWelcomePage.this, Create_Event.class);
                startActivity(intent);
            }
        });

        // Manage Attendees Button
        Button manageEventsButton = findViewById(R.id.manageEventsButton);
        manageEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerWelcomePage.this, EventListActivity.class);
                startActivity(intent);
            }
        });
    }

    // Log Out Method
    public void logOut(View view) {
        finish();
    }
}
