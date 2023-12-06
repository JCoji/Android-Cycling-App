package com.example.segfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class rating_activity extends AppCompatActivity {

    Spinner clubSpinner;
    EditText scoreText, descText;
    String username, score, desc, club;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        clubSpinner = (Spinner) findViewById(R.id.rateClubSpinner);

        scoreText = (EditText) findViewById(R.id.score_editText);
        descText = (EditText) findViewById(R.id.decription_editText);


        //Storing username from user activity page.
        Bundle extras = getIntent().getExtras();
        username = extras.getString("Username");
    }

    protected void onStart() {
        super.onStart();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fDatabaseRoot = database.getReference();

        fDatabaseRoot.child("users/" + username + "/Clubs/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> eventTypeNames = new ArrayList<String>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String eventName = postSnapshot.getKey().toString();

                    if (eventName != null) {
                        eventTypeNames.add(eventName);
                    }
                }

                clubSpinner = (Spinner) findViewById(R.id.rateClubSpinner);
                ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(rating_activity.this, android.R.layout.simple_spinner_item, eventTypeNames);
                addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                clubSpinner.setAdapter(addressAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Check if the received score is in between 1 and 5, and if it is not empty.
    public Boolean correctScore(String score){
        if (!score.isEmpty()){
            int scoreNum = Integer.parseInt(score);

            if (scoreNum <= 5 && scoreNum >= 1){
                return true;
            }
        }
        return false;
    }

    //Check if there is a description.
    public Boolean correctDesc(String desc){
        if (!desc.isEmpty()){
            return true;
        }
        return false;
    }

    public void submitBtnOnClick(View view) {
        club = clubSpinner.getSelectedItem().toString();
        score = scoreText.getText().toString();
        desc = descText.getText().toString();

        if (!correctScore(score)){//Invalid score
            Toast.makeText(rating_activity.this, "INVALID SCORE", Toast.LENGTH_SHORT).show();
        }
        else if (!correctDesc(desc)){//Missing description
            Toast.makeText(rating_activity.this, "MISSING DESCRIPTION", Toast.LENGTH_SHORT).show();
        }
        else{
            FirebaseDatabase dbRef = FirebaseDatabase.getInstance();

            DatabaseReference newRatingName = dbRef.getReference("clubs/" +  club +  "/ratings/" + username);
            DatabaseReference newRatingScore = dbRef.getReference("clubs/" +  club +  "/ratings/" + username + "/scores/");
            DatabaseReference newRatingDesc = dbRef.getReference("clubs/" +  club +  "/ratings/" + username + "/descriptions/");

            newRatingName.setValue(username);
            newRatingScore.setValue(score);
            newRatingDesc.setValue(desc);
        }
    }

    public void returnBtnOnClick(View view) {
        Bundle extras = getIntent().getExtras();
        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
        intent.putExtra("Username", extras.getString("Username"));
        startActivity(intent);
    }
}
