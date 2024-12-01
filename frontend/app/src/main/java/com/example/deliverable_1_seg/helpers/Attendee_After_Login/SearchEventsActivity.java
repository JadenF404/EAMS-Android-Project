package com.example.deliverable_1_seg.helpers.Attendee_After_Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deliverable_1_seg.FirebaseEventHelper;
import com.example.deliverable_1_seg.MainActivity;
import com.example.deliverable_1_seg.R;
import com.example.deliverable_1_seg.helpers.db.Event;
import com.example.deliverable_1_seg.helpers.db.EventAdapter;
import com.example.deliverable_1_seg.helpers.welcomepages.AttendeeWelcomePage;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class SearchEventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private FirebaseEventHelper firebaseEventHelper;
    private ArrayList<Event> eventList;
    private EditText searchInput;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_view_events);


        //log out button
        Button attendeeButton = findViewById(R.id.backButton);
        attendeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.deliverable_1_seg.helpers.Attendee_After_Login.SearchEventsActivity.this, AttendeeWelcomePage.class);
                startActivity(intent);
            }
        });

        //find UI elements
        searchInput = findViewById(R.id.searchInput);
        searchButton = findViewById(R.id.searchButton);
        recyclerView = findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //search button listener
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String keyword = searchInput.getText().toString().trim();

                if(!keyword.isEmpty()){
                    searchEvents(keyword);
                } else {
                    Toast.makeText(SearchEventsActivity.this, "Enter a search keyword", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Initialize the event list and Firebase helper
        eventList = new ArrayList<>();
        firebaseEventHelper = new FirebaseEventHelper();

        //then load events
        firebaseEventHelper.loadAllEvents(new FirebaseEventHelper.DataStatus() {

            @Override
            public void DataLoaded(List<Event> events) {
                eventList.clear();
                eventList.addAll(events);
                adapter = new EventAdapter(eventList, com.example.deliverable_1_seg.helpers.Attendee_After_Login.SearchEventsActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(DatabaseError error) {
                Toast.makeText(com.example.deliverable_1_seg.helpers.Attendee_After_Login.SearchEventsActivity.this, "Failed to load events: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void searchEvents(String keyword){
        firebaseEventHelper.searchEvents(keyword, new FirebaseEventHelper.DataStatus() {
            @Override
            public void DataLoaded(List<Event> events) {
                eventList.clear();
                eventList.addAll(events);
                adapter = new EventAdapter(eventList, SearchEventsActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(DatabaseError error) {
                Toast.makeText(SearchEventsActivity.this, "Failed to search events: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}