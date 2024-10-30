package com.example.deliverable_1_seg.user_actions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deliverable_1_seg.FirebaseHelper;
import com.example.deliverable_1_seg.MainActivity;
import com.example.deliverable_1_seg.R;
import com.google.firebase.database.DatabaseError;
import com.example.deliverable_1_seg.helpers.db.RequestsAdapter;

import com.example.deliverable_1_seg.helpers.welcomepages.AdministratorWelcomePage;
import com.example.deliverable_1_seg.helpers.db.RegistrationRequest;

import java.util.ArrayList;
import java.util.List;

public class AdminRequestsActivity extends AppCompatActivity {

    private RecyclerView requestsRecyclerView;
    private final List<RegistrationRequest> requestList = new ArrayList<>(); // Make final
    private RequestsAdapter requestsAdapter;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_requests_inbox);

        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminRequestsActivity.this, AdministratorWelcomePage.class);
            startActivity(intent);
        });

        // Initialize RecyclerView
        requestsRecyclerView = findViewById(R.id.requestsRecyclerView);
        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestsAdapter = new RequestsAdapter(this, requestList, false);
        requestsRecyclerView.setAdapter(requestsAdapter);

        // Initialize FirebaseHelper
        firebaseHelper = new FirebaseHelper();

        // Load pending requests
        loadPendingRequests();



    }


    private void loadPendingRequests() {
        firebaseHelper.loadAttendeeRequests("pending", new FirebaseHelper.DataStatus() {
            @Override
            public void onDataLoaded(List<RegistrationRequest> requests) {
                requestsAdapter.addRequests(requests); // Use addRequests for specific notifications
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("AdminRequestsActivity", "Failed to load attendee requests", error.toException());
            }
        });

        firebaseHelper.loadOrganizerRequests("pending", new FirebaseHelper.DataStatus() {
            @Override
            public void onDataLoaded(List<RegistrationRequest> requests) {
                requestsAdapter.addRequests(requests); // Use addRequests for specific notifications
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("AdminRequestsActivity", "Failed to load organizer requests", error.toException());
            }
        });
    }
}
