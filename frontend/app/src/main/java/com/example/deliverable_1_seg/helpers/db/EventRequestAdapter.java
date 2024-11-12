package com.example.deliverable_1_seg.helpers.db;
import static android.content.Intent.getIntent;
import static android.content.Intent.parseUri;

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

import java.util.ArrayList;

public class EventRequestAdapter extends RecyclerView.Adapter<EventRequestAdapter.RequestViewHolder> {

    private ArrayList<String> requestList;
    private Context context;
    private String userID;
    private String eventID;

    public EventRequestAdapter(ArrayList<String> requestList, AppCompatActivity eventListActivity) {
        Bundle resultIntent = getIntent().getExtras();
        eventID = resultIntent.getString("eventId");

        this.requestList = requestList;
        this.context = eventListActivity;

        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        FirebaseEventHelper eventHelper = new FirebaseEventHelper();

        String request = requestList.get(position);


        holder.textViewName.setText(request);

            //manage requests button
            holder.buttonAcceptRequest.setOnClickListener(v -> {
                Intent intent = new Intent(context, EventRequestAdapter.class);
                eventHelper.removeUserFromRequests(request, eventID, new FirebaseEventHelper.writeCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(context, "Joined event successfully!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(DatabaseError error) {
                        Toast.makeText(context, "Failed to join event: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                eventHelper.joinEvent(request, eventID, new FirebaseEventHelper.writeCallback() {
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
                context.startActivity(intent);
            });


    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        Button buttonAcceptRequest;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);

            buttonAcceptRequest = itemView.findViewById(R.id.buttonAcceptRequest);


        }
    }
}

