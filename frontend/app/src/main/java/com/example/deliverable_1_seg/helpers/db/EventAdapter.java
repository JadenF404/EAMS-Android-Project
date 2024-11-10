package com.example.deliverable_1_seg.helpers.db;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deliverable_1_seg.helpers.Organizer_After_login.ApproveAttendeesActivity;
import com.example.deliverable_1_seg.*;
import com.example.deliverable_1_seg.helpers.Organizer_After_login.EventListActivity;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private ArrayList<Event> eventList;
    private Context context;

    public EventAdapter(ArrayList<Event> eventList, EventListActivity eventListActivity) {

        this.eventList = eventList;
        this.context = eventListActivity;
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

        holder.textViewTitle.setText(event.getTitle());
        holder.textViewDate.setText(event.getDate());
        holder.textViewStartTime.setText(event.getStartTime());
        holder.textViewEndTime.setText(event.getEndTime());
        holder.textViewDescription.setText(event.getDescription());

        //manage requests button
        holder.buttonManageRequests.setOnClickListener(v -> {
            Intent intent = new Intent(context, ApproveAttendeesActivity.class);
            intent.putExtra("eventId", event.getEventId());
            context.startActivity(intent);
        });

        //delete event button
        holder.buttonDeleteEvent.setOnClickListener(v -> {
           //ToDo make delete event button using FirebaseEventHelper
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDate, textViewStartTime, textViewEndTime, textViewDescription;
        Button buttonManageRequests, buttonDeleteEvent;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewStartTime = itemView.findViewById(R.id.textViewStartTime);
            textViewEndTime = itemView.findViewById(R.id.textViewEndTime);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);

            buttonManageRequests = itemView.findViewById(R.id.buttonManageRequests);
            buttonDeleteEvent = itemView.findViewById(R.id.buttonDeleteEvent);


        }
    }
}

