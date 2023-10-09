package com.example.segfinalproject;

public class User extends Account{
    User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;

        final String roll = "USER";
    }
}
