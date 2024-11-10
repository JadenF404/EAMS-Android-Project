package com.example.deliverable_1_seg.helpers.db;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deliverable_1_seg.R;
import com.example.deliverable_1_seg.helpers.db.RegistrationRequest;

import java.util.List;

public class PendingAttendeeAdapter extends RecyclerView.Adapter<PendingAttendeeAdapter.PendingAttendeeViewHolder> {

    private final List<RegistrationRequest> attendeeList;
    private final Context context;

    public PendingAttendeeAdapter(List<RegistrationRequest> attendeeList, Context context) {
        this.attendeeList = attendeeList;
        this.context = context;
    }

    @NonNull
    @Override
    public PendingAttendeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pending_attendee, parent, false);
        return new PendingAttendeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingAttendeeViewHolder holder, int position) {
        RegistrationRequest attendee = attendeeList.get(position);

        holder.textViewFirstName.setText(attendee.getFirstName());
        holder.textViewLastName.setText(attendee.getLastName());
        holder.textViewEmail.setText(attendee.getEmail());
        holder.textViewAddress.setText(attendee.getAddress());
        holder.textViewPhoneNumber.setText(attendee.getPhoneNumber());
    }


    @Override
    public int getItemCount() {
        return attendeeList.size();
    }

    public static class PendingAttendeeViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFirstName, textViewLastName, textViewAddress, textViewPhoneNumber, textViewEmail;
        Button buttonApprove, buttonReject;

        public PendingAttendeeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFirstName = itemView.findViewById(R.id.textViewFirstName);
            textViewLastName = itemView.findViewById(R.id.textViewLastName);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);

            buttonApprove = itemView.findViewById(R.id.buttonApprove);
            buttonReject = itemView.findViewById(R.id.buttonReject);
        }
    }
}
