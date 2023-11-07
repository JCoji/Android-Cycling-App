package com.example.segfinalproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ClubList extends ArrayAdapter<Club> {
    private Activity context;
    List<Club> clubs;

    public ClubList(Activity context, List<Club> clubs) {
        super(context, R.layout.club_list, clubs);
        this.context = context;
        this.clubs = clubs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.club_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.ClubName);
        TextView textViewEmail = (TextView) listViewItem.findViewById(R.id.ClubEmail);

        Account club = clubs.get(position);
        textViewName.setText(club.getUsername());
        textViewEmail.setText(club.getEmail());
        return listViewItem;
    }
}
