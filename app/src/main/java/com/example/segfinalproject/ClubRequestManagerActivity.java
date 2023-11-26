package com.example.segfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClubRequestManagerActivity extends AppCompatActivity implements DenyAcceptDialogFragment.DADialogListener {

    String clubName;
    ListView listViewUsers;
    List<User> users;
    DatabaseReference databaseUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_member_list);

        Bundle extras = getIntent().getExtras();

        clubName = extras.getString("clubName");

        listViewUsers = (ListView) findViewById(R.id.userList);

        users = new ArrayList<>();

        databaseUsers = FirebaseDatabase.getInstance().getReference("clubs/" + clubName + "/requests");

        listViewUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = users.get(i);
                Bundle args = new Bundle();
                args.putString("name", user.getUsername());
                DenyAcceptDialogFragment dialog = new DenyAcceptDialogFragment();
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "dialog");

                return true;
            }
        });
    }
    protected void onStart() {
        super.onStart();

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();

                for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User user;

                    //Temporary non-valid values while firebase updates
                    String email = "";


                    if(postSnapshot.child("email").getValue() != null){
                        email = postSnapshot.child("email").getValue().toString();
                    }


                    user = new User(postSnapshot.getKey(), email, "");
                    users.add(user);

                }

                UserList eventAdapter = new UserList(ClubRequestManagerActivity.this, users);
                listViewUsers.setAdapter(eventAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ClubRequestManagerActivity.this, "Failed to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deny(String name){
        denyUser(name);
    }

    public void accept(String name) {
        acceptUser(name);
    }
    private void acceptUser(String name) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("clubs/" + clubName + "/members/" + name);

        dR.setValue(name);
        removeUser(name);
        Toast.makeText(getApplicationContext(),  name + " is now a member of your club.", Toast.LENGTH_LONG).show();
    }

    private void denyUser(String username) {
        removeUser(username);
        Toast.makeText(getApplicationContext(), "User " + username + " denied.", Toast.LENGTH_LONG).show();
    }

    private void removeUser(String username) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("clubs/" + clubName + "/requests").child(username);
        dR.removeValue();
    }

    public void backButtonOnClick(View view){
        Bundle extras = getIntent().getExtras();
        Intent intent = new Intent(getApplicationContext(), ClubActivity.class);
        intent.putExtra("Username", extras.getString("clubName"));
        startActivity(intent);
    }
}