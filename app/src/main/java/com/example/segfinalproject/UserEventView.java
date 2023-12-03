package com.example.segfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserEventView extends ArrayAdapter<ClubEvent> implements Filterable{

    private Activity context;

    List<ClubEvent> events;
    List<ClubEvent> filteredEvents;

    public UserEventView(Activity context, List<ClubEvent> events) {
        super(context, R.layout.activity_user_event_view, events);
        this.context = context;
        this.events = events;


    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_user_event_view, null, true);

        TextView textViewTitle = (TextView) listViewItem.findViewById(R.id.EventName);
        TextView textViewType = (TextView) listViewItem.findViewById(R.id.EventType);
        TextView textViewClubName = (TextView) listViewItem.findViewById(R.id.ClubNameUserView);

        ClubEvent event = events.get(position);
        textViewTitle.setText("Event Name: " + event.getName());
        textViewType.setText("Type: " + event.getEventType().getTitle());
        textViewClubName.setText("Club: " + event.getClubName());

        return listViewItem;
    }




}
