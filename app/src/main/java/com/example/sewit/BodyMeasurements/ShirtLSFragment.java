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

public class ShirtLSFragment extends Fragment {

    TextInputEditText neck,bust,waist,shoulder_length,sleeve_length,wrist
            ,shirt_length,bicep_around,sleeve_width;
    Button confirm_btn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shirt_l_s, container, false);

        neck = v.findViewById(R.id.neck);
        waist = v.findViewById(R.id.waist);
        shoulder_length = v.findViewById(R.id.shoulder);
        bust = v.findViewById(R.id.bust);
        sleeve_length = v.findViewById(R.id.sleeveLength);
        wrist = v.findViewById(R.id.wrist);
        shirt_length = v.findViewById(R.id.shirtLength);
        bicep_around = v.findViewById(R.id.bicepAround);
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

                        if(snapshot.contains("wrist")){
                            String Wrist = Objects.requireNonNull(snapshot.get("wrist")).toString();
                            wrist.setText(Wrist);
                        }

                    }

                }).addOnFailureListener(e -> Toast.makeText(getContext(),"Error while loading", Toast.LENGTH_SHORT).show());

        confirm_btn.setOnClickListener(v1 -> {
            //check all fields are filled
            if(String.valueOf(neck.getText()).isEmpty() || String.valueOf(waist.getText()).isEmpty() ||
                    String.valueOf(shoulder_length.getText()).isEmpty() || String.valueOf(bust.getText()).isEmpty() ||
                    String.valueOf(sleeve_length.getText()).isEmpty() || String.valueOf(shirt_length.getText()).isEmpty()||
                    String.valueOf(wrist.getText()).isEmpty() || String.valueOf(bicep_around.getText()).isEmpty() ||
                    String.valueOf(sleeve_width.getText()).isEmpty()){
                Toast.makeText(getContext(),"All fields should be filled", Toast.LENGTH_SHORT).show();
            }
            else{

                Bundle bundle = new Bundle();
                bundle.putString("neck", String.valueOf(neck.getText()));
                bundle.putString("waist", String.valueOf(waist.getText()));
                bundle.putString("shoulder_length", String.valueOf(shoulder_length.getText()));
                bundle.putString("chest", String.valueOf(bust.getText()));
                bundle.putString("sleeve_length", String.valueOf(sleeve_length.getText()));
                bundle.putString("shirt_length", String.valueOf(shirt_length.getText()));
                bundle.putString("wrist", String.valueOf(wrist.getText()));
                bundle.putString("bicep_around", String.valueOf(bicep_around.getText()));
                bundle.putString("sleeve_width", String.valueOf(sleeve_width.getText()));

                getParentFragmentManager().setFragmentResult("ShirtLS",bundle);

                Toast.makeText(getContext(),"Saved", Toast.LENGTH_SHORT).show();

            }
        });

        return v;
    }
}