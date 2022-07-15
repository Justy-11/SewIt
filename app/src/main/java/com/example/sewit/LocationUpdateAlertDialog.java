package com.example.sewit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class LocationUpdateAlertDialog extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Location update alert")
                .setMessage("Location should be updated for proper functionality of the app")
                .setIcon(R.drawable.ic_alert_location)
                .setPositiveButton("ok", (dialog, which) -> {
                    //not gonna do anything here
                });

        return builder.create();
    }
}
