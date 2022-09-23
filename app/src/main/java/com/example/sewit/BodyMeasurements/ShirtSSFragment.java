package com.example.sewit.BodyMeasurements;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sewit.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ShirtSSFragment extends Fragment {

    TextInputEditText neck,bust,waist,shoulder_length,sleeve_length
            ,shirt_length,armhole,sleeve_width;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shirt_s_s, container, false);

        neck = v.findViewById(R.id.neck);
        waist = v.findViewById(R.id.waist);
        shoulder_length = v.findViewById(R.id.shoulder);
        bust = v.findViewById(R.id.bust);
        sleeve_length = v.findViewById(R.id.sleeveLength);
        shirt_length = v.findViewById(R.id.shirtLength);
        armhole = v.findViewById(R.id.armhole);
        sleeve_width = v.findViewById(R.id.sleeveWidth);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //get data from firebase
        FirebaseUser user = fAuth.getCurrentUser();
        assert user != null;
        DocumentReference df = fStore.collection("Users").document(user.getUid())
                .collection("Body measurements").document("measurements");

        df.get()
                .addOnSuccessListener(snapshot -> {
                    if(snapshot.exists()){

                        if(snapshot.contains("waist")){
                            String Waist = Objects.requireNonNull(snapshot.get("waist")).toString();
                            waist.setText(Waist);
                        }

                        if(snapshot.contains("sleeve length")){
                            String Sleeve = Objects.requireNonNull(snapshot.get("sleeve length")).toString();
                            sleeve_length.setText(Sleeve);
                        }

                        if(snapshot.contains("shoulder length")){
                            String Shoulder = Objects.requireNonNull(snapshot.get("shoulder length")).toString();
                            shoulder_length.setText(Shoulder);
                        }

                        if(snapshot.contains("sleeve length")){
                            String Sleeve = Objects.requireNonNull(snapshot.get("sleeve length")).toString();
                            sleeve_length.setText(Sleeve);
                        }
                        if(snapshot.contains("chest")){
                            String Chest = Objects.requireNonNull(snapshot.get("chest")).toString();
                            bust.setText(Chest);
                        }

                    }

                }).addOnFailureListener(e -> Toast.makeText(getContext(),"Error while loading", Toast.LENGTH_SHORT).show());


        return v;
    }
}