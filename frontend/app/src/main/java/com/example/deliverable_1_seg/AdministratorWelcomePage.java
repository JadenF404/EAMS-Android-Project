package com.example.deliverable_1_seg;

import android.content.Intent;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;



public class AdministratorWelcomePage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrator_welcome_page);


        //log out button
        Button administratorButton = findViewById(R.id.AdminLogOut_button);
        administratorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdministratorWelcomePage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void logOut(View view){
        finish();
    }

}

