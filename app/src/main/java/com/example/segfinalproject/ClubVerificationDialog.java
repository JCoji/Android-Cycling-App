package com.example.segfinalproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClubVerificationDialog extends DialogFragment {
    private CVDialogListener listener;
    private EditText phone;
    private EditText fullName;
    private EditText socialLink;

    private Button button;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_club_verification_dialog, null);

        builder.setView(view)
                .setTitle("Verify")

                .setNegativeButton("Confirm Info", null);


        phone = view.findViewById(R.id.user_phone);
        fullName = view.findViewById(R.id.user_full_name);
        socialLink = view.findViewById(R.id.user_social_media);
        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String phoneNum = phone.getText().toString();
                        String name = fullName.getText().toString();
                        String link = socialLink.getText().toString();
                        if (!(isNameValid(name))) {
                            Toast.makeText(getContext(), "Name invalid.", Toast.LENGTH_LONG).show();
                        } else if (!(isPhoneNumValid(phoneNum))) {
                            Toast.makeText(getContext(), "Phone number invalid.", Toast.LENGTH_LONG).show();
                        } else if (!(isLinkValid(link))) {
                            Toast.makeText(getContext(), "Link is invalid.", Toast.LENGTH_LONG).show();
                        } else {
                            listener.storeTexts(phoneNum, name, link);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        return dialog;
    }
    private boolean isPhoneNumValid(String phoneNum) {
        Pattern p = Pattern.compile("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$"); //Regex from https://stackoverflow.com/questions/16699007/regular-expression-to-match-standard-10-digit-phone-number
        Matcher m = p.matcher(phoneNum);
        return (m.matches());
    }

    private boolean isNameValid(String name) {
        Pattern p = Pattern.compile("^[a-zA-Z]+(?:[\\s.]+[a-zA-Z]+)*$"); //Regex from https://stackoverflow.com/questions/43935255/regular-expression-for-name-with-spaces-allowed-in-between-the-text-and-avoid-sp
        Matcher m = p.matcher(name);
        return (m.matches());
    }

    private boolean isLinkValid(String link) {
        return !(link.isEmpty());
    }


    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (CVDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement CVDialogListener.");
        }
    }
    public interface CVDialogListener{
        void storeTexts(String phoneNum, String fullName, String socialLink);

    }
}