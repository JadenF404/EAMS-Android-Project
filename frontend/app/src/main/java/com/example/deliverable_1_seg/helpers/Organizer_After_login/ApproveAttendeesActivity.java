package com.example.deliverable_1_seg.helpers.Organizer_After_login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deliverable_1_seg.FirebaseEventHelper;
import com.example.deliverable_1_seg.FirebaseHelper;
import com.example.deliverable_1_seg.R;
import com.example.deliverable_1_seg.helpers.db.Event;
import com.example.deliverable_1_seg.helpers.db.EventAdapter;
import com.example.deliverable_1_seg.helpers.db.EventRequestAdapter;
import com.example.deliverable_1_seg.helpers.db.PendingAttendeeAdapter;
import com.example.deliverable_1_seg.helpers.db.RegistrationRequest;
import com.example.deliverable_1_seg.helpers.welcomepages.AttendeeWelcomePage;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ApproveAttendeesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EventRequestAdapter adapter;
    private FirebaseEventHelper firebaseHelper;
    private FirebaseEventHelper firebaseEventHelper;
    private List<RegistrationRequest> pendingRequests;
    private List<RegistrationRequest> requestList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_requests);

        recyclerView = findViewById(R.id.recyclerViewRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pendingRequests = new ArrayList<>();
        firebaseEventHelper = new FirebaseEventHelper();

        String eventId = getIntent().getStringExtra("eventId");

        requestList = new ArrayList<>();

        if (eventId != null) {
            loadPendingRequests(eventId);
        } else {
            Toast.makeText(this, "Invalid Event ID", Toast.LENGTH_SHORT).show();
            Log.e("ApproveAttendeesActivity", "Event ID is null");
        }

    }

    private void loadPendingRequests(String eventId){
        firebaseEventHelper.loadRequestsByEventId(eventId, new FirebaseEventHelper.requestStatus() {

           public void DataLoaded(List<RegistrationRequest> requests) {
               requestList.clear();
               requestList.addAll(requests);
               adapter = new EventRequestAdapter(requestList, ApproveAttendeesActivity.this, eventId);
               recyclerView.setAdapter(adapter);
           }

           @Override
           public void onError(DatabaseError error) {
               Toast.makeText(ApproveAttendeesActivity.this, "Failed to load events: " + error.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });

    }

    public void onBackButtonClick(View view) {
        finish();  // Closes the current activity and returns to the previous one
    }

}
