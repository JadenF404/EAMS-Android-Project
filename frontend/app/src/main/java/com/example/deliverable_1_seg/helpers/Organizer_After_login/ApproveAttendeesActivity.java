package com.example.deliverable_1_seg.helpers.Organizer_After_login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deliverable_1_seg.FirebaseHelper;
import com.example.deliverable_1_seg.R;
import com.example.deliverable_1_seg.helpers.db.PendingAttendeeAdapter;
import com.example.deliverable_1_seg.helpers.db.RegistrationRequest;

import java.util.ArrayList;
import java.util.List;

public class ApproveAttendeesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PendingAttendeeAdapter adapter;
    private FirebaseHelper firebaseHelper;
    private List<RegistrationRequest> pendingRequests;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_requests);

        recyclerView = findViewById(R.id.recyclerViewRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pendingRequests = new ArrayList<>();
        firebaseHelper = new FirebaseHelper();

        String eventId = getIntent().getStringExtra("eventId");

        if (eventId != null) {
            loadPendingRequests(eventId);
        } else {
            Toast.makeText(this, "Invalid Event ID", Toast.LENGTH_SHORT).show();
            Log.e("ApproveAttendeesActivity", "Event ID is null");
        }

    }

    private void loadPendingRequests(String eventId){
        //ToDo implement method
    }

    public void onBackButtonClick(View view) {
        finish();  // Closes the current activity and returns to the previous one
    }

}
