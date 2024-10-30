package com.example.deliverable_1_seg.user_actions;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deliverable_1_seg.FirebaseHelper;
import com.example.deliverable_1_seg.R;
import com.example.deliverable_1_seg.helpers.db.RegistrationRequest;
import com.example.deliverable_1_seg.helpers.db.RequestsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AdminRejectedRequestsActivity extends AppCompatActivity {

    private RecyclerView rejectedRequestsRecyclerView;
    private final List<RegistrationRequest> rejectedRequestList = new ArrayList<>();
    private RequestsAdapter rejectedRequestsAdapter;
    private FirebaseHelper firebaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_rejected_requests_inbox);

        // Initialize RecyclerView
        rejectedRequestsRecyclerView = findViewById(R.id.rejectedRequestsRecyclerView);
        rejectedRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rejectedRequestsAdapter = new RequestsAdapter(this, rejectedRequestList, true);
        rejectedRequestsRecyclerView.setAdapter(rejectedRequestsAdapter);


        // Initialize FirebaseHelper
        firebaseHelper = new FirebaseHelper();

        // Load rejected requests
        loadRejectedRequests();
    }

    private void loadRejectedRequests() {
        firebaseHelper.loadRejectedAttendeeRequests(new FirebaseHelper.DataStatus() {
            @Override
            public void onDataLoaded(List<RegistrationRequest> requests) {
                rejectedRequestsAdapter.addRequests(requests); // Use addRequests for specific notifications
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("AdminRequestsActivity", "Failed to load attendee requests", error.toException());
//                Toast.makeText(this, "1111", Toast.LENGTH_SHORT).show();
            }
        });

        firebaseHelper.loadRejectedOrganizerRequests(new FirebaseHelper.DataStatus() {
            @Override
            public void onDataLoaded(List<RegistrationRequest> requests) {
                rejectedRequestsAdapter.addRequests(requests); // Use addRequests for specific notifications
            }

            @Override
            public void onError(DatabaseError error) {
//                Toast.makeText(, "22222", Toast.LENGTH_SHORT).show();
                Log.e("AdminRequestsActivity", "Failed to load organizer requests", error.toException());
            }
        });

    }

//    public void onBackButtonClick(View view) {
//        finish();
//    }
}
