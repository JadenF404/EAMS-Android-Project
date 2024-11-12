package com.example.deliverable_1_seg.helpers.db;
import static android.content.Intent.getIntent;

import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class EventRequestAdapter extends RecyclerView.Adapter<EventRequestAdapter.RequestViewHolder> {

    private List<RegistrationRequest> requestList;
    private Context context;
    private String userID;
    private String eventID;

    public EventRequestAdapter(List<RegistrationRequest> requestList, AppCompatActivity eventListActivity, String eventID) {


        this.requestList = requestList;
        this.context = eventListActivity;
        this.eventID = eventID;
        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_attendee, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        FirebaseEventHelper eventHelper = new FirebaseEventHelper();

        RegistrationRequest request = requestList.get(position);

        // Display each user's details
        holder.textViewFirstName.setText(request.getFirstName());
        holder.textViewLastName.setText(request.getLastName());
        holder.textViewEmail.setText(request.getEmail());
        holder.textViewPhone.setText(request.getPhoneNumber());
        holder.textViewAddress.setText(request.getAddress());

            //manage requests button
            holder.buttonAcceptRequest.setOnClickListener(v -> {
                String userID = request.getUserId();

                //Intent intent = new Intent(context, EventRequestAdapter.class);
                eventHelper.removeUserFromRequests(eventID, userID, new FirebaseEventHelper.writeCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(context, "Joined event successfully!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(DatabaseError error) {
                        Toast.makeText(context, "Failed to join event: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                eventHelper.joinEvent(eventID, userID, new FirebaseEventHelper.writeCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(context, "Joined event successfully!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(DatabaseError error) {
                        Toast.makeText(context, "Failed to join event: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
//                intent.putExtra("eventId", event.getEventId());
                //context.startActivity(intent);
            });

            //reject button
        holder.buttonRejectRequest.setOnClickListener(v ->{
            String userID = request.getUserId();

            // Remove the user from the requests list in Firebase (rejecting the request)
            eventHelper.removeUserFromRequests(eventID, userID, new FirebaseEventHelper.writeCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(context, "User request rejected successfully!", Toast.LENGTH_SHORT).show();

                    //remove the item from the list in the adapter and notify the adapter to refresh the view
                    requestList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, requestList.size());
                }

                @Override
                public void onFailure(DatabaseError error) {
                    Toast.makeText(context, "Failed to reject user request: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
            //reject button
        holder.buttonRejectRequest.setOnClickListener(v ->{
            String userID = request.getUserId();

            // Remove the user from the requests list in Firebase (rejecting the request)
            eventHelper.removeUserFromRequests(eventID, userID, new FirebaseEventHelper.writeCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(context, "User request rejected successfully!", Toast.LENGTH_SHORT).show();

                    //remove the item from the list in the adapter and notify the adapter to refresh the view
                    requestList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, requestList.size());
                }

                @Override
                public void onFailure(DatabaseError error) {
                    Toast.makeText(context, "Failed to reject user request: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFirstName, textViewLastName;
        TextView textViewEmail;
        TextView textViewPhone;
        TextView textViewAddress;
        Button buttonAcceptRequest, buttonRejectRequest;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFirstName = itemView.findViewById(R.id.textViewFirstName);
            textViewLastName = itemView.findViewById(R.id.textViewLastName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewPhone = itemView.findViewById(R.id.textViewPhoneNumber);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            buttonAcceptRequest = itemView.findViewById(R.id.buttonApprove);
            buttonRejectRequest = itemView.findViewById(R.id.buttonReject);

        }
    }
}

