package com.example.deliverable_1_seg.helpers.Attendee_After_Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deliverable_1_seg.FirebaseEventHelper;
import com.example.deliverable_1_seg.R;
import com.example.deliverable_1_seg.helpers.db.Event;
import com.example.deliverable_1_seg.helpers.db.EventAdapter;
import com.example.deliverable_1_seg.helpers.welcomepages.AttendeeWelcomePage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class RegisteredEventsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private FirebaseEventHelper firebaseEventHelper;
    private ArrayList<Event> eventList;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_view_registered_events);

        // Initialize views and Firebase helper
        recyclerView = findViewById(R.id.recyclerViewRegisteredEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseEventHelper = new FirebaseEventHelper();
        eventList = new ArrayList<>();

        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        // Load registered and requested events
        loadRegisteredAndRequestedEvents();

        //back button
        Button attendeeButton = findViewById(R.id.backButton);
        attendeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.deliverable_1_seg.helpers.Attendee_After_Login.RegisteredEventsActivity.this, AttendeeWelcomePage.class);
                startActivity(intent);
            }
        });

    }

    private void loadRegisteredAndRequestedEvents(){
        firebaseEventHelper.loadRegisteredEvents(userID, new FirebaseEventHelper.EventIDDataStatus() {
            @Override
            public void DataLoaded(List<String> registeredEventIds) {
                firebaseEventHelper.loadRequestedEvents(userID, new FirebaseEventHelper.EventIDDataStatus() {
                    @Override
                    public void DataLoaded(List<String> requestedEventIds) {
                        List<String> userEventIds = new ArrayList<>();
                        userEventIds.addAll(registeredEventIds);
                        userEventIds.addAll(requestedEventIds);

                        // Now, load events based on these IDs
                        loadEvents(userEventIds);
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        Toast.makeText(RegisteredEventsActivity.this, "Error loading requested events: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(DatabaseError error) {
                Toast.makeText(RegisteredEventsActivity.this, "Error loading registered events: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadEvents(List<String> eventIds) {
        firebaseEventHelper.loadAllEvents(new FirebaseEventHelper.DataStatus() {
            @Override
            public void DataLoaded(List<Event> events) {
                // Filter the events based on the user's event IDs
                for (Event event : events) {
                    if (eventIds.contains(event.getEventId())) {
                        eventList.add(event);
                    }
                }
                // Set the adapter
                adapter = new EventAdapter(eventList, RegisteredEventsActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(DatabaseError error) {
                Toast.makeText(RegisteredEventsActivity.this, "Failed to load events: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}