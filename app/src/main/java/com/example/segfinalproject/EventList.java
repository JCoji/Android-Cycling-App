package com.example.segfinalproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class EventList extends ArrayAdapter<Event> {

    private Activity context;

    List<Event> events;

    public EventList(Activity context, List<Event> events) {
        super(context, R.layout.event_list, events);
        this.context = context;
        this.events = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.event_list, null, true);

        TextView textViewTitle = (TextView) listViewItem.findViewById(R.id.Title);
        TextView textViewAge = (TextView) listViewItem.findViewById(R.id.Age);
        TextView textViewLevel = (TextView) listViewItem.findViewById(R.id.Level);
        TextView textViewPace = (TextView) listViewItem.findViewById(R.id.Pace);

        Event event = events.get(position);
        textViewTitle.setText(event.getTitle());
        textViewAge.setText(event.getAge() + "");
        textViewLevel.setText(event.getLevel());
        textViewPace.setText(event.getPace() + "");

        return listViewItem;
    }
}