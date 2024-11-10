package com.example.deliverable_1_seg.helpers.Organizer_After_login;
import com.example.deliverable_1_seg.R;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Create_Event extends AppCompatActivity {

    private EditText editTextDate, editTextStartTime, editTextEndTime;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);  // Use the correct layout file

        // Initialize views
        editTextDate = findViewById(R.id.editTextDate);
        editTextStartTime = findViewById(R.id.editTextStartTime);
        editTextEndTime = findViewById(R.id.editTextEndTime);
        MaterialButton buttonSubmit = findViewById(R.id.buttonSubmitOrg);

        // Initialize calendar
        calendar = Calendar.getInstance();

        // Set up the Date Picker
        editTextDate.setOnClickListener(v -> showDatePickerDialog());

        // Set up the Start Time Picker
        editTextStartTime.setOnClickListener(v -> showTimePickerDialog(editTextStartTime));

        // Set up the End Time Picker
        editTextEndTime.setOnClickListener(v -> showTimePickerDialog(editTextEndTime));

        // Handle the Submit button
        buttonSubmit.setOnClickListener(v -> {
            String date = editTextDate.getText().toString();
            String startTime = editTextStartTime.getText().toString();
            String endTime = editTextEndTime.getText().toString();
            String eventTitle = ((TextInputEditText) findViewById(R.id.editTextEventTitle)).getText().toString();

            // Validation to ensure fields are filled in
            if (eventTitle.isEmpty()) {
                Toast.makeText(this, "Please enter an event title", Toast.LENGTH_SHORT).show();
            } else if (date.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
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
                        // Successfully created event
                        Toast.makeText(this, "Event created successfully!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error parsing time", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Show DatePickerDialog with past dates disabled
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            editTextDate.setText(dateFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        // Disable past dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    // Show TimePickerDialog with 30-minute increments
    private void showTimePickerDialog(EditText timeEditText) {
        Calendar timeCalendar = Calendar.getInstance();
        int hour = timeCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = (timeCalendar.get(Calendar.MINUTE) / 30) * 30; // Round to nearest 30 minutes

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
            // Adjust minute to be in 30-minute increments
            selectedMinute = (selectedMinute / 30) * 30;
            timeCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
            timeCalendar.set(Calendar.MINUTE, selectedMinute);

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            timeEditText.setText(timeFormat.format(timeCalendar.getTime()));
        }, hour, minute, true);

        timePickerDialog.show();
    }
}
