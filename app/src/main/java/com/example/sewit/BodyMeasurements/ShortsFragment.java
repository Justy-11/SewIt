package com.example.sewit.BodyMeasurements;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.sewit.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ShortsFragment extends Fragment {

    TextInputEditText hip,waist,crotch_length
            ,shorts_length,thigh_around,leg_opening;
    Button confirm_btn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shorts, container, false);

        hip = v.findViewById(R.id.hip);
        waist = v.findViewById(R.id.waist);
        crotch_length = v.findViewById(R.id.crotchLength);
        shorts_length = v.findViewById(R.id.shortLength);
        thigh_around = v.findViewById(R.id.thigh);
        leg_opening = v.findViewById(R.id.legOpening);
        confirm_btn = v.findViewById(R.id.confirmBtn);

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

                        if(snapshot.contains("crotch length")){
                            String CrotchLength = Objects.requireNonNull(snapshot.get("crotch length")).toString();
                            crotch_length.setText(CrotchLength);
                        }
                    }

                }).addOnFailureListener(e -> Toast.makeText(getContext(),"Error while loading", Toast.LENGTH_SHORT).show());

        confirm_btn.setOnClickListener(v1 -> {
            //check all fields are filled
            if(String.valueOf(hip.getText()).isEmpty() || String.valueOf(waist.getText()).isEmpty() ||
                    String.valueOf(crotch_length.getText()).isEmpty() || String.valueOf(shorts_length.getText()).isEmpty() ||
                    String.valueOf(thigh_around.getText()).isEmpty() || String.valueOf(leg_opening.getText()).isEmpty()){
                Toast.makeText(getContext(),"All fields should be filled", Toast.LENGTH_SHORT).show();
            }
            else{

                Bundle bundle = new Bundle();
                bundle.putString("hip", String.valueOf(hip.getText()));
                bundle.putString("waist", String.valueOf(waist.getText()));
                bundle.putString("crotch_length", String.valueOf(crotch_length.getText()));
                bundle.putString("shorts_length", String.valueOf(shorts_length.getText()));
                bundle.putString("thigh_around", String.valueOf(thigh_around.getText()));
                bundle.putString("leg_opening", String.valueOf(leg_opening.getText()));

                getParentFragmentManager().setFragmentResult("Shorts",bundle);

                Toast.makeText(getContext(),"Saved", Toast.LENGTH_SHORT).show();

            }
        });

        return v;
    }
}