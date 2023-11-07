package com.example.segfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void AccountManagerOnClick(View view){
        Intent intent = new Intent(getApplicationContext(), ClubManager.class);
        startActivity(intent);
    }

    public void EventManagerOnClick(View view){
        //Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
        //startActivity(intent);
    }
}