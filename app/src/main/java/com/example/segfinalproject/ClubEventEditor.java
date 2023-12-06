package com.example.segfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
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
    Button setTimeButton;
    Button setDateButton;

    int[] date = new int[3];
    int[] time = new int[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_event_editor);

        eventSpinner = (Spinner) findViewById(R.id.EventTypeSpinner);
        memberNum = (EditText) findViewById(R.id.Max_mem_txt);
        eventName = (EditText) findViewById(R.id.name_txt);
        eventFee = (EditText) findViewById(R.id.fee_txt);
        setTimeButton = (Button) findViewById(R.id.set_time_btn);
        setDateButton = (Button) findViewById(R.id.set_date_btn);

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
                // It's better to use a List because you don't know the size
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

    public void setTimeBtnOnClick(View view) {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "Time Picker");
    }
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.time[0] = hourOfDay;
        this.time[1] = minute;
    }
    public void setDateBtnOnClick(View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "Date Picker");
    }
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.date[0] = year;
        this.date[1] = month;
        this.date[2] = dayOfMonth;
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
                            Toast.makeText(getApplicationContext(), "Participant Limit Invalid", Toast.LENGTH_LONG).show();
                        } else if (!(isFeeValid(eventFee))) {
                            Toast.makeText(getApplicationContext(), "Event Fee Invalid", Toast.LENGTH_LONG).show();
                        } else if (!(isDateValid(date))) {
                            Toast.makeText(getApplicationContext(), "Event Date Invalid", Toast.LENGTH_LONG).show();
                        } else {
                            ClubEvent newCE = new ClubEvent(selectedEventType, eventName.getText().toString(), Integer.parseInt(memberNum.getText().toString()), clubName, Integer.parseInt(eventFee.getText().toString()));
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
    private boolean isDateValid(int[] arr) {
        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        int easyDateCurr = (y * m) + d;
        //Checks if the date entered is in the future
        return (easyDateCurr < ((arr[0] * arr[1]) + arr[2]));
    }

    private void uploadClubEvent(ClubEvent event) {
        FirebaseDatabase dbRef = FirebaseDatabase.getInstance();

        DatabaseReference newEventTypeRef = dbRef.getReference("clubs/" + clubName +  "/events/" + event.getName() + "/type");
        DatabaseReference newEventSizeRef = dbRef.getReference("clubs/" + clubName +  "/events/" + event.getName() + "/limit");
        DatabaseReference newEventDateRef = dbRef.getReference("clubs/" + clubName +  "/events/" + event.getName() + "/date");
        DatabaseReference newEventTimeRef = dbRef.getReference("clubs/" + clubName +  "/events/" + event.getName() + "/time");
        DatabaseReference newEventFeeRef = dbRef.getReference("clubs/" + clubName +  "/events/" + event.getName() + "/fee");
        DatabaseReference newEventCNameRef = dbRef.getReference("clubs/" + clubName +  "/events/" + event.getName() + "/Club Name");

        newEventTypeRef.setValue(selectedEventName);
        newEventSizeRef .setValue(event.getSize());
        newEventCNameRef.setValue(clubName);
        newEventDateRef.setValue(event.getDate());
        newEventTimeRef.setValue(event.getTime());
        newEventFeeRef.setValue(event.getFee());
    }
}