package com.example.sewit;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MoreCustFragment extends Fragment {

    TextView signOutCustomer;
    TextView updateLocCustomer;
    TextView updateBodyMeasurements;
    TextView accDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_more_cust, container, false);

        signOutCustomer = v.findViewById(R.id.tvLogout);
        updateLocCustomer = v.findViewById(R.id.tvLocation);
        accDetails = v.findViewById(R.id.tvAccDetails);
        updateBodyMeasurements = v.findViewById(R.id.tvBodyMeasurements);

        signOutCustomer.setOnClickListener(v1 -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        updateLocCustomer.setOnClickListener(v12 -> {
            startActivity(new Intent(getActivity(),MapsActivity.class));
        });

        accDetails.setOnClickListener(v13 -> {
            startActivity(new Intent(getActivity(),AccountDetails.class));
        });

        updateBodyMeasurements.setOnClickListener(v14 ->{
            startActivity(new Intent(getActivity(),UpdateBodyMeasurements.class));
        });

        return v;
    }


}