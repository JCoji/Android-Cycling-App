package com.example.segfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventCreator extends AppCompatActivity {

    EditText title;
    EditText age;
    EditText level;
    EditText pace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creator);

        title = (EditText) findViewById(R.id.Register_title);
        age = (EditText) findViewById(R.id.EventSize);
        level = (EditText) findViewById(R.id.EventFee);
        pace = (EditText) findViewById(R.id.Register_pace);
    }


    public void createButtonOnClick(View view){
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("events");

        eventsRef.child(title.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        Toast.makeText(getApplicationContext(), "Event already exists", Toast.LENGTH_LONG).show();
                    }else{
                        if (!isTitleValid(title)) {
                            Toast.makeText(getApplicationContext(), "Event Title Invalid", Toast.LENGTH_LONG).show();
                        } else if (!isAgeValid(age)) {
                            Toast.makeText(getApplicationContext(), "Event Age Invalid", Toast.LENGTH_LONG).show();
                        } else if (!isLevelValid(level)) {
                            Toast.makeText(getApplicationContext(), "Event Level Invalid", Toast.LENGTH_LONG).show();
                        } else if (!isPaceValid(pace)) {
                            Toast.makeText(getApplicationContext(), "Event Pace Invalid", Toast.LENGTH_LONG).show();
                        } else {
                            Event newEvent;

                            newEvent = new Event(title.getText().toString(), Integer.parseInt(age.getText().toString()), level.getText().toString(), Integer.parseInt(pace.getText().toString()));
                            Intent intent = new Intent(getApplicationContext(), EventManager.class);
                            uploadEvent(newEvent);
                            startActivity(intent);
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Check failed", Toast.LENGTH_LONG).show();
                }
            }
        });
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

    public void uploadEvent(Event event) {
        FirebaseDatabase dB = FirebaseDatabase.getInstance();

        DatabaseReference ageDR = dB.getReference("events/" + event.getTitle() + "/age");
        DatabaseReference levelDR = dB.getReference("events/" + event.getTitle() + "/level");
        DatabaseReference paceDR = dB.getReference("events/" + event.getTitle() + "/pace");

        ageDR.setValue(event.getAge());
        levelDR.setValue(event.getLevel());
        paceDR.setValue(event.getPace());

    }
}