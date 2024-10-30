package com.example.deliverable_1_seg.helpers.welcomepages;

import android.content.Intent;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.deliverable_1_seg.user_actions.AdminRejectedRequestsActivity;
import com.example.deliverable_1_seg.user_actions.AdminRequestsActivity;
import com.example.deliverable_1_seg.MainActivity;
import com.example.deliverable_1_seg.R;


public class AdministratorWelcomePage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrator_welcome_page);

        //manage requests button
        Button manageRequestsButton = findViewById(R.id.manageRequestsButton);
        Button manageRejectedRequestsButton = findViewById(R.id.manageRejectedRequestsButton);


        //log out button
        Button administratorButton = findViewById(R.id.AdminLogOut_button);

        administratorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdministratorWelcomePage.this, MainActivity.class);
                startActivity(intent);
            }
        });

       manageRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdministratorWelcomePage.this, AdminRequestsActivity.class);
                startActivity(intent);
            }
        });

        manageRejectedRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdministratorWelcomePage.this, AdminRejectedRequestsActivity.class);
                startActivity(intent);
            }
        });
    }
    public void logOut(View view){
        finish();
    }

}

