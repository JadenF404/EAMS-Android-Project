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
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class RegisteredEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_view_registered_events);


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

}