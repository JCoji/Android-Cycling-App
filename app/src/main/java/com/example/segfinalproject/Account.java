package com.example.segfinalproject;

public abstract class Account {

    String username;
    String email;
    String password;

    String roll;

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String getUsername(){
        return username;
    }

    public String getRoll(){
        return roll;
    }
    public boolean equals(Account other) {
        if (this.username.equals(other.getUsername())) {
            return true;
        } else {
            return false;
        }
    }
}
