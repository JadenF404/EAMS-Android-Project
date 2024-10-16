package com.example.deliverable_1_seg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AttendeeLoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_login_screen); // Link to attendee_login_form.xml


        // Find the Attendee button and set its click listener
        Button ForgotPassword_bttn = findViewById(R.id.ForgotPassword_bttn);

        ForgotPassword_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch AttendeeLoginActivity when button is clicked
                Intent intent = new Intent(AttendeeLoginActivity.this, AttendeePasswordChange.class);
                startActivity(intent);
            }

        });



    }


    // Method to switch to the signup form
    public void switchToSignupAttendee(View view) {
        setContentView(R.layout.attendee_signup_form); // Link to attendee_signup_form.xml
    }



    public void notAnAttendee(View view){
        finish();
    }


    public void loginAttendee(View view){
        //Todo MILLER: ADD THE CODE FOR ATTENDEE LOGIN HERE
    }

    public void signupAttendee(View view){

    }






    //Back button to go back to login page
    public void backAttendeeForm (View view) {
        finish();
    }


}
