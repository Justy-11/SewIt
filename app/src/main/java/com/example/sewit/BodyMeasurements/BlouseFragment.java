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

public class BlouseFragment extends Fragment {

    TextInputEditText blouse_length,armhole,lower_bust,bust,
            shoulder_length,neck_front,neck_back,sleeve_length,sleeve_width;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_blouse, container, false);

        blouse_length = v.findViewById(R.id.blouseLength);
        armhole = v.findViewById(R.id.armhole);
        lower_bust = v.findViewById(R.id.lowerBust);
        bust = v.findViewById(R.id.bust);
        shoulder_length = v.findViewById(R.id.shoulder);
        neck_front = v.findViewById(R.id.neckFront);
        neck_back = v.findViewById(R.id.neckBack);
        sleeve_length = v.findViewById(R.id.sleeveLength);
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