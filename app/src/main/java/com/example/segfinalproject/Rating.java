package com.example.segfinalproject;

public class Rating {
    String score, description, club, user;
    public Rating(String score, String description, String club, String user){
        this.score = score;
        this.description = description;
        this.club = club;
        this.user = user;
    }

    public String getScore(){
        return score;
    }

    public String getDescription(){
        return description;
    }

    public String getClub(){
        return club;
    }

    public String getUser(){
        return user;
    }

    public void setScore(String newScore){
        score = newScore;
    }

    public void setDescription(String newDescription){
        description = newDescription;
    }

    public void setClub(String newClub){
        club = newClub;
    }

    public void setUser(String newUser){
        user = newUser;
    }
}
