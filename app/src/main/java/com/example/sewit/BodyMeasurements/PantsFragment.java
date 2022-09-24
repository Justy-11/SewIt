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

public class PantsFragment extends Fragment {

    TextInputEditText hip,waist,crotch_length,in_seam,out_seam,calf
            ,thigh_around,knee_around,bottom;
    Button confirm_btn;
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

        confirm_btn.setOnClickListener(v1 -> {
            //check all fields are filled
            if(String.valueOf(hip.getText()).isEmpty() || String.valueOf(waist.getText()).isEmpty() ||
                    String.valueOf(crotch_length.getText()).isEmpty() || String.valueOf(in_seam.getText()).isEmpty() ||
                    String.valueOf(out_seam.getText()).isEmpty() || String.valueOf(calf.getText()).isEmpty()||
                    String.valueOf(thigh_around.getText()).isEmpty() || String.valueOf(knee_around.getText()).isEmpty() ||
                    String.valueOf(bottom.getText()).isEmpty()){
                Toast.makeText(getContext(),"All fields should be filled", Toast.LENGTH_SHORT).show();
            }
            else{

                Bundle bundle = new Bundle();
                bundle.putString("hip", String.valueOf(hip.getText()));
                bundle.putString("waist", String.valueOf(waist.getText()));
                bundle.putString("crotch_length", String.valueOf(crotch_length.getText()));
                bundle.putString("in_seam", String.valueOf(in_seam.getText()));
                bundle.putString("out_seam", String.valueOf(out_seam.getText()));
                bundle.putString("thigh_around", String.valueOf(thigh_around.getText()));
                bundle.putString("bottom", String.valueOf(bottom.getText()));
                bundle.putString("knee_around", String.valueOf(knee_around.getText()));
                bundle.putString("calf", String.valueOf(calf.getText()));
                getParentFragmentManager().setFragmentResult("Pants",bundle);

                Toast.makeText(getContext(),"Saved", Toast.LENGTH_SHORT).show();

            }
        });

        return v;
    }
}