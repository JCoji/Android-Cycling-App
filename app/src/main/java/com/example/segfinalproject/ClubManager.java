package com.example.segfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClubManager extends AppCompatActivity {

    ListView listViewClubs;
    List<Club> clubs;

    DatabaseReference databaseClubs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_manager);

        listViewClubs = (ListView) findViewById(R.id.ClubList);

        clubs = new ArrayList<>();

        databaseClubs = FirebaseDatabase.getInstance().getReference("clubs");
    }


    protected void onStart() {
        super.onStart();

        databaseClubs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clubs.clear();


                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Club club;
                    String email = postSnapshot.child("email").getValue().toString();
                    String password = postSnapshot.child("password").getValue().toString();

                    club = new Club(postSnapshot.getKey(), email, password);
                    clubs.add(club);
                }

                ClubList clubAdapter = new ClubList(ClubManager.this, clubs);
                listViewClubs.setAdapter(clubAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}