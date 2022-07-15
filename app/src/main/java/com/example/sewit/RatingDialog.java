package com.example.sewit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;


public class RatingDialog extends AppCompatDialogFragment {

    RatingBar rating;
    TextInputEditText feedback;
    DialogListener dialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.layout_rating_dialog,null);

        builder.setView(view)
                .setTitle("Rate the tailor")
                .setNegativeButton("cancel", (dialog, which) -> {
                    // cancel
                })
                .setPositiveButton("ok", (dialog, which) -> {
                    float ratingFloat = rating.getRating();
                    int Rating = (int) ratingFloat;
                    String Feedback = Objects.requireNonNull(feedback.getText()).toString();
                    dialogListener.applyDetails(Rating,Feedback);

                });

        rating = view.findViewById(R.id.ratingBar);
        feedback = view.findViewById(R.id.getFeedback);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dialogListener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement Dialog listener");
        }
    }

    public interface DialogListener{
        void applyDetails(int Rating,String Feedback);
    }
}
