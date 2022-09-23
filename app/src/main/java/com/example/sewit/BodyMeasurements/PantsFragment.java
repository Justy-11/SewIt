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

public class PantsFragment extends Fragment {

    TextInputEditText hip,waist,crotch_length,in_seam,out_seam,calf
            ,thigh_around,knee_around,bottom;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pants, container, false);

        hip = v.findViewById(R.id.hip);
        waist = v.findViewById(R.id.waist);
        crotch_length = v.findViewById(R.id.crotchLength);
        in_seam = v.findViewById(R.id.inseam);
        out_seam = v.findViewById(R.id.outSeam);
        calf = v.findViewById(R.id.calf);
        thigh_around = v.findViewById(R.id.thigh);
        knee_around = v.findViewById(R.id.knee);
        bottom = v.findViewById(R.id.bottom);

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

                        if(snapshot.contains("calf")){
                            String Calf = Objects.requireNonNull(snapshot.get("calf")).toString();
                            calf.setText(Calf);
                        }

                        if(snapshot.contains("inseam")){
                            String Inseam = Objects.requireNonNull(snapshot.get("inseam")).toString();
                            in_seam.setText(Inseam);
                        }

                        if(snapshot.contains("outSeam")){
                            String OutSeam = Objects.requireNonNull(snapshot.get("outSeam")).toString();
                            out_seam.setText(OutSeam);
                        }
                    }

                }).addOnFailureListener(e -> Toast.makeText(getContext(),"Error while loading", Toast.LENGTH_SHORT).show());

        return v;
    }
}