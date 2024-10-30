package com.example.deliverable_1_seg.helpers.db;

import android.content.Context;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import android.util.Base64;
import java.util.List;
import java.util.function.Consumer;
import com.example.deliverable_1_seg.BuildConfig;



public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestViewHolder> {

    private List<RegistrationRequest> requestList;
    private Context context;
    private boolean isRejectedRequestsMode;

    public RequestsAdapter(Context context, List<RegistrationRequest> requestList, boolean isRejectedRequestsMode) {
        this.context = context;
        this.requestList = requestList;
        this.isRejectedRequestsMode = isRejectedRequestsMode;
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
        if (isRejectedRequestsMode) {

            //hide rejected button
            holder.rejectButton.setVisibility(View.GONE);
            holder.approveButton.setText("Re-Approve");

            holder.approveButton.setOnClickListener(v -> {
                updateRequestStatus(request.getUserId(), request.getUserType(), "approved", isSuccessful -> {
                if (isSuccessful) {
                    requestList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition()); // Notify adapter of item removal
                    notifyItemRangeChanged(holder.getAdapterPosition(), requestList.size()); // Update the remaining items
                    sendConfirmationEmail(request.getEmail());
                }
                });
            });

        } else {
            // Approve button click handler
            holder.approveButton.setOnClickListener(v -> {
                updateRequestStatus(request.getUserId(), request.getUserType(), "approved", isSuccessful -> {
                    //realistically we could have the callback run here but this seems much less encapsulated
                    if (isSuccessful) {
                        requestList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition()); // Notify adapter of item removal
                        notifyItemRangeChanged(holder.getAdapterPosition(), requestList.size()); // Update the remaining items
                        sendConfirmationEmail(request.getEmail());
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

    }

    public void sendConfirmationEmail(String email){
        if(!BuildConfig.IS_PRODUCTION){
            if (!(BuildConfig.API_KEY.equals("null") || BuildConfig.API_SECRET.equals("null"))) {
                Toast.makeText(context, "Sending Email: " + BuildConfig.API_KEY + " " + BuildConfig.API_SECRET, Toast.LENGTH_SHORT).show();


                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                    // setting up the http request, must be a POST request
                    URL url = new URL("https://api.mailjet.com/v3.1/send");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    // Authentication Header --> how it lets u use API
                    String auth = BuildConfig.API_KEY + ":" + BuildConfig.API_SECRET;
                    String encodedAuth = Base64.encodeToString(auth.getBytes("UTF-8"), Base64.NO_WRAP);
                    connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
                    connection.setRequestProperty("Content-Type", "application/json");


                    JSONArray messagesArray = new JSONArray();

                    // Creating JSON to be interpreted as string later
                    JSONObject from = new JSONObject();
                    from.put("Email", "msuxo021@uottawa.ca");
                    from.put("Name", "Event Management");


                    JSONObject to = new JSONObject();
                    to.put("Email", email);

                    JSONObject message = new JSONObject();
                    message.put("From", from);
                    message.put("To", new JSONArray().put(to)); // Add 'To' to an array
                    message.put("Subject", "Success your signup has been approved!");
                    message.put("TextPart", "Dear User, welcome to our management system!");
                    message.put("HTMLPart", "<h3>Dear User, welcome to our management system</h3><br />Your signup request was accepted, please proceed with login!");

                    messagesArray.put(message);

                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("Messages", messagesArray);

                    // Write JSON  to output stream
                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] input = jsonBody.toString().getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }



                    // Get the response to see if it send
                    int status = connection.getResponseCode();

//                Toast.makeText(context, "Send result: ", Toast.LENGTH_SHORT).show();
                    BufferedReader in;
                    if (status > 299) {
                        in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    } else {
                        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    }
                    String responseLine;
                    StringBuilder response = new StringBuilder();
                    while ((responseLine = in.readLine()) != null) {
                        response.append(responseLine);
                    }
                    in.close();


                } catch (Exception e) {
//                Toast.makeText(context, "Failed Sending email" + e.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("NOTE", "Failed to send email", e);
                }
                    }
                });



                thread.start();
            } else {
                Toast.makeText(context, "Dev: Please setup api key", Toast.LENGTH_SHORT).show();
            }
        } else{
                Toast.makeText(context, "Sending Email", Toast.LENGTH_SHORT).show();
        }



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
