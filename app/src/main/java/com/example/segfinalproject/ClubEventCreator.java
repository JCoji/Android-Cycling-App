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

public class ClubEventCreator extends AppCompatActivity {

    //Store and pass through club Username so can store in correct FB
    String clubName;
    Event selectedEventType;
    Spinner eventSpinner;
    String selectedEventName;
    EditText memberNum;
    EditText eventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_event_creator);

        eventSpinner = (Spinner) findViewById(R.id.EventTypeSpinner);
        memberNum = (EditText) findViewById(R.id.Max_mem_txt);
        eventName = (EditText) findViewById(R.id.name_txt);

        //Stores name of club, passed from prev. activity
        Bundle extras = getIntent().getExtras();
        clubName = extras.getString("ClubName");
    }

    public void BackButtonOnClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ClubActivity.class);
        startActivity(intent);
    }

    protected void onStart() {
        super.onStart();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fDatabaseRoot = database.getReference();

        fDatabaseRoot.child("events").addListenerForSingleValueEvent(new ValueEventListener() {
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

                Spinner spinnerProperty = (Spinner) findViewById(R.id.EventTypeSpinner);
                ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(ClubEventCreator.this, android.R.layout.simple_spinner_item, eventTypeNames);
                addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProperty.setAdapter(addressAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void storeSelectedSpinnerValue(){


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fDatabaseRoot = database.getReference();

        fDatabaseRoot.child("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> eventTypeNames = new ArrayList<String>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String eventName = postSnapshot.getKey().toString();


                    if(eventName.equals(selectedEventName)){

                        selectedEventType = new Event(eventName, Integer.parseInt(postSnapshot.child("age").getValue().toString()), postSnapshot.child("level").getValue().toString(), Integer.parseInt(postSnapshot.child("pace").getValue().toString()));

                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void SubmitOnClick(View view){
        selectedEventName = eventSpinner.getSelectedItem().toString();
        storeSelectedSpinnerValue();

        ClubEvent newCE = new ClubEvent(selectedEventType, eventName.getText().toString(), Integer.parseInt(memberNum.getText().toString()));
        uploadClubEvent(newCE);

    }

    private void uploadClubEvent(ClubEvent event) {
        FirebaseDatabase dbRef = FirebaseDatabase.getInstance();

        DatabaseReference newEventTypeRef = dbRef.getReference("clubs/" + clubName +  "/events/" + event.getName() + "/type");
        DatabaseReference newEventSizeRef = dbRef.getReference("clubs/" + clubName +  "/events/" + event.getName() + "/size");

        newEventTypeRef.setValue(selectedEventName);
        newEventSizeRef .setValue(event.getSize());
    }
}