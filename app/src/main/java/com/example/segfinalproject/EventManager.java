package com.example.segfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventManager extends AppCompatActivity implements ConfirmDeleteDialogFragment.CDDialogListener {

    ListView listViewEvents;
    List<Event> events;

    DatabaseReference databaseEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_manager);

        listViewEvents = (ListView) findViewById(R.id.EventList);

        events = new ArrayList<>();

        databaseEvents = FirebaseDatabase.getInstance().getReference("events");

        listViewEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Event event = events.get(i);
                Bundle args = new Bundle();
                args.putString("name", event.getTitle());
                ConfirmDeleteDialogFragment dialog = new ConfirmDeleteDialogFragment();
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "dialog");

                return true;
            }
        });


    }

    protected void onStart() {
        super.onStart();

        databaseEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                events.clear();

                for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Event event;

                        //Temporary non-valid values while firebase updates
                        int age = -1;
                        String level = "";
                        int pace = -1;

                        if(postSnapshot.child("age").getValue() != null){
                            age = Integer.parseInt(postSnapshot.child("age").getValue().toString());
                        }

                        if(postSnapshot.child("level").getValue() != null){
                            level = postSnapshot.child("level").getValue().toString();
                        }

                        if(postSnapshot.child("pace").getValue() != null){
                            pace = Integer.parseInt(postSnapshot.child("pace").getValue().toString());
                        }


                        event = new Event(postSnapshot.getKey(), age, level, pace);
                        events.add(event);

                }

                    EventList eventAdapter = new EventList(EventManager.this, events);
                    listViewEvents.setAdapter(eventAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EventManager.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void remove(String title) {
        deleteEvent(title);
    }

    private void deleteEvent(String title) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("events").child(title);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Event \"" + title + "\" Deleted", Toast.LENGTH_LONG).show();
    }

    public void newEventButtonOnClick(View view) {
        Intent intent = new Intent(getApplicationContext(), EventCreator.class);
        startActivity(intent);
    }

    public void editEventButtonOnClick(View view) {
        Intent intent = new Intent(getApplicationContext(), UpdateEvent.class);
        startActivity(intent);
    }

    public void backButtonOnClick(View view){
        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
        startActivity(intent);
    }
}