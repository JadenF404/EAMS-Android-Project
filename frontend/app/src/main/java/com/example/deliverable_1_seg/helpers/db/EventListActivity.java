package com.example.deliverable_1_seg.helpers.db;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deliverable_1_seg.*;
import com.example.deliverable_1_seg.helpers.Organizer_After_login.Create_Event;

import java.util.ArrayList;

public class EventListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        recyclerView = findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the list of events from Create_Event
        ArrayList<Create_Event.Event> eventList = Create_Event.getEventList();
        adapter = new EventAdapter(eventList);
        recyclerView.setAdapter(adapter);
    }
}
