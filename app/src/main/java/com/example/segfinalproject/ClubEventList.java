package com.example.segfinalproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

public class ClubEventList extends ArrayAdapter<String> {
    private Activity context;
    List<String> clubEvents;

    public ClubEventList(Activity context, List<String> clubEvents) {
        super(context, R.layout.club_event_list);
        this.context = context;
        this.clubEvents = clubEvents;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.club_event_list, null, true);

        TextView textViewTitle = (TextView) listViewItem.findViewById(R.id.Title);


        String event = clubEvents.get(position);

        textViewTitle.setText("Name:" + event);


        return listViewItem;
    }
}
