package com.example.segfinalproject;

import java.util.ArrayList;

public class User extends Account{
    public ArrayList<ClubEvent> registeredEvents;
    User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;

        roll = "users";
    }

}
