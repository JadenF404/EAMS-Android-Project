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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.function.Consumer;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestViewHolder> {

    private List<RegistrationRequest> requestList;
    private Context context;

    public RequestsAdapter(Context context, List<RegistrationRequest> requestList) {
        this.context = context;
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        RegistrationRequest request = requestList.get(position);
        holder.requestDetailsTextView.setText(
                "First Name: " + request.getFirstName() + "\n" +
                        "Last Name: " + request.getLastName() + "\n" +
                        "Email: " + request.getEmail() + "\n" +
                        "Phone: " + request.getPhoneNumber() + "\n" +
                        "Address: " + request.getAddress() + "\n" +
                        (request.getOrganization() != null ? "Organization: " + request.getOrganization() + "\n" : "")
        );

        // Approve button click handler
        holder.approveButton.setOnClickListener(v -> {

            updateRequestStatus(request.getUserId(), request.getUserType(), "approved", isSuccessful -> {
                //realistically we could have the callback run here but this seems much less encapsulated
                if (isSuccessful) {
                    requestList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition()); // Notify adapter of item removal
                    notifyItemRangeChanged(holder.getAdapterPosition(), requestList.size()); // Update the remaining items
                }

            });
        });

        // Reject button click handler
        holder.rejectButton.setOnClickListener(v -> {
            updateRequestStatus(request.getUserId(), request.getUserType(), "rejected", isSuccessful -> {
                //realistically we could have the callback run here but this seems much less encapsulated
                if (isSuccessful) {
                    requestList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition()); // Notify adapter of item removal
                    notifyItemRangeChanged(holder.getAdapterPosition(), requestList.size()); // Update the remaining items
                }

            });
        });

    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView requestDetailsTextView;
        Button approveButton, rejectButton;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            requestDetailsTextView = itemView.findViewById(R.id.requestDetailsTextView);
            approveButton = itemView.findViewById(R.id.approveButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }
    }

    // to fix later, using a callback to delete the item on success ? very scuffed
    private void updateRequestStatus(String requestId, String userType, String status, Consumer<Boolean> callback) {
//        Toast.makeText(context, userType + "/" + userType + "_requests/" + requestId, Toast.LENGTH_SHORT).show();
        DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference(userType + "/" + userType + "_requests/").child(requestId);
        requestRef.child("status").setValue(status).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Request " + status, Toast.LENGTH_SHORT).show();
                callback.accept(true);
            } else {
                Toast.makeText(context, "Failed to update status.", Toast.LENGTH_SHORT).show();
                callback.accept(false);
            }
        }
        );
    }

    // New method to add requests to the adapter
    public void addRequests(List<RegistrationRequest> newRequests) {
        int startPosition = requestList.size();
        requestList.addAll(newRequests); // Add new requests to the list
        notifyItemRangeInserted(startPosition, newRequests.size()); // Notify adapter about new items
    }

    // Optional: Method to clear and update the request list
    public void updateRequests(List<RegistrationRequest> newRequests) {
        requestList.clear(); // Clear old requests
        requestList.addAll(newRequests); // Add new requests
        notifyDataSetChanged(); // Notify adapter of data changes
    }
}
