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
    List<ClubEvent> clubEvents;

    public ClubEventList(Activity context, List<ClubEvent> clubEvents) {
        super(context, R.layout.club_event_list);
        this.context = context;
        this.clubEvents = clubEvents;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.club_event_list, null, true);

        TextView textViewTitle = (TextView) listViewItem.findViewById(R.id.Title);
        TextView textViewClubName = (TextView) listViewItem.findViewById(R.id.ClubName);
        TextView textViewType = (TextView) listViewItem.findViewById(R.id.Type);
        TextView textViewParticipants = (TextView) listViewItem.findViewById(R.id.ParticipantNum);
        TextView textViewFee = (TextView) listViewItem.findViewById(R.id.Fee);
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.Date);
        TextView textViewTime = (TextView) listViewItem.findViewById(R.id.Time);


        String title = clubEvents.get(position).getName();
        String clubName = clubEvents.get(position).getClubName();
        String type = String.valueOf(clubEvents.get(position).getEventType());
        String participants = String.valueOf(clubEvents.get(position).getCurrMembers()) + "/" + String.valueOf(clubEvents.get(position).getSize());
        String fee = String.valueOf(clubEvents.get(position).getFee());
        String date = clubEvents.get(position).getDate();
        String time = clubEvents.get(position).getTime();

        textViewTitle.setText(title);
        textViewClubName.setText(clubName);
        textViewType.setText(type);
        textViewParticipants.setText(participants);
        textViewFee.setText(fee);
        textViewDate.setText(date);
        textViewTime.setText(time);

        return listViewItem;
    }
}
