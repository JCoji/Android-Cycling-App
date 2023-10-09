package com.example.segfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;


public class MainActivity extends AppCompatActivity {

    Switch isAClub;
    EditText email;
    EditText password;

    EditText result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isAClub  = (Switch) findViewById(R.id.Club_Switch);
        email = (EditText) findViewById(R.id.Register_Email);
        password = (EditText) findViewById(R.id.Register_Password);
        result = (EditText) findViewById(R.id.Result_text);
    }

    public void Register_Email_Click(View view){
        Account newAccount;
        String accountType = "";

        if(!isEmailValid(email)){
            result.setText("INVALID EMAIL");
        }else if(!isPasswordValid(password)){
            result.setText("INVALID PASSWORD");
        }else{
            if(isAClub.isChecked()){
                newAccount = new Club(email.getText().toString(),password.getText().toString());
                accountType = "CLUB";
            }else{
                newAccount = new User(email.getText().toString(), password.getText().toString());
                accountType = "USER";
            }

            result.setText("Email: " + newAccount.getEmail() + " Password: " + newAccount.getPassword() + " Account Type: " + accountType);
        }



    }

    public boolean isEmailValid(EditText email){
        String emailInput = email.getText().toString();

        if(!emailInput.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            return true;
        }else{
            return false;
        }
    }

    public boolean isPasswordValid(EditText email){
        String passwordInput = password.getText().toString();

        if(!passwordInput.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    public void UploadUser(Account account){

    }
}