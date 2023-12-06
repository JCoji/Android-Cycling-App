package com.example.segfinalproject;

import java.util.ArrayList;
import java.util.List;

public class ClubEvent {
    Event eventType;
    ArrayList<User> participants;
    String name;
    int maxMembers;
    int currMembers;
    int fee;

    int age;
    String clubName;
    String date;
    String time;

    public ClubEvent(Event eventType, String name, int maxMembers, String clubName, int fee){
        this.eventType = eventType;
        this.name = name;
        this.maxMembers = maxMembers;
        this.currMembers = 0;
        this.fee = fee;
        this.clubName = clubName;
        participants = new ArrayList<User>();

        age = eventType.getAge();
    }

    public Event getEventType(){
        return eventType;
    }

    public String getName(){
        return name;
    }

    public int getAge(){
        return age;
    }

    public int getSize(){
        return maxMembers;
    }

    public String getClubName(){
        return clubName;
    }

    public String getDate() { return this.date;}

    public String getTime() {return this.time;}

    public int getFee() {return this.fee;}

    public int getCurrMembers() {return this.currMembers;}

    public ArrayList<User> getParticipants(){
        return participants;
    }

    public void addParticipant(User participant) throws IndexOutOfBoundsException{
        if(this.currMembers <= maxMembers){
            participants.add(participant);

            this.currMembers++;
        }else{
            throw new IndexOutOfBoundsException("Participant limit exceeded");
        }
    }

    public void removeParticipant(User participant) {
        for (int i = 0; i < currMembers; i ++) {
            if (participants.get(i).equals(participant)) {
                participants.remove(i);
                this.currMembers--;
            }
        }
    }

    public String toString(){
        return name;
    }

    public void setDate(int dd, int mm, int yyyy) {
        this.date = Integer.toString(yyyy) + "-" + Integer.toString(mm + 1) + "-" + Integer.toString(dd);
    }

    public void setTime(int hour, int mins) {
        if (mins > 9) {
            this.time = Integer.toString(hour) + ":" + Integer.toString(mins);
        } else {
            this.time = Integer.toString(hour) + ":0" + Integer.toString(mins);

        }
    }

}
