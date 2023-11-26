package com.example.segfinalproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import org.w3c.dom.Text;

public class ConfirmDeleteDialogFragment extends DialogFragment {


    private CDDialogListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_confirm_delete_dialog, null);
        String username = String.valueOf(getArguments());
        int start = username.indexOf("=") + 1;
        int end = username.length() - 2;
        String finalUsername = username.substring(start, end);

        TextView message = view.findViewById(R.id.dialog_text);
        message.setText("Are you sure you want to delete " + finalUsername);

        builder.setView(view)
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        String username = String.valueOf(getArguments());
                        int start = username.indexOf("=") + 1;
                        int end = username.length() - 2;
                        String finalUsername = username.substring(start, end);
                        listener.remove(finalUsername);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (CDDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + "must implement CDDialogListener");
        }
    }
    public interface CDDialogListener{
        void remove(String name);
    }
}
