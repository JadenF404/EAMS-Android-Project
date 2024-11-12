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
    public interface requestStatus {
        void DataLoaded(List<String> events);
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

                        // Ensure people and requests lists are not null
                        if (event.getPeople() == null) {
                            event.setPeople(new ArrayList<>());
                        }
                        if (event.getRequests() == null) {
                            event.setRequests(new ArrayList<>());
                        }

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

    //loads the organizers current events
    public void loadAllEvents (DataStatus dataStatus){
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

    // Loads the list of requests by eventId and returns a list of request strings
    public void loadRequestsByEventId(String eventId, requestStatus requestStatus) {
        eventsRef.child(eventId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check if the event exists
//                if (dataSnapshot.exists()) {

                    // Create a list to hold the request strings
                    List<String> requestList = new ArrayList<>();

                    // Check if the 'requests' node exists
                    if (dataSnapshot.child("requests").exists()) {
                        // Loop through the 'requests' map and collect all request values
                        for (DataSnapshot requestSnapshot : dataSnapshot.child("requests").getChildren()) {
                            String request = requestSnapshot.getValue(String.class);
                            if (request != null) {
                                requestList.add(request);
                            }
                        }
                    }

                    requestStatus.DataLoaded(requestList);
//                } else {
//                    dataStatus.onError(new DatabaseError(DatabaseError.DISCONNECTED, "Event not found"));
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to load requests", databaseError.toException());
                requestStatus.onError(databaseError);
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

    public void joinEvent(String eventId, String userID, writeCallback callback){
        if (eventId != null && userID != null) {
            DatabaseReference peopleRef = eventsRef.child(eventId).child("people");

            // Add the user ID to the "people" list (push creates a new entry with a unique key)
            peopleRef.push().setValue(userID).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User joined event successfully");
                    callback.onSuccess();
                } else {
                    Log.e(TAG, "Failed to join event", task.getException());
                    callback.onFailure(DatabaseError.fromException(task.getException()));
                }
            });
        } else {
            Log.e(TAG, "Event ID or User ID is null. Cannot join event.");
            callback.onFailure(DatabaseError.fromException(new Exception("Event ID or User ID is null")));
        }
    }

    public void requestEvent(String eventId, String userID, writeCallback callback){
        if (eventId != null && userID != null) {
            DatabaseReference requestsRef = eventsRef.child(eventId).child("requests");

            // Add the user ID to the "requests" list
            requestsRef.push().setValue(userID).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User requested event successfully");
                    callback.onSuccess();
                } else {
                    Log.e(TAG, "Failed to request event", task.getException());
                    callback.onFailure(DatabaseError.fromException(task.getException()));
                }
            });
        } else {
            Log.e(TAG, "Event ID or User ID is null. Cannot request event.");
            callback.onFailure(DatabaseError.fromException(new Exception("Event ID or User ID is null")));
        }
    }

    public void removeUserFromPeople(String eventId, String userID, writeCallback callback) {
        if (eventId != null && userID != null) {
            DatabaseReference peopleRef = eventsRef.child(eventId).child("people");

            // Query for the user in the "people" list and remove it
            peopleRef.orderByValue().equalTo(userID).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        userSnapshot.getRef().removeValue().addOnCompleteListener(removeTask -> {
                            if (removeTask.isSuccessful()) {
                                Log.d(TAG, "User removed from people list successfully");
                                callback.onSuccess();
                            } else {
                                Log.e(TAG, "Failed to remove user", removeTask.getException());
                                callback.onFailure(DatabaseError.fromException(removeTask.getException()));
                            }
                        });
                    }
                } else {
                    Log.e(TAG, "Failed to find user in people list", task.getException());
                    callback.onFailure(DatabaseError.fromException(task.getException()));
                }
            });
        } else {
            Log.e(TAG, "Event ID or User ID is null. Cannot remove user from people list.");
            callback.onFailure(DatabaseError.fromException(new Exception("Event ID or User ID is null")));
        }
    }


    public void removeUserFromRequests(String eventId, String userID, writeCallback callback) {
        if (eventId != null && userID != null) {
            DatabaseReference requestsRef = eventsRef.child(eventId).child("requests");

            // Query for the user in the "requests" list and remove it
            requestsRef.orderByValue().equalTo(userID).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        userSnapshot.getRef().removeValue().addOnCompleteListener(removeTask -> {
                            if (removeTask.isSuccessful()) {
                                Log.d(TAG, "User removed from requests list successfully");
                                callback.onSuccess();
                            } else {
                                Log.e(TAG, "Failed to remove user from requests list", removeTask.getException());
                                callback.onFailure(DatabaseError.fromException(removeTask.getException()));
                            }
                        });
                    }
                } else {
                    Log.e(TAG, "Failed to find user in requests list", task.getException());
                    callback.onFailure(DatabaseError.fromException(task.getException()));
                }
            });
        } else {
            Log.e(TAG, "Event ID or User ID is null. Cannot remove user from requests list.");
            callback.onFailure(DatabaseError.fromException(new Exception("Event ID or User ID is null")));
        }
    }

}
