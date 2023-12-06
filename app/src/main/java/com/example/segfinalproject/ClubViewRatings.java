package com.example.segfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClubViewRatings extends AppCompatActivity {

    ListView listViewRatings;
    List<Rating> ratings;

    DatabaseReference databaseRatings;
    RatingView ratingAdapter;

    String clubname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_view_ratings);

        listViewRatings = (ListView) findViewById(R.id.ratings);
        ratings = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        clubname = extras.getString("clubName");

        databaseRatings = FirebaseDatabase.getInstance().getReference("clubs/" + clubname + "/ratings/" );
    }

    public void backButtonOnClick(View view) {
        Bundle extras = getIntent().getExtras();
        Intent intent = new Intent(getApplicationContext(), ClubActivity.class);
        intent.putExtra("Username", clubname);
        startActivity(intent);
    }

    protected void onStart() {
        super.onStart();

        databaseRatings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ratings.clear();

                for(DataSnapshot ratingSnapshot : snapshot.getChildren()) {
                    //Temporary non-valid values while firebase updates
                    String username = "";
                    String score = "";
                    String desc = "";

                    if(ratingSnapshot.getKey() != null){
                        username = ratingSnapshot.getKey().toString();
                    }

                    if(ratingSnapshot.child("scores").getValue() != null){
                        score = ratingSnapshot.child("scores").getValue().toString();
                    }

                    if(ratingSnapshot.child("descriptions").getValue() != null){
                        desc = ratingSnapshot.child("descriptions").getValue().toString();
                    }


                    //rating(score, desc, club, user)
                    Rating rating = new Rating(score, desc, "", username);
                    ratings.add(rating);

                }

                ratingAdapter = new RatingView(ClubViewRatings.this, ratings);
                listViewRatings.setAdapter(ratingAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ClubViewRatings.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}