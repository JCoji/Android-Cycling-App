package com.example.segfinalproject;

import java.util.ArrayList;
import java.util.List;

public class ClubEvent {
    Event eventType;
    ArrayList<User> participants;
    String name;
    int maxMembers;

    public ClubEvent(Event eventType, String name,int maxMembers){
        this.eventType = eventType;
        this.name = name;
        this.maxMembers = maxMembers;
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


}
