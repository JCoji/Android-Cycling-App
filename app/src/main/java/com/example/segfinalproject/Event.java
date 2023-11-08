package com.example.segfinalproject;

public class Event {
    String title;
    int age;
    String level;
    int pace;
    //Event requires a name/title, a minimum age for participating, a level (easy,medium,hard) and a pace.
    Event(String title, int age, String level, int pace){
        this.title = title;
        this.age = age;
        this.level = level;
        this.pace = pace;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String new_title){
        title = new_title;
    }
    public int getAge(){
        return age;
    }
    public void setAge(int new_age){
        age = new_age;
    }
    public String getLevel(){
        return level;
    }
    public void setLevel(String new_level){
        title = new_level;
    }
    public int getPace(){
        return pace;
    }
    public void setPace(int new_pace){
        pace = new_pace;
    }
}
