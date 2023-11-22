package com.example.segfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class ClubActivity extends AppCompatActivity {

    String name;
    EditText clubname;
    ListView listViewEvents;
    List<String> eventNames;

    DatabaseReference databaseEvents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        Bundle extras = getIntent().getExtras();
        name = extras.getString("Username");

        clubname = (EditText) findViewById(R.id.clubText);

        clubname.setText("Welcome: " + name);

        listViewEvents = (ListView) findViewById(R.id.CEventsList);
        eventNames = new ArrayList<>();

        //Toast.makeText(getApplicationContext(), "clubs/" + name + "/events", Toast.LENGTH_LONG).show();

        listViewEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String eventName = eventNames.get(i);
                deleteEvent(eventName);
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


    public void newClubEventButtonOnClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ClubEventCreator.class);

        intent.putExtra("ClubName", name);

        startActivity(intent);
    }

    private void deleteEvent(String title) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("clubs/" + name + "/events").child(title);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Event \"" + title + "\" Deleted", Toast.LENGTH_LONG).show();
    }

}