package com.example.deliverable_1_seg.user_actions;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private DatabaseReference attendeeRequestsRef;
    private DatabaseReference organizerRequestsRef;
    private List<RegistrationRequest> rejectedRequestList = new ArrayList<>();
    private RequestsAdapter rejectedRequestsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_rejected_requests_inbox);

        // Initialize RecyclerView
        rejectedRequestsRecyclerView = findViewById(R.id.rejectedRequestsRecyclerView);
        rejectedRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rejectedRequestsAdapter = new RequestsAdapter(this, rejectedRequestList, true);
        rejectedRequestsRecyclerView.setAdapter(rejectedRequestsAdapter);

        // Initialize Firebase references
        attendeeRequestsRef = FirebaseDatabase.getInstance().getReference("attendee/attendee_requests");
        organizerRequestsRef = FirebaseDatabase.getInstance().getReference("organizer/organizer_requests");

        // Load rejected requests
        loadRejectedRequests();
    }

    private void loadRejectedRequests() {
        attendeeRequestsRef.orderByChild("status").equalTo("rejected").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RegistrationRequest request = snapshot.getValue(RegistrationRequest.class);
                    if (request != null) {
                        rejectedRequestList.add(request);
                    }
                }
                rejectedRequestsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("AdminRejectedRequestsActivity", "Failed to load rejected attendee requests", databaseError.toException());
            }
        });

        organizerRequestsRef.orderByChild("status").equalTo("rejected").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RegistrationRequest request = snapshot.getValue(RegistrationRequest.class);
                    if (request != null) {
                        rejectedRequestList.add(request);
                    }
                }
                rejectedRequestsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("AdminRejectedRequestsActivity", "Failed to load rejected organizer requests", databaseError.toException());
            }
        });
    }

    public void onBackButtonClick(View view) {
        finish();
    }
}
