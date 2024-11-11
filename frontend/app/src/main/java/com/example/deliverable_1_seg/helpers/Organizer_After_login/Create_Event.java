package com.example.deliverable_1_seg.helpers.Organizer_After_login;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deliverable_1_seg.R;
import com.example.deliverable_1_seg.FirebaseEventHelper;
import com.example.deliverable_1_seg.helpers.db.Event;
import com.example.deliverable_1_seg.helpers.welcomepages.OrganizerWelcomePage;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Create_Event extends AppCompatActivity {

    private static ArrayList<Event> eventList = new ArrayList<>();

    private EditText editTextDate, editTextStartTime, editTextEndTime;
    private Calendar calendar;
    private CalendarView calendarView;
//    private EditText editTextStartTime, editTextEndTime;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        // Initialize views
        calendarView = findViewById(R.id.calendarView);
        editTextStartTime = findViewById(R.id.editTextStartTime);
        editTextEndTime = findViewById(R.id.editTextEndTime);
        MaterialButton buttonSubmit = findViewById(R.id.buttonSubmitOrg);

        // Initialize calendar and set the default date
        Calendar calendar = Calendar.getInstance();
        selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());

        // Set CalendarView to current date
        calendarView.setDate(System.currentTimeMillis(), false, true);

        // Listen for date changes on the CalendarView
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Update the selectedDate when the user picks a new date
            calendar.set(year, month, dayOfMonth);
            selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
        });

        // Set up the Start Time Picker
        editTextStartTime.setOnClickListener(v -> showTimePickerDialog(editTextStartTime));

        // Set up the End Time Picker
        editTextEndTime.setOnClickListener(v -> showTimePickerDialog(editTextEndTime));

        // Handle the Submit button
        buttonSubmit.setOnClickListener(v -> {
            System.out.println("Event Submitted");

            String startTime = editTextStartTime.getText().toString();
            String endTime = editTextEndTime.getText().toString();
            String eventTitle = ((android.widget.EditText) findViewById(R.id.editTextEventTitle)).getText().toString();
            String description = ((TextInputEditText) findViewById(R.id.editTextDescription)).getText().toString();

            // Validation to ensure fields are filled in
            if (eventTitle.isEmpty()) {
                Toast.makeText(this, "Please enter an event title", Toast.LENGTH_SHORT).show();
            } else if (startTime.isEmpty() || endTime.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Additional validation for start time before end time
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                try {
                    Date start = sdf.parse(startTime);
                    Date end = sdf.parse(endTime);
                    if (start != null && end != null && start.after(end)) {
                        Toast.makeText(this, "Start time cannot be later than end time", Toast.LENGTH_SHORT).show();
                    } else {
                        // Successfully created event, add and save to Firebase
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            String organizerID = user.getUid();
                            Event event = new Event(null, eventTitle, selectedDate, startTime, endTime, description, organizerID);

                            FirebaseEventHelper eventHelper = new FirebaseEventHelper();
                            eventHelper.addEvent(event, new FirebaseEventHelper.writeCallback() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(Create_Event.this, "Event created successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Create_Event.this, OrganizerWelcomePage.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure(DatabaseError error) {
                                    Toast.makeText(Create_Event.this, "Failed to create event: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(Create_Event.this, "No user logged in", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error parsing time", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Show TimePickerDialog with 30-minute increments
    private void showTimePickerDialog(EditText timeEditText) {
        Calendar timeCalendar = Calendar.getInstance();
        int hour = timeCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = (timeCalendar.get(Calendar.MINUTE) / 30) * 30;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
            selectedMinute = (selectedMinute / 30) * 30;
            timeCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
            timeCalendar.set(Calendar.MINUTE, selectedMinute);

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            timeEditText.setText(timeFormat.format(timeCalendar.getTime()));
        }, hour, minute, true);

        timePickerDialog.show();
    }
}
