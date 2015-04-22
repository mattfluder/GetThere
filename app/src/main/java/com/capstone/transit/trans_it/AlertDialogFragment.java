package com.capstone.transit.trans_it;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Thomas on 4/21/2015.
 */
public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Sorry!")
                .setMessage("Try again!")
                .setPositiveButton("OK", null);


        AlertDialog dialog = builder.create();
        return dialog;

    }
}