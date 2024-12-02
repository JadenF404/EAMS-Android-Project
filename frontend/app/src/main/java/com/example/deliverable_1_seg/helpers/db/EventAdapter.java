package com.example.deliverable_1_seg.helpers.db;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deliverable_1_seg.helpers.Organizer_After_login.ApproveAttendeesActivity;
import com.example.deliverable_1_seg.*;
import com.example.deliverable_1_seg.helpers.Organizer_After_login.EventListActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private ArrayList<Event> eventList;
    private Context context;
    private boolean manageEvents;
    private String userID;

    public EventAdapter(ArrayList<Event> eventList, AppCompatActivity eventListActivity) {
        this.manageEvents = eventListActivity instanceof EventListActivity;
        this.eventList = eventList;
        this.context = eventListActivity;

        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.textViewTitle.setText("Title: " + event.getTitle());
        holder.textViewDate.setText("Date: " + event.getDate());
        holder.textViewStartTime.setText("Start Time: " + event.getStartTime());
        holder.textViewEndTime.setText("End Time: " + event.getEndTime());
        holder.textViewDescription.setText("Description: " + event.getDescription());

        FirebaseEventHelper myEventHelper = new FirebaseEventHelper();

        if (manageEvents) {
            //manage requests button
            holder.buttonManageRequests.setOnClickListener(v -> {
                Intent intent = new Intent(context, ApproveAttendeesActivity.class);
                intent.putExtra("eventId", event.getEventId());
                context.startActivity(intent);
            });

            //delete event button
            holder.buttonDeleteEvent.setOnClickListener(v -> {
                FirebaseEventHelper eventHelper = new FirebaseEventHelper();

                eventHelper.deleteEvent(event.getEventId(), new FirebaseEventHelper.writeCallback() {
                    public void onSuccess() {
                        // Remove the deleted event from the list and notify the adapter
                        Toast.makeText(context, "Event deleted successfully!", Toast.LENGTH_SHORT).show();
                        eventList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, eventList.size());
                    }

                    @Override
                    public void onFailure(DatabaseError error) {
                        Toast.makeText(context, "Failed to delete event: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
        } else{
            // Check if the user is registered or has requested the event
            myEventHelper.loadRegisteredEvents(userID, new FirebaseEventHelper.EventIDDataStatus() {
                @Override
                public void DataLoaded(List<String> eventIds) {
                    // Check if the event is in registeredEvents
                    if (eventIds.contains(event.getEventId())) {
                        holder.textViewStatus.setText("Status: Joined");
                        holder.buttonManageRequests.setText("Leave Event");

                        //leave button
                        holder.buttonManageRequests.setOnClickListener(v ->{
                            myEventHelper.leaveEvent(event.getEventId(), userID, new FirebaseEventHelper.writeCallback() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(context, "Left Event Sucessfully!", Toast.LENGTH_SHORT).show();
                                    eventList.remove(position);
                                    notifyItemRemoved(position);
                                }

                                @Override
                                public void onFailure(DatabaseError error) {
                                    Toast.makeText(context, "Failed to leave eveent" + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        });

                    } else {
                        // Check if the event is in requestedEvents
                        myEventHelper.loadRequestedEvents(userID, new FirebaseEventHelper.EventIDDataStatus() {
                            @Override
                            public void DataLoaded(List<String> requestedEventIds) {
                                if (requestedEventIds.contains(event.getEventId())) {

                                    myEventHelper.getEventRequestStatus(userID, event.getEventId(), new FirebaseEventHelper.EventRequestStatusCallback() {
                                        @Override
                                        public void onStatusLoaded(boolean isRequested) {
                                            if (isRequested) {
                                                holder.textViewStatus.setText("Status: Requested");
                                            } else {
                                                holder.textViewStatus.setText("Status: Rejected");
                                            }
                                        }

                                        @Override
                                        public void onError(DatabaseError error) {
                                            holder.textViewStatus.setText("Status: Error");
                                        }
                                    });

                                    holder.buttonManageRequests.setText("Cancel Request");

                                    //Cancel Requests Button
                                    holder.buttonManageRequests.setOnClickListener(v -> {
                                        myEventHelper.removeUserFromRequests(event.getEventId(), userID, new FirebaseEventHelper.writeCallback() {
                                            @Override
                                            public void onSuccess() {
                                                Toast.makeText(context, "Request Cancelled Sucessfully!", Toast.LENGTH_SHORT).show();
                                                eventList.remove(position);
                                                notifyItemRemoved(position);
                                            }

                                            @Override
                                            public void onFailure(DatabaseError error) {
                                                Toast.makeText(context, "Failed to cancel request" + error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    });
                                } else {
                                    holder.textViewStatus.setText("Status: Not Joined");
                                    holder.buttonManageRequests.setText(event.isAutomaticApproval() ? "Join Event" : "Ask Permission");
                                }
                            }

                            @Override
                            public void onError(DatabaseError error) {
                                holder.textViewStatus.setText("Status: Error loading");
                            }
                        });
                    }
                }

                @Override
                public void onError(DatabaseError error) {
                    holder.textViewStatus.setText("Status: Error loading");
                }
            });


            //manage requests button
            FirebaseEventHelper eventHelper = new FirebaseEventHelper();
            holder.buttonManageRequests.setText(event.isAutomaticApproval() ? "Join Event": "Ask Permission");

            holder.buttonManageRequests.setOnClickListener(v -> {

                if (event.isAutomaticApproval()) {
                    // If automatic approval is true, add the user to the 'people' list
                    eventHelper.joinEvent(event.getEventId(), userID, new FirebaseEventHelper.writeCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(context, "Joined event successfully!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(DatabaseError error) {
                            Toast.makeText(context, "Failed to join event: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // If automatic approval is false, add the user to the 'requests' list
                    eventHelper.requestEvent(event.getEventId(), userID, new FirebaseEventHelper.writeCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(context, "Request sent to join event!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(DatabaseError error) {
                            Toast.makeText(context, "Failed to send request: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            holder.buttonDeleteEvent.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDate, textViewStartTime, textViewEndTime, textViewDescription, textViewStatus;
        Button buttonManageRequests, buttonDeleteEvent;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewName);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewStartTime = itemView.findViewById(R.id.textViewStartTime);
            textViewEndTime = itemView.findViewById(R.id.textViewEndTime);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);

            buttonManageRequests = itemView.findViewById(R.id.buttonAcceptRequest);
            buttonDeleteEvent = itemView.findViewById(R.id.buttonDeleteEvent);


        }
    }
}

