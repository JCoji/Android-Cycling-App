package com.example.segfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class UserActivity extends AppCompatActivity {


    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Bundle extras = getIntent().getExtras();

        username = (EditText) findViewById(R.id.userText);

        username.setText(extras.getString("Username"));
    }

    public void toSearchBtnOnClick(View view){
        Bundle extras = getIntent().getExtras();
        Intent intent = new Intent(getApplicationContext(), UserSearch.class);
        intent.putExtra("Username", extras.getString("Username"));
        startActivity(intent);
    }

    public void toRateBtnOnClick(View view){
        Bundle extras = getIntent().getExtras();
        Intent intent = new Intent(getApplicationContext(), rating_activity.class);
        intent.putExtra("Username", extras.getString("Username"));
        startActivity(intent);
    }

}