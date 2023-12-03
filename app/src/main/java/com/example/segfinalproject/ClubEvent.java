package com.example.segfinalproject;

import java.util.ArrayList;
import java.util.List;

public class ClubEvent {
    Event eventType;
    ArrayList<User> participants;
    String name;
    int maxMembers;

    String clubName;

    public ClubEvent(Event eventType, String name,int maxMembers, String clubName){
        this.eventType = eventType;
        this.name = name;
        this.maxMembers = maxMembers;
        this.clubName = clubName;
        participants = new ArrayList<User>();
    }

    public Event getEventType(){
        return eventType;
    }

    public String getName(){
        return name;
    }

    public int getSize(){
        return maxMembers;
    }

    public String getClubName(){
        return clubName;
    }

    public ArrayList<User> getParticipants(){
        return participants;
    }

    public boolean addParticipant(User participant){
        if(participants.size() < maxMembers){
            participants.add(participant);
            return true;
        }else{
            return false;
        }
    }

    public String toString(){
        return name;
    }


}
