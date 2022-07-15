package com.example.sewit;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MoreTailorFragment extends Fragment {

    TextView signOutTailor;
    TextView updateLocTailor, accDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_more_tailor, container, false);

        signOutTailor = v.findViewById(R.id.tvLogoutTailor);
        updateLocTailor = v.findViewById(R.id.tvLocationTailor);
        accDetails = v.findViewById(R.id.tvAccDetailsTailor);

        signOutTailor.setOnClickListener(v1 -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        updateLocTailor.setOnClickListener(v12 -> {
            startActivity(new Intent(getActivity(), MapsActivity.class));
        });

        accDetails.setOnClickListener(v13 -> {
            startActivity(new Intent(getActivity(),AccountDetails.class));
        });
        return v;
    }
}