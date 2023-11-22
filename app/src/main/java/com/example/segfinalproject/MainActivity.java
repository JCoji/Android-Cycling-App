package com.example.segfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    Switch isAClub;
    EditText email;
    EditText password;
    EditText username;
    EditText result;

    boolean success = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isAClub  = (Switch) findViewById(R.id.Club_Switch);
        username = (EditText) findViewById(R.id.Register_Username);
        email = (EditText) findViewById(R.id.Register_Email);
        password = (EditText) findViewById(R.id.Register_Password);
        result = (EditText) findViewById(R.id.Result_Text);


    }

    public void Register_Click(View view){
        Account newAccount;
        String accountType = "";

        String ADMIN_USERNAME = "admin";
        String ADMIN_PWD = "admin";

        if(!isUsernameValid(username)){
            result.setText("INVALID USERNAME");
        }
        else if(!isEmailValid(email)){
            result.setText("INVALID EMAIL");
        }else if(!isPasswordValid(password)){
            result.setText("INVALID PASSWORD");
        }else {

            //CHECK IF ADMIN CREDENTIALS
            if (password.getText().toString().equals(ADMIN_PWD) && username.getText().toString().equals(ADMIN_USERNAME)) {
                newAccount = new Admin(username.getText().toString(), email.getText().toString(), password.getText().toString());
            } else if (isAClub.isChecked()) {
                newAccount = new Club(username.getText().toString(), email.getText().toString(), password.getText().toString());

            } else {
                newAccount = new User(username.getText().toString(), email.getText().toString(), password.getText().toString());
            }

            DatabaseReference accountRef = FirebaseDatabase.getInstance().getReference(newAccount.getRoll());
            accountRef.child(username.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        if(task.getResult().exists()){
                            result.setText("ACCOUNT ALREADY EXISTS");
                        }else{
                            success = true;

                            Intent intent;

                            switch(newAccount.getRoll()){
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

                            UploadUser(newAccount);
                            intent.putExtra("Username", newAccount.getUsername());
                            startActivity(intent);
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Check failed", Toast.LENGTH_LONG).show();
                    }
                }

            });

        }
    }

    public void to_Login_Click(View view){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public boolean isEmailValid(EditText email){
        String emailInput = email.getText().toString();

        if(!emailInput.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            return true;
        }else{
            return false;
        }
    }

    public boolean isPasswordValid(EditText password){
        String passwordInput = password.getText().toString();

        if(!passwordInput.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    public boolean isUsernameValid(EditText username){
        String usernameInput = username.getText().toString();

        if(!usernameInput.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    public void UploadUser(Account account){


        FirebaseDatabase database = FirebaseDatabase.getInstance();


        if(account.getRoll() == "clubs"){
            DatabaseReference newUserEmailRef = database.getReference("clubs/" + account.getUsername() + "/email");
            DatabaseReference newUserPasswordRef = database.getReference("clubs/" + account.getUsername() + "/password");

            newUserEmailRef.setValue(account.getEmail());
            newUserPasswordRef.setValue(account.getPassword());

        }else if(account.getRoll() == "users"){
            DatabaseReference newUserEmailRef = database.getReference("users/" + account.getUsername() + "/email");
            DatabaseReference newUserPasswordRef = database.getReference("users/" + account.getUsername() + "/password");

            newUserEmailRef.setValue(account.getEmail());
            newUserPasswordRef.setValue(account.getPassword());
        }else if(account.getRoll() == "admin"){
            DatabaseReference newUserEmailRef = database.getReference("admin/" + account.getUsername() + "/email");
            DatabaseReference newUserPasswordRef = database.getReference("admin/" + account.getUsername() + "/password");

            newUserEmailRef.setValue(account.getEmail());
            newUserPasswordRef.setValue(account.getPassword());
        }




    }
}