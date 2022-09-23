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

public class SalwarFragment extends Fragment {

    TextInputEditText hip,bust,waist,sleeve_length,calf,salwar_length
    ,kameez_length,bottom,neck_front,neck_back,sleeve_width,above_around_waist,
    knee_around,thigh_around;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_salwar, container, false);

        hip = v.findViewById(R.id.hip);
        waist = v.findViewById(R.id.waist);
        calf = v.findViewById(R.id.calf);
        bust = v.findViewById(R.id.bust);
        sleeve_length = v.findViewById(R.id.sleeveLength);
        salwar_length = v.findViewById(R.id.salwarLength);
        kameez_length = v.findViewById(R.id.kameezLength);
        bottom = v.findViewById(R.id.bottom);
        neck_front = v.findViewById(R.id.neckFront);
        neck_back = v.findViewById(R.id.neckBack);
        sleeve_width = v.findViewById(R.id.sleeveWidth);
        above_around_waist = v.findViewById(R.id.aroundAboveWaist);
        knee_around = v.findViewById(R.id.knee);
        thigh_around = v.findViewById(R.id.thigh);

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

                        if(snapshot.contains("hip")){
                            String Hip = Objects.requireNonNull(snapshot.get("hip")).toString();
                            hip.setText(Hip);
                        }

                        if(snapshot.contains("calf")){
                            String Calf = Objects.requireNonNull(snapshot.get("calf")).toString();
                            calf.setText(Calf);
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