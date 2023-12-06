package com.example.segfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClubActivity extends AppCompatActivity implements ClubVerificationDialog.CVDialogListener, ConfirmDeleteDialogFragment.CDDialogListener {

    String name;
    TextView clubname;
    ListView listViewEvents;
    List<String> eventNames;

    DatabaseReference databaseEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        Bundle extras = getIntent().getExtras();
        name = extras.getString("Username");

        clubname = (TextView) findViewById(R.id.clubText);

        clubname.setText("Welcome: " + name);

        listViewEvents = (ListView) findViewById(R.id.CEventsList);
        eventNames = new ArrayList<>();

        DatabaseReference databaseVerifiedCheck = FirebaseDatabase.getInstance().getReference("clubs/" + name);
        databaseVerifiedCheck.addValueEventListener(new ValueEventListener() {
            boolean unverified = true;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(unverified) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        if (postSnapshot.getKey().equals("unverified")) {
                            ClubVerificationDialog dialog = new ClubVerificationDialog();
                            dialog.show(getSupportFragmentManager(), "Verification Dialog");
                            unverified = false;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Toast.makeText(getApplicationContext(), "clubs/" + name + "/events", Toast.LENGTH_LONG).show();

        listViewEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String eventName = eventNames.get(i);
                Bundle args = new Bundle();
                args.putString("username", eventName);
                ConfirmDeleteDialogFragment dialog = new ConfirmDeleteDialogFragment();
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "dialog");

                return true;
            }
        });
    }

    protected void onStart() {
        super.onStart();

        databaseEvents = FirebaseDatabase.getInstance().getReference("clubs/" + name + "/events");

        databaseEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventNames.clear();

                for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String eventName = "";

                    if(eventName != null){
                        eventName = postSnapshot.getKey();
                        eventNames.add(eventName);
                    }


                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ClubActivity.this, android.R.layout.simple_list_item_1,eventNames);
                listViewEvents.setAdapter(adapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void storeClub(String phoneNum, String fullName, String socialLink) {
        DatabaseReference dRPhone = FirebaseDatabase.getInstance().getReference("clubs/" + name + "/phoneNumber");
        DatabaseReference dRName = FirebaseDatabase.getInstance().getReference("clubs/" +  name + "/fullName");
        DatabaseReference dRLink = FirebaseDatabase.getInstance().getReference("clubs/" +  name + "/socialMedia");
        DatabaseReference dRVerify = FirebaseDatabase.getInstance().getReference("clubs/" + name + "/unverified");

        dRPhone.setValue(phoneNum);
        dRName.setValue(fullName);
        dRLink.setValue(socialLink);
        dRVerify.removeValue();
    }

    public void newClubEventButtonOnClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ClubEventCreator.class);

        intent.putExtra("ClubName", name);

        startActivity(intent);
    }

    public void editClubEventButtonOnClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ClubEventEditor.class);

        intent.putExtra("ClubName", name);

        startActivity(intent);
    }

    public void membersOnClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ClubMemberListActivity.class);
        intent.putExtra("clubName", name);
        startActivity(intent);
    }

    public void ratingOnClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ClubViewRatings.class);
        intent.putExtra("clubName", name);
        startActivity(intent);
    }

    public void requestOnClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ClubRequestManagerActivity.class);
        intent.putExtra("clubName", name);
        startActivity(intent);
    }
    public void remove(String name){
        deleteEvent(name);
    }
    private void deleteEvent(String title) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("clubs/" + name + "/events").child(title);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Event \"" + title + "\" Deleted", Toast.LENGTH_LONG).show();
    }

}