package com.example.segfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateEvent extends AppCompatActivity {

    EditText title;
    EditText age;
    EditText level;
    EditText pace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);

        title = (EditText) findViewById(R.id.Register_title);
        age = (EditText) findViewById(R.id.Register_age);
        level = (EditText) findViewById(R.id.Register_level);
        pace = (EditText) findViewById(R.id.Register_pace);
    }

    public void confirmButtonOnClick(View view) {
        Event newEvent;

        if (!isTitleValid(title)) {
            Toast.makeText(getApplicationContext(), "Event Title Invalid", Toast.LENGTH_LONG).show();
        } else if (!isAgeValid(age)) {
            Toast.makeText(getApplicationContext(), "Event Age Invalid", Toast.LENGTH_LONG).show();
        } else if (!isLevelValid(level)) {
            Toast.makeText(getApplicationContext(), "Event Level Invalid", Toast.LENGTH_LONG).show();
        } else if (!isPaceValid(pace)) {
            Toast.makeText(getApplicationContext(), "Event Pace Invalid", Toast.LENGTH_LONG).show();
        } else {
            newEvent = new Event(title.getText().toString(), Integer.parseInt(age.getText().toString()), level.getText().toString(), Integer.parseInt(pace.getText().toString()));
            Intent intent = new Intent(getApplicationContext(), EventManager.class);

            DatabaseReference dR = FirebaseDatabase.getInstance().getReference("events").child(title.toString());

            Event editedEvent = new Event(title.toString(), Integer.parseInt(age.toString()), level.toString(), Integer.parseInt(pace.toString()));
            dR.setValue(editedEvent);
            Toast.makeText(getApplicationContext(), "Event Updated", Toast.LENGTH_LONG).show();
            startActivity(intent);

        }
    }

    public boolean isTitleValid(EditText title){
        String titleInput = title.getText().toString();

        return !(titleInput.isEmpty());
    }



    public boolean isAgeValid(EditText age){
        String ageInput = age.getText().toString();

        //Checks if the age input is an integer
        try {
            int ageInt = Integer.parseInt(ageInput);
        } catch (NumberFormatException e) {
            return false;
        }

        //Checks if the EditText contains anything
        return !(ageInput.isEmpty());
    }

    public boolean isLevelValid(EditText level){
        String levelInput = level.getText().toString();

        return !(levelInput.isEmpty());
    }

    public boolean isPaceValid(EditText pace){
        String paceInput = pace.getText().toString();

        //Checks if the pace input is an integer
        try {
            int paceInt = Integer.parseInt(paceInput);
        } catch (NumberFormatException e) {
            return false;
        }

        //Checks if the EditText contains anything
        return !(paceInput.isEmpty());
    }

    public void backButtonOnClick(View view){
        Intent intent = new Intent(getApplicationContext(), EventManager.class);
        startActivity(intent);
    }
}