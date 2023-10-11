package com.example.segfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class ClubActivity extends AppCompatActivity {

    EditText clubname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        Bundle extras = getIntent().getExtras();

        clubname = (EditText) findViewById(R.id.clubText);

        clubname.setText("Welcome: " + extras.getString("Username"));
    }
}