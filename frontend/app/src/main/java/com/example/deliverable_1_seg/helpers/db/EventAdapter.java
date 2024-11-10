package com.example.deliverable_1_seg.helpers.db;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deliverable_1_seg.helpers.Organizer_After_login.Create_Event;
import com.example.deliverable_1_seg.*;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private ArrayList<Create_Event.Event> eventList;

    public EventAdapter(ArrayList<Create_Event.Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Create_Event.Event event = eventList.get(position);
        holder.textViewTitle.setText(event.getTitle());
        holder.textViewDate.setText(event.getDate());
        holder.textViewStartTime.setText(event.getStartTime());
        holder.textViewEndTime.setText(event.getEndTime());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDate, textViewStartTime, textViewEndTime;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewStartTime = itemView.findViewById(R.id.textViewStartTime);
            textViewEndTime = itemView.findViewById(R.id.textViewEndTime);
        }
    }
}

