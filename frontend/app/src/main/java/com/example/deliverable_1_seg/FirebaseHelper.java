package com.example.deliverable_1_seg;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.deliverable_1_seg.helpers.db.RegistrationRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseHelper {

    private static final String TAG = "FirebaseHelper";
    private final DatabaseReference attendeeRequestsRef;
    private final DatabaseReference organizerRequestsRef;
    private final FirebaseAuth userAuth;

    public FirebaseHelper() {
        // Initialize Firebase references
        attendeeRequestsRef = FirebaseDatabase.getInstance().getReference("attendee/attendee_requests");
        organizerRequestsRef = FirebaseDatabase.getInstance().getReference("organizer/organizer_requests");
        userAuth = FirebaseAuth.getInstance();
    }

    // Define an interface for callbacks
    public interface DataStatus {
        void onDataLoaded(List<RegistrationRequest> requests);
        void onError(DatabaseError error);
    }

    // Load pending requests for attendees
    public void loadAttendeeRequests(DataStatus dataStatus) {
        attendeeRequestsRef.orderByChild("status").equalTo("pending").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<RegistrationRequest> requestList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RegistrationRequest request = snapshot.getValue(RegistrationRequest.class);
                    if (request != null) {
                        request.setUserId(snapshot.getKey());
                        requestList.add(request);
                    }
                }
                dataStatus.onDataLoaded(requestList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to load attendee requests", databaseError.toException());
                dataStatus.onError(databaseError);
            }
        });
    }

    // Load pending requests for organizers
    public void loadOrganizerRequests(DataStatus dataStatus) {
        organizerRequestsRef.orderByChild("status").equalTo("pending").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<RegistrationRequest> requestList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RegistrationRequest request = snapshot.getValue(RegistrationRequest.class);
                    if (request != null) {
                        request.setUserId(snapshot.getKey());
                        requestList.add(request);
                    }
                }
                dataStatus.onDataLoaded(requestList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to load organizer requests", databaseError.toException());
                dataStatus.onError(databaseError);
            }
        });
    }

    // Sign in method

    //TODO: very scuffed way to inject the UID, need to refactor into a better class system design!
    public void signIn(String email, String password, boolean isOrganizer, SignInCallback callback) {
        userAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = userAuth.getCurrentUser();
                DatabaseReference userStatusRef = FirebaseDatabase.getInstance().getReference((isOrganizer) ? "organizer/organizer_requests": "attendee/attendee_requests").child(user.getUid()).child("status");


                userStatusRef.get().addOnCompleteListener(statustask -> {
                    if (statustask.isSuccessful()) {
                        DataSnapshot dataSnapshot = statustask.getResult();
                        if (dataSnapshot.exists()) {
                            String status = dataSnapshot.getValue(String.class);
                            if ("approved".equals(status)) {
                                callback.onSuccess(user, "approved");
                            } else if ("pending".equals(status)) {
                                callback.onSuccess(user, "pending");
                            } else {
                                callback.onSuccess(user, "rejected");
                            }
                        } else {
                            callback.onFailure(new Exception("User status not found."));
                        }
                    } else {
                        callback.onFailure(statustask.getException());
                        Log.e(TAG, "Error retrieving status", statustask.getException());
                    }
                });


            } else {
                callback.onFailure(task.getException());
                Log.e(TAG, "Sign-in failed", task.getException());
            }
        });
    }

    // Sign up method for both attendees and organizers
    public void signUp(String email, String password, Map<String, Object> userData, boolean isOrganizer, SignUpCallback callback) {
        userAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = userAuth.getCurrentUser();
                if (user != null) {
                    String userKey = user.getUid();
                    DatabaseReference userRef = isOrganizer ? organizerRequestsRef : attendeeRequestsRef;

                    userRef.child(userKey).setValue(userData).addOnCompleteListener(databaseTask -> {
                        if (databaseTask.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure(databaseTask.getException());
                            Log.e(TAG, "Failed to save user details", databaseTask.getException());
                        }
                    });
                }
            } else {
                callback.onFailure(task.getException());
                Log.e(TAG, "Sign-up failed", task.getException());
            }
        });
    }


    // Callback interfaces for authentication
    public interface SignInCallback {
        void onSuccess(FirebaseUser user, String status);
        void onFailure(Exception e);
    }

    public interface SignUpCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

}
