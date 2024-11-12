package com.example.deliverable_1_seg.helpers.Organizer_After_login;


import android.os.Bundle;
import android.widget.Toast;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deliverable_1_seg.*;
import com.example.deliverable_1_seg.helpers.db.Event;
import com.example.deliverable_1_seg.helpers.db.EventAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;


public class EventListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private FirebaseEventHelper firebaseEventHelper;
    private ArrayList<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        recyclerView = findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the event list and Firebase helper
        eventList = new ArrayList<>();
        firebaseEventHelper = new FirebaseEventHelper();

        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String organizerId = user.getUid();

        //isPastEvents toggle
        boolean isPastEvents = getIntent().getBooleanExtra("isPastEvents", false);
        if (isPastEvents) {
            loadPastEvents(organizerId);
        } else {
            loadCurrentEvents(organizerId);
        }
    }
    public void onBackButtonClick(View view) {
        finish();  // Closes the current activity and returns to the previous one
    }

    private void loadCurrentEvents(String organizerId) {
        firebaseEventHelper.loadEventsForCurrentUser(organizerId, new FirebaseEventHelper.DataStatus() {
            @Override
            public void DataLoaded(List<Event> events) {
                eventList.clear();
                eventList.addAll(events);
                adapter = new EventAdapter(eventList, EventListActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(DatabaseError error) {
                Toast.makeText(EventListActivity.this, "Failed to load current events: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPastEvents(String organizerId) {
        firebaseEventHelper.loadPastEventsForCurrentUser(organizerId, new FirebaseEventHelper.DataStatus() {
            @Override
            public void DataLoaded(List<Event> events) {
                eventList.clear();
                eventList.addAll(events);
                adapter = new EventAdapter(eventList, EventListActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(DatabaseError error) {
                Toast.makeText(EventListActivity.this, "Failed to load past events: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
