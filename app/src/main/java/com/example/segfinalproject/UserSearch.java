package com.example.segfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserSearch extends AppCompatActivity implements ConfirmJoinDialogFragment.CJDialogListener {
    ListView listViewEvents;
    List<ClubEvent> events;

    DatabaseReference databaseClubs;
    UserEventView eventAdapter;

    EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        listViewEvents = (ListView) findViewById(R.id.AllEvents);
        searchBar = (EditText) findViewById(R.id.SearchBar);


        events = new ArrayList<>();

        databaseClubs = FirebaseDatabase.getInstance().getReference("clubs");

        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                ClubEvent event = events.get(i);
                Bundle args = new Bundle();
                args.putString("eventName", event.getName());
                args.putString("clubName", event.getClubName());
                args.putInt("eventAge", event.getEventType().getAge());
                ConfirmJoinDialogFragment dialog = new ConfirmJoinDialogFragment();
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    protected void onStart() {
        super.onStart();

        databaseClubs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                events.clear();

                for(DataSnapshot ClubSnapshot : snapshot.getChildren()) {
                    //Toast.makeText(UserSearch.this, "BOOP", Toast.LENGTH_SHORT).show();

                    for(DataSnapshot eventSnapshot : ClubSnapshot.child("events").getChildren()){
                        //Looking at individual events in events tab of club
                        ClubEvent event;

                            //Temporary non-valid values while firebase updates
                            Event type = new Event("", -1, "", -1);
                            String name = "";
                            int size = -1;
                            String clubName = "";
                            int fee = -1;

                            if(eventSnapshot.getKey() != null){
                                name = eventSnapshot.getKey().toString();

                            }

                            if(eventSnapshot.child("type").getValue() != null){
                                type.setTitle(eventSnapshot.child("type").getValue().toString());
                            }

                            if(eventSnapshot.child("size").getValue() != null){
                                size = Integer.parseInt(eventSnapshot.child("size").getValue().toString());
                            }

                            if(eventSnapshot.child("Club Name").getValue() != null){
                                clubName = eventSnapshot.child("Club Name").getValue().toString();
                            }

                            if(eventSnapshot.child("fee").getValue() != null){
                                fee = Integer.parseInt(eventSnapshot.child("fee").getValue().toString());
                            }

                            if(eventSnapshot.child("age").getValue() != null){
                                type.setAge(Integer.parseInt(eventSnapshot.child("age").getValue().toString()));
                            }


                            //Event name, club name, size, type
                            event = new ClubEvent(type,name, size, clubName, fee);
                            events.add(event);

                    }


                }

                eventAdapter = new UserEventView(UserSearch.this, events);
                listViewEvents.setAdapter(eventAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserSearch.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void submitBtnOnClick(View view){
        List<ClubEvent> searchHits = new ArrayList<>();
        String searchTxt = searchBar.getText().toString();

        if(searchTxt != null){
            for(int i = 0; i < events.size(); i++){
                ClubEvent curEvent = events.get(i);

                if(curEvent.getName().contains(searchTxt)){
                    searchHits.add(curEvent);
                }else if(curEvent.getClubName().contains(searchTxt)){
                    searchHits.add(curEvent);
                }else if(curEvent.getEventType().getTitle().contains(searchTxt)){
                    searchHits.add(curEvent);
                }

            }

            UserEventView searchAdapter = new UserEventView(UserSearch.this, searchHits);
            listViewEvents.setAdapter(searchAdapter);
        }else{
            eventAdapter = new UserEventView(UserSearch.this, events);
            listViewEvents.setAdapter(eventAdapter);
        }

    }

    public void join(String eventName, String clubName) {
        Bundle extras = getIntent().getExtras();
        DatabaseReference dRUser = FirebaseDatabase.getInstance().getReference("users/" + extras.get("Username") + "/Events/" + eventName);
        DatabaseReference dREvent = FirebaseDatabase.getInstance().getReference("clubs/" + clubName + "/events/" + eventName + "/registrees/" + extras.get("Username"));

        dRUser.setValue(eventName);
        dREvent.setValue(extras.get("Username"));
    }

    public void backBtnOnClick(View view) {
        Bundle extras = getIntent().getExtras();
        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
        intent.putExtra("Username", extras.getString("Username"));
        startActivity(intent);
    }
}