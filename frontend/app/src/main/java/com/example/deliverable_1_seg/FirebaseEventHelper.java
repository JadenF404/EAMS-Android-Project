package com.example.deliverable_1_seg;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.deliverable_1_seg.helpers.db.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseEventHelper {
    private static final String TAG = "FirebaseEventHelper";
    private final DatabaseReference eventsRef;

    public FirebaseEventHelper() {
        // Initialize Firebase reference for events
        eventsRef = FirebaseDatabase.getInstance().getReference("events");
    }

    public interface writeCallback {
        void onSuccess();
        void onFailure(DatabaseError error);
    }

    public interface DataStatus {
        //void DataLoaded();
        //void onError();
    }

    //method called when creating event
    public void addEvent(Event event, writeCallback callback){
        String eventID = eventsRef.push().getKey();

       if (eventID != null){
           event.setEventId(eventID);
           eventsRef.child(eventID).setValue(event).addOnCompleteListener(task -> {
               if (task.isSuccessful()){
                   Log.d(TAG, "Event added successfully");
                   callback.onSuccess();
               } else {
                   Log.e(TAG, "Failed to add event", task.getException());
                   callback.onFailure(DatabaseError.fromException(task.getException()));
               }
           });
       } else {
           Log.e(TAG, "Failed to generate unique event ID.");
           callback.onFailure(DatabaseError.fromException(new Exception("Failed to generate unique event ID.")));
       }
    }
}
