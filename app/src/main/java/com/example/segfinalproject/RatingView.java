package com.example.segfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RatingView extends ArrayAdapter{

    private Activity context;

    List<Rating> ratings;

    public RatingView(Activity context, List<Rating> ratings) {
        super(context, R.layout.activity_rating_view, ratings);
        this.context = context;
        this.ratings = ratings;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_rating_view, null, true);

        TextView textViewUsername = (TextView) listViewItem.findViewById(R.id.username_txt);
        TextView textViewScore = (TextView) listViewItem.findViewById(R.id.score_txt);
        TextView textViewDesc = (TextView) listViewItem.findViewById(R.id.desc_txt);

        Rating rating = ratings.get(position);
        textViewUsername.setText("User: " + rating.getUser());
        textViewScore.setText("Score: " + rating.getScore() + "/5");
        textViewDesc.setText("Description: " + rating.getDescription());

        return listViewItem;
    }
}