package com.example.segfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class ClubMemberListActivity extends AppCompatActivity implements ConfirmDeleteDialogFragment.CDDialogListener {



    boolean t;
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

        databaseUsers = FirebaseDatabase.getInstance().getReference("clubs/" + clubName + "/members");

        listViewUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = users.get(i);
                Bundle args = new Bundle();
                args.putString("name", user.getUsername());
                ConfirmDeleteDialogFragment dialog = new ConfirmDeleteDialogFragment();
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

                UserList eventAdapter = new UserList(ClubMemberListActivity.this, users);
                listViewUsers.setAdapter(eventAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ClubMemberListActivity.this, "Failed to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void remove(String title) {
        deleteUser(title);
    }
    private void deleteUser(String username) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("clubs/" + clubName + "/members").child(username);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "User " + username + " removed.", Toast.LENGTH_LONG).show();
    }

    public void backButtonOnClick(View view){
        Intent intent = new Intent(getApplicationContext(), ClubActivity.class);
        intent.putExtra("Username", clubName);
        startActivity(intent);
    }
}