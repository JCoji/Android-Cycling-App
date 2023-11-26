package com.example.segfinalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class DenyAcceptDialogFragment extends DialogFragment {
    private DADialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstances) {


        String username = String.valueOf(getArguments());
        int start = username.indexOf("=") + 1;
        int end = username.length() - 2;
        String finalUsername = username.substring(start, end);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("What would you like to do with "+finalUsername +"'s request?")
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String username = String.valueOf(getArguments());
                        int start = username.indexOf("=") + 1;
                        int end = username.length() - 2;
                        String finalUsername = username.substring(start, end);
                        listener.accept(finalUsername);
                    }
                })
                .setNeutralButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String username = String.valueOf(getArguments());
                        int start = username.indexOf("=") + 1;
                        int end = username.length() - 2;
                        String finalUsername = username.substring(start, end);
                        listener.deny(finalUsername);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DADialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + "must implement DADialogListener");
        }
    }

    public interface DADialogListener {
        void deny(String name);
        void accept(String name);
    }
}
