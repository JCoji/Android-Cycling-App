package com.example.segfinalproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfirmJoinDialogFragment extends DialogFragment {
    private EditText ageText;
    private CJDialogListener listener;

    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        String eventname = String.valueOf(getArguments());
        int start = eventname.indexOf("=") + 1;
        int end = eventname.indexOf(",");
        String finalEventName = eventname.substring(start, end);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_confirm_join_dialog, null);

        builder.setView(view)
                .setTitle("Do you want to join " + finalEventName +"?")
                .setPositiveButton("Join", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        ageText = (EditText) view.findViewById(R.id.age);
        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userAge = ageText.getText().toString();
                        if (isAgeValid(userAge)) {
                            Bundle extras = getArguments();
                            listener.join(finalEventName, extras.getString("clubName"));
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "Age is not valid", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        return dialog;
    }
    public boolean isAgeValid(String age) {;
        int ageInt = 0;
        //Checks if the fee input is an integer
        try {
            ageInt = Integer.parseInt(age);
        } catch (NumberFormatException e) {
            return false;
        }

        Bundle extras = getArguments();

        if ( ageInt < extras.getInt("eventAge")) {
            return false;
        }
        //Checks if the EditText contains anything
        return !(age.isEmpty());

    }
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ConfirmJoinDialogFragment.CJDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement CVDialogListener.");
        }
    }
    public interface CJDialogListener {
        void join(String eventName, String clubName);
    }
}