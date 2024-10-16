package com.example.deliverable_1_seg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdministratorLoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrator_login_screen); // Link to administrator_login_screen.xml

    }

    public void notAnAdmin(View view){

        finish();
    }


    public void loginAdmin(View view){
        //Todo MILLER: ADD THE CODE FOR ATTENDEE LOGIN HERE
    }



    //Back button to go back to login page
    public void backAttendeeForm (View view) {
        finish();
    }
}
