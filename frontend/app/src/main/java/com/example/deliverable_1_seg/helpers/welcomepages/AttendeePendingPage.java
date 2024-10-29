package com.example.deliverable_1_seg.helpers.welcomepages;

import android.content.Intent;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.deliverable_1_seg.user_actions.AdminRequestsActivity;
import com.example.deliverable_1_seg.MainActivity;
import com.example.deliverable_1_seg.R;


public class AttendeePendingPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_pending_screen);


    }
    public void logOut(View view){
        finish();
    }

}

