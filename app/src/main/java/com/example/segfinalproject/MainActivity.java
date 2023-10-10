package com.example.segfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    Switch isAClub;
    EditText email;
    EditText password;
    EditText username;
    EditText result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isAClub  = (Switch) findViewById(R.id.Club_Switch);
        username = (EditText) findViewById(R.id.Register_Username);
        email = (EditText) findViewById(R.id.Register_Email);
        password = (EditText) findViewById(R.id.Register_Password);
        result = (EditText) findViewById(R.id.Result_text);
    }

    public void Register_Email_Click(View view){
        Account newAccount;
        String accountType = "";

        if(!isUsernameValid(username)){
            result.setText("INVALID USERNAME");
        }
        else if(!isEmailValid(email)){
            result.setText("INVALID EMAIL");
        }else if(!isPasswordValid(password)){
            result.setText("INVALID PASSWORD");
        }else{

            //CHECK IF ADMIN CREDENTIALS

            if(isAClub.isChecked()){
                newAccount = new Club(username.getText().toString(), email.getText().toString(),password.getText().toString());
                accountType = "CLUB";
                Intent intent = new Intent(getApplicationContext(), ClubActivity.class);
                startActivity(intent);
            }else{
                newAccount = new User(username.getText().toString(), email.getText().toString(), password.getText().toString());
                accountType = "USER";
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
            }

            UploadUser(newAccount);
            result.setText("Name: " + newAccount.getUsername() + " Email: " + newAccount.getEmail() + " Password: " + newAccount.getPassword() + " Account Type: " + accountType);

            //Checks if the Account is a user account or club account then sets the activity to a user or club account

        }
            /*
             * CODE FOR ADMIN ACTIVITY:
             *
             * Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
               startActivity(intent);
             */


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

        DatabaseReference newUserRoleRef = database.getReference("users/" + account.getUsername() + "/role");
        DatabaseReference newUserEmailRef = database.getReference("users/" + account.getUsername() + "/email");
        DatabaseReference newUserPasswordRef = database.getReference("users/" + account.getUsername() + "/password");

        newUserRoleRef.setValue(account.getRoll());
        newUserEmailRef.setValue(account.getEmail());
        newUserPasswordRef.setValue(account.getPassword());

    }
}