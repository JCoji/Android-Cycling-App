package com.example.segfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClubEventEditor extends AppCompatActivity {

    //Store and pass through club Username so can store in correct FB
    String clubName;
    Event selectedEventType;
    Spinner eventSpinner;
    String selectedEventName;
    EditText memberNum;
    EditText eventName;
    EditText eventFee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_event_editor);

        eventSpinner = (Spinner) findViewById(R.id.EventTypeSpinner);
        memberNum = (EditText) findViewById(R.id.Max_mem_txt);
        eventName = (EditText) findViewById(R.id.name_txt);
        eventFee = (EditText) findViewById(R.id.fee_txt);

        //Stores name of club, passed from prev. activity
        Bundle extras = getIntent().getExtras();
        clubName = extras.getString("ClubName");
    }

    public void backButtonOnClick(View view) {
        Bundle extras = getIntent().getExtras();
        Intent intent = new Intent(getApplicationContext(), ClubActivity.class);
        intent.putExtra("Username", extras.getString("ClubName"));
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
                ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(ClubEventEditor.this, android.R.layout.simple_spinner_item, eventTypeNames);
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
                        Toast.makeText(getApplicationContext(), eventName, Toast.LENGTH_LONG).show();

                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateButtonOnClick(View view){
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("clubs");

        selectedEventName = eventSpinner.getSelectedItem().toString();
        storeSelectedSpinnerValue();

        eventsRef.child(clubName).child("events").child(eventName.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        if (!isFeeValid(eventFee)){
                            Toast.makeText(getApplicationContext(), "Event Age Invalid", Toast.LENGTH_LONG).show();
                        } else if (!isLimitValid(memberNum)) {
                            Toast.makeText(getApplicationContext(), "Event Level Invalid", Toast.LENGTH_LONG).show();
                        } else {
                            ClubEvent newCE = new ClubEvent(selectedEventType, eventName.getText().toString(), Integer.parseInt(memberNum.getText().toString()), clubName);
                            //DatabaseReference dR = FirebaseDatabase.getInstance().getReference("clubs").child(clubName).child("events").child(eventName.getText().toString());
                            uploadClubEvent(newCE);
                            Toast.makeText(getApplicationContext(), "Event Updated", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Event does not exist", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Check failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isFeeValid(EditText fee){
        String feeInput = fee.getText().toString();

        //Checks if the fee input is an integer
        try {
            int feeInt = Integer.parseInt(feeInput);
        } catch (NumberFormatException e) {
            return false;
        }

        //Checks if the EditText contains anything
        return !(feeInput.isEmpty());
    }

    private boolean isLimitValid(EditText limit){
        String limitInput = limit.getText().toString();

        //Checks if the limit input is an integer
        try {
            int limitInt = Integer.parseInt(limitInput);
        } catch (NumberFormatException e) {
            return false;
        }

        //Checks if the EditText contains anything
        return !(limitInput.isEmpty());
    }

    private void uploadClubEvent(ClubEvent event) {
        FirebaseDatabase dbRef = FirebaseDatabase.getInstance();

        DatabaseReference newEventTypeRef = dbRef.getReference("clubs/" + clubName +  "/events/" + event.getName() + "/type");
        DatabaseReference newEventSizeRef = dbRef.getReference("clubs/" + clubName +  "/events/" + event.getName() + "/size");
        DatabaseReference newEventCNameRef = dbRef.getReference("clubs/" + clubName +  "/events/" + event.getName() + "/Club Name");

        newEventTypeRef.setValue(selectedEventName);
        newEventSizeRef.setValue(event.getSize());

        newEventCNameRef.setValue(clubName);
    }
}