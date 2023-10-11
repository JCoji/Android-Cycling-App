package com.example.segfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class UserActivity extends AppCompatActivity {


    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Bundle extras = getIntent().getExtras();

        username = (EditText) findViewById(R.id.userText);

        username.setText("Welcome: " + extras.getString("Username"));
    }
}