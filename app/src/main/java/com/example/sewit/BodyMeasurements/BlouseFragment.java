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

public class BlouseFragment extends Fragment {

    TextInputEditText blouse_length,armhole,lower_bust,bust,
            shoulder_length,neck_front,neck_back,sleeve_length,sleeve_width;
    Button confirm_btn;
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

        confirm_btn.setOnClickListener(v1 -> {
            //check all fields are filled
            if(String.valueOf(blouse_length.getText()).isEmpty() || String.valueOf(armhole.getText()).isEmpty() ||
                    String.valueOf(lower_bust.getText()).isEmpty() || String.valueOf(bust.getText()).isEmpty() ||
                    String.valueOf(shoulder_length.getText()).isEmpty() || String.valueOf(neck_front.getText()).isEmpty()||
                    String.valueOf(neck_back.getText()).isEmpty() || String.valueOf(sleeve_length.getText()).isEmpty() ||
                    String.valueOf(sleeve_width.getText()).isEmpty()){
                Toast.makeText(getContext(),"All fields should be filled", Toast.LENGTH_SHORT).show();
            }
            else{

                Bundle bundle = new Bundle();
                bundle.putString("blouse_length", String.valueOf(blouse_length.getText()));
                bundle.putString("armhole", String.valueOf(armhole.getText()));
                bundle.putString("lower_bust", String.valueOf(lower_bust.getText()));
                bundle.putString("bust", String.valueOf(bust.getText()));
                bundle.putString("shoulder_length", String.valueOf(shoulder_length.getText()));
                bundle.putString("neck_front", String.valueOf(neck_front.getText()));
                bundle.putString("neck_back", String.valueOf(neck_back.getText()));
                bundle.putString("sleeve_length", String.valueOf(sleeve_length.getText()));
                bundle.putString("sleeve_width", String.valueOf(sleeve_width.getText()));
                getParentFragmentManager().setFragmentResult("Blouse",bundle);

                Toast.makeText(getContext(),"Saved", Toast.LENGTH_SHORT).show();

            }
        });

        return v;
    }
}