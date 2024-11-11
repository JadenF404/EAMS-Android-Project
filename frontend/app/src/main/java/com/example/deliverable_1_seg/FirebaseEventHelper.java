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
        void DataLoaded(List<Event> events);
        void onError(DatabaseError error);
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

    //loads the organizers current events
    public void loadEventsForCurrentUser (String organizerId, DataStatus dataStatus){
        eventsRef.orderByChild("organizerId").equalTo(organizerId).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot){
                List<Event> eventsList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Event event = snapshot.getValue(Event.class);
                    if (event != null){
                        event.setEventId(snapshot.getKey());
                        eventsList.add(event);
                    }
                }
                dataStatus.DataLoaded(eventsList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to load events", databaseError.toException());
                dataStatus.onError(databaseError);
            }
        });
    }

    public void deleteEvent(String eventId, writeCallback callback){
        if (eventId != null){
            eventsRef.child(eventId).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Event deleted successfully");
                    callback.onSuccess();
                } else {
                    Log.e(TAG, "Failed to delete event", task.getException());
                    callback.onFailure(DatabaseError.fromException(task.getException()));
                }
            });
        } else {
            Log.e(TAG, "Event ID is null. Cannot delete event.");
        }
            callback.onFailure(DatabaseError.fromException(new Exception("Event ID is null")));
    }
}
