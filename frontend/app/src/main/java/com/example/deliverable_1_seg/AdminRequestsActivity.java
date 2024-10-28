package com.example.deliverable_1_seg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


import androidx.appcompat.app.AppCompatActivity;


public class AdminRequestsActivity extends AppCompatActivity {

    private RecyclerView requestsRecyclerView;
    private DatabaseReference attendeeRequestsRef;
    private DatabaseReference organizerRequestsRef;
    private List<RegistrationRequest> requestList = new ArrayList<>();
    private RequestsAdapter requestsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_requests_inbox);

        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminRequestsActivity.this, AdministratorWelcomePage.class);
                startActivity(intent);


            }
        });

        // Initialize RecyclerView
        requestsRecyclerView = findViewById(R.id.requestsRecyclerView);
        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestsAdapter = new RequestsAdapter(this, requestList);
        requestsRecyclerView.setAdapter(requestsAdapter);

        // Initialize Firebase references
        attendeeRequestsRef = FirebaseDatabase.getInstance().getReference("attendee/attendee_requests");
        organizerRequestsRef = FirebaseDatabase.getInstance().getReference("organizer/organizer_requests");

        // Load pending requests
        loadPendingRequests();
    }

    //load and format the pending requests
    private void loadPendingRequests() {
        attendeeRequestsRef.orderByChild("status").equalTo("pending").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RegistrationRequest request = snapshot.getValue(RegistrationRequest.class);
                    if (request != null) {
                        requestList.add(request);
                    }
                }
                requestsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("AdminRequestsActivity", "Failed to load attendee requests", databaseError.toException());
            }
        });

        organizerRequestsRef.orderByChild("status").equalTo("pending").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RegistrationRequest request = snapshot.getValue(RegistrationRequest.class);
                    if (request != null) {
                        requestList.add(request);
                    }
                }
                requestsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("AdminRequestsActivity", "Failed to load organizer requests", databaseError.toException());
            }
        });
    }

}
