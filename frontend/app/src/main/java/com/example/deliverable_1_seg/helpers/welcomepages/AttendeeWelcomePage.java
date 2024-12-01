package com.example.deliverable_1_seg.helpers.welcomepages;

import android.content.Intent;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.deliverable_1_seg.FirebaseEventHelper;
import com.example.deliverable_1_seg.MainActivity;
import com.example.deliverable_1_seg.R;
import com.example.deliverable_1_seg.helpers.Attendee_After_Login.RegisteredEventsActivity;
import com.example.deliverable_1_seg.helpers.Attendee_After_Login.SearchEventsActivity;
import com.example.deliverable_1_seg.helpers.Organizer_After_login.EventListActivity;
import com.example.deliverable_1_seg.helpers.db.Event;
import com.example.deliverable_1_seg.helpers.db.EventAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;


public class AttendeeWelcomePage extends AppCompatActivity{

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private FirebaseEventHelper firebaseEventHelper;
    private ArrayList<Event> eventList;

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

        //view events button
        Button eventsButton = findViewById(R.id.searchButton);
        eventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendeeWelcomePage.this, SearchEventsActivity.class);
                startActivity(intent);
            }
        });

        //view registrations button
        Button registrationButton = findViewById(R.id.viewRegistrationsButton);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendeeWelcomePage.this, RegisteredEventsActivity.class);
                startActivity(intent);
            }
        });

    }
    
    public void logOut(View view){
        finish();

    }

}
