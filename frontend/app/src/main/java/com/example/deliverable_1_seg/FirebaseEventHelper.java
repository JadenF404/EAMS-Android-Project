package com.example.deliverable_1_seg;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.deliverable_1_seg.helpers.db.Event;
import com.example.deliverable_1_seg.helpers.db.RegistrationRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class FirebaseEventHelper {
    private static final String TAG = "FirebaseEventHelper";
    public final DatabaseReference eventsRef;
    public final DatabaseReference attendeeRef;


    public FirebaseEventHelper() {
        // Initialize Firebase reference for events
        eventsRef = FirebaseDatabase.getInstance().getReference("events");
        attendeeRef = FirebaseDatabase.getInstance().getReference("attendee/attendee_requests");
    }

    public interface writeCallback {
        void onSuccess();

        void onFailure(DatabaseError error);
    }

    public interface DataStatus {
        void DataLoaded(List<Event> events);

        void onError(DatabaseError error);
    }

    public interface EventIDDataStatus {
        void DataLoaded(List<String> eventIds);
        void onError(DatabaseError error);
    }

    public interface requestStatus {
        void DataLoaded(List<RegistrationRequest> events);

        void onError(DatabaseError error);
    }

    public interface EventRequestStatusCallback {
        void onStatusLoaded(boolean isRequested);
        void onError(DatabaseError error);
    }

    /*********************
    EVENT CREATION METHODS
     ********************/

    //method called when creating event
    public void addEvent(Event event, writeCallback callback) {
        String eventID = eventsRef.push().getKey();

        if (eventID != null) {
            event.setEventId(eventID);
            eventsRef.child(eventID).setValue(event).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
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

    /*********************
     ORGANIZER EVENT METHODS
     ********************/
    //loads the organizers current events
    public void loadEventsForCurrentUser(String organizerId, DataStatus dataStatus) {
        long currentTime = Calendar.getInstance().getTimeInMillis();

        eventsRef.orderByChild("organizerId").equalTo(organizerId).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Event> eventsList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);
                    if (event != null && event.getLongDate() > currentTime) {
                        event.setEventId(snapshot.getKey());

                        // Ensure people and requests lists are not null
                        if (event.getPeople() == null) {
                            event.setPeople(new HashMap<>());
                        }
                        if (event.getRequests() == null) {
                            event.setRequests(new HashMap<>());
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

    //loads the organizers past events
    public void loadPastEventsForCurrentUser(String organizerId, DataStatus dataStatus) {
        long currentTime = Calendar.getInstance().getTimeInMillis();

        eventsRef.orderByChild("organizerId").equalTo(organizerId).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Event> eventsList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);
                    if (event != null && event.getLongDate() < currentTime) {
                        event.setEventId(snapshot.getKey());

                        // Ensure people and requests lists are not null
                        if (event.getPeople() == null) {
                            event.setPeople(new HashMap<>());
                        }
                        if (event.getRequests() == null) {
                            event.setRequests(new HashMap<>());
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

    /*********************
     ATTENDEE EVENT METHODS
     ********************/

    //loads the all current events
    public void loadAllEvents(DataStatus dataStatus) {
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Event> eventsList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);
                    if (event != null) {
                        if (event.getPeople() == null) {
                            event.setPeople(new HashMap<>());
                        }
                        if (event.getRequests() == null) {
                            event.setRequests(new HashMap<>());
                        }
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

    //loads all events a user has not yet registered for
    public void loadAllEvents(String userID, DataStatus dataStatus) {
        loadRegisteredEvents(userID, new EventIDDataStatus() {
            @Override
            public void DataLoaded(List<String> registeredEventIds) {
                loadRequestedEvents(userID, new EventIDDataStatus() {
                    @Override
                    public void DataLoaded(List<String> requestedEventIds) {
                        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                List<Event> eventsList = new ArrayList<>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Event event = snapshot.getValue(Event.class);
                                    if (event != null) {
                                        String eventId = snapshot.getKey();
                                        if (!registeredEventIds.contains(eventId) && !requestedEventIds.contains((eventId))){
                                            if (event.getPeople() == null) {
                                                event.setPeople(new HashMap<>());
                                            }
                                            if (event.getRequests() == null) {
                                                event.setRequests(new HashMap<>());
                                            }
                                            event.setEventId(eventId);
                                            eventsList.add(event);
                                        }

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

                    @Override
                    public void onError(DatabaseError error) {
                        Log.e(TAG, "Failed to load Requested events", error.toException());
                        dataStatus.onError(error);
                    }
                });
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e(TAG, "Failed to load registered events", error.toException());
                dataStatus.onError(error);
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
                List<RegistrationRequest> requestList = new ArrayList<>();

                // Check if the 'requests' node exists
                if (dataSnapshot.child("requests").exists()) {
                    // Loop through the 'requests' map and collect all request values
                    for (DataSnapshot requestSnapshot : dataSnapshot.child("requests").getChildren()) {
                        String userID = requestSnapshot.getKey();
                        Boolean value = requestSnapshot.getValue(Boolean.class);

                        if (value != null && value) {
                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("attendee/attendee_requests").child(userID);

                            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot userSnapshot) {
                                    String firstname = userSnapshot.child("firstName").getValue(String.class);
                                    String lastname = userSnapshot.child("lastName").getValue(String.class);
                                    String email = userSnapshot.child("email").getValue(String.class);
                                    String phone = userSnapshot.child("phoneNumber").getValue(String.class);
                                    String address = userSnapshot.child("address").getValue(String.class);

                                    RegistrationRequest registrationRequest = new RegistrationRequest(userID, firstname, lastname, email, phone, address);
                                    requestList.add(registrationRequest);

                                    if (requestList.size() == dataSnapshot.child("requests").getChildrenCount()) {
                                        requestStatus.DataLoaded(requestList);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e(TAG, "Failed to load user details", databaseError.toException());
                                    requestStatus.onError(databaseError);
                                }
                            });
                        }
                    }
                } else {
                    requestStatus.DataLoaded(requestList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to load requests", databaseError.toException());
                requestStatus.onError(databaseError);
            }
        });
    }

    //searches database for events with keyword in title or description
    public void searchEvents(String keyword, String userID, final DataStatus dataStatus) {

        loadRegisteredEvents(userID, new EventIDDataStatus() {

            @Override
            public void DataLoaded(List<String> registeredEventIds) {
                loadRequestedEvents(userID, new EventIDDataStatus() {

                    @Override
                    public void DataLoaded(List<String> requestedEventIds) {
                        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<Event> eventList = new ArrayList<>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Event event = snapshot.getValue(Event.class);
                                    String eventID = snapshot.getKey();
                                    if (event != null){
                                        if (event.getTitle() != null && event.getTitle().toLowerCase().contains(keyword.toLowerCase())){
                                            if (!registeredEventIds.contains(eventID) && !requestedEventIds.contains(eventID)){
                                                event.setEventId(snapshot.getKey());
                                                eventList.add(event);
                                            }
                                        } else if (event.getDescription() != null && event.getDescription().toLowerCase().contains(keyword.toLowerCase())){
                                            if (!registeredEventIds.contains(eventID) && !requestedEventIds.contains(eventID)){
                                                event.setEventId(snapshot.getKey());
                                                eventList.add(event);
                                            }
                                        }
                                    }
                                }
                                dataStatus.DataLoaded(eventList);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e(TAG, "Failed to load events", error.toException());
                                dataStatus.onError(error);
                            }
                        });
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });

    }

    // Method to load registered events for the user
    public void loadRegisteredEvents(String userId, final EventIDDataStatus callback) {
        attendeeRef.child(userId).child("registeredEvents").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> eventIds = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    eventIds.add(snapshot.getKey()); // eventId
                }
                callback.DataLoaded(eventIds); // Return the list of eventIds the user is registered for
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }

    // Method to load requested events for the user
    public void loadRequestedEvents(String userId, final EventIDDataStatus callback) {
        attendeeRef.child(userId).child("requestedEvents").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> eventIds = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    eventIds.add(snapshot.getKey()); // eventId
                }
                callback.DataLoaded(eventIds); // Return the list of eventIds the user has requested
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }

    //method to return the request status for a user in an event
    public void getEventRequestStatus(String userId, String eventId, EventRequestStatusCallback callback) {
        DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference("attendee").child("attendee_requests").child(userId).child("requestedEvents").child(eventId);
        requestRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Boolean isRequested = snapshot.getValue(Boolean.class);
                    if (isRequested != null) {
                        callback.onStatusLoaded(isRequested);
                    } else {
                        callback.onError(DatabaseError.fromException(new Exception("No status found")));
                    }
                } else {
                    callback.onError(DatabaseError.fromException(new Exception("Event request does not exist")));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error);
            }
        });
    }


    /*********************
     EVENT DELETION/EDITING METHODS
     ********************/
    public void deleteEvent(String eventId, writeCallback callback) {
        if (eventId != null) {
            eventsRef.child(eventId).child("people").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists() && snapshot.hasChildren()){
                        Log.e(TAG, "Event cannot be deleted because users have already joined.");
                        callback.onFailure(DatabaseError.fromException(new Exception("Event cannot be deleted because users have already joined.")));
                    } else {
                        eventsRef.child(eventId).removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Event deleted successfully");
                                callback.onSuccess();
                            } else {
                                Log.e(TAG, "Failed to delete event", task.getException());
                                callback.onFailure(DatabaseError.fromException(task.getException()));
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Failed to check if users have joined the event", error.toException());
                    callback.onFailure(error);
                }
            });

        } else {
            Log.e(TAG, "Event ID is null. Cannot delete event.");
        }
        callback.onFailure(DatabaseError.fromException(new Exception("Event ID is null")));
    }

    public void joinEvent(String eventId, String userId, writeCallback callback) {
        if (eventId != null && userId != null) {
            // Add user to the event's "people" list
            eventsRef.child(eventId).child("people").child(userId).setValue(true).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Add the event to the user's "registeredEvents"
                    attendeeRef.child(userId).child("registeredEvents").child(eventId).setValue(true).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Log.d(TAG, "User joined event successfully");
                            callback.onSuccess();
                        } else {
                            Log.e(TAG, "Failed to add event to user's registered events", task1.getException());
                            callback.onFailure(DatabaseError.fromException(task1.getException()));
                        }
                    });
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

    // Request event method - Update both "events" and "users" nodes
    public void requestEvent(String eventId, String userId, writeCallback callback) {
        if (eventId != null && userId != null) {
            // Add user to the event's "requests" list
            eventsRef.child(eventId).child("requests").child(userId).setValue(true).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Add the event to the user's "requestedEvents"
                    attendeeRef.child(userId).child("requestedEvents").child(eventId).setValue(true).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Log.d(TAG, "User requested event successfully");
                            callback.onSuccess();
                        } else {
                            Log.e(TAG, "Failed to add event to user's requested events", task1.getException());
                            callback.onFailure(DatabaseError.fromException(task1.getException()));
                        }
                    });
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

    // Remove user from the event's "people" list and user's "registeredEvents"
    public void leaveEvent(String eventId, String userId, writeCallback callback) {
        if (eventId != null && userId != null) {
            // Remove user from event's "people" list
            eventsRef.child(eventId).child("people").child(userId).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Remove event from user's "registeredEvents"
                    attendeeRef.child(userId).child("registeredEvents").child(eventId).removeValue().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Log.d(TAG, "User removed from event successfully");
                            callback.onSuccess();
                        } else {
                            Log.e(TAG, "Failed to remove event from user's registered events", task1.getException());
                            callback.onFailure(DatabaseError.fromException(task1.getException()));
                        }
                    });
                } else {
                    Log.e(TAG, "Failed to remove user from event", task.getException());
                    callback.onFailure(DatabaseError.fromException(task.getException()));
                }
            });
        } else {
            Log.e(TAG, "Event ID or User ID is null. Cannot remove user.");
            callback.onFailure(DatabaseError.fromException(new Exception("Event ID or User ID is null")));
        }
    }

    // Remove user from the event's "requests" list and user's "requestedEvents"
    public void removeUserFromRequests(String eventId, String userId, writeCallback callback) {
        if (eventId != null && userId != null) {
            // Remove user from event's "requests" list
            eventsRef.child(eventId).child("requests").child(userId).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Remove event from user's "requestedEvents"
                    attendeeRef.child(userId).child("requestedEvents").child(eventId).removeValue().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Log.d(TAG, "User removed from request list successfully");
                            callback.onSuccess();
                        } else {
                            Log.e(TAG, "Failed to remove event from user's requested events", task1.getException());
                            callback.onFailure(DatabaseError.fromException(task1.getException()));
                        }
                    });
                } else {
                    Log.e(TAG, "Failed to remove user from request list", task.getException());
                    callback.onFailure(DatabaseError.fromException(task.getException()));
                }
            });
        } else {
            Log.e(TAG, "Event ID or User ID is null. Cannot remove user.");
            callback.onFailure(DatabaseError.fromException(new Exception("Event ID or User ID is null")));
        }
    }

    // Remove user from user's "requestedEvents" only
    public void rejectUser(String eventId, String userId, writeCallback callback) {
        if (eventId != null && userId != null) {
            // Remove user from event's "requests" list
            eventsRef.child(eventId).child("requests").child(userId).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Remove event from user's "requestedEvents"
                    attendeeRef.child(userId).child("requestedEvents").child(eventId).setValue(false).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Log.d(TAG, "User removed from request list successfully");
                            callback.onSuccess();
                        } else {
                            Log.e(TAG, "Failed to remove event from user's requested events", task1.getException());
                            callback.onFailure(DatabaseError.fromException(task1.getException()));
                        }
                    });
                } else {
                    Log.e(TAG, "Failed to remove user from request list", task.getException());
                    callback.onFailure(DatabaseError.fromException(task.getException()));
                }
            });
        } else {
            Log.e(TAG, "Event ID or User ID is null. Cannot remove user.");
            callback.onFailure(DatabaseError.fromException(new Exception("Event ID or User ID is null")));
        }
    }
}