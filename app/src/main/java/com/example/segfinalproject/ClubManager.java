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

public class ClubManager extends AppCompatActivity implements ConfirmDeleteDialogFragment.CDDialogListener {

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

        listViewClubs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Club club = clubs.get(i);
                Bundle args = new Bundle();
                args.putString("name", club.getUsername());
                ConfirmDeleteDialogFragment dialog = new ConfirmDeleteDialogFragment();
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "dialog");

                return true;
            }
        });
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

    public void remove(String title) {
        deleteClub(title);
    }
    private void deleteClub(String userName) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("clubs").child(userName);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Club Deleted", Toast.LENGTH_LONG).show();
    }

    public void backButtonOnClick(View view){
        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
        startActivity(intent);
    }


}