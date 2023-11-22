package com.example.segfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText password;
    EditText username;
    EditText result;

    String[] rolls = {"admin", "users", "clubs"};

    boolean completed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.Register_Username);
        password = (EditText) findViewById(R.id.Register_Password);
        result = (EditText) findViewById(R.id.Result_Text);
    }

    public void Login_Click(View view){
        FindUser(0);

    }

    public void Back_Click(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void FindUser(int rollsIndex){
        int i = rollsIndex;
        completed = false;

        DatabaseReference accountRef = FirebaseDatabase.getInstance().getReference(rolls[i]);

        accountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if(postSnapshot.getKey().toString().equals(username.getText().toString())){
                        if(postSnapshot.child("password").getValue().toString().equals(password.getText().toString())){
                            login(rolls[i]);
                        }else{
                            result.setText("INCORRECT PASSWORD");
                        }

                        completed = true;
                    }
                }

                if(!completed){
                    if(i == rolls.length - 1){
                        result.setText("ACCOUNT DOES NOT EXIST");
                    }else{
                        FindUser(i+ 1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    private void login(String roll){
        Intent intent;

        switch(roll){
            case("admin"):
                intent = new Intent(getApplicationContext(), AdminActivity.class);
                break;
            case("clubs"):
                intent = new Intent(getApplicationContext(), ClubActivity.class);
                break;
            default:
                intent = new Intent(getApplicationContext(), UserActivity.class);
                break;

        }

        intent.putExtra("Username", username.getText().toString());
        startActivity(intent);
    }
}