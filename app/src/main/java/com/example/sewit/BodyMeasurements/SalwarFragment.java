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

public class SalwarFragment extends Fragment {

    TextInputEditText hip,bust,waist,sleeve_length,calf,salwar_length
    ,kameez_length,bottom,neck_front,neck_back,sleeve_width,above_around_waist,
    knee_around,thigh_around;
    Button confirm_btn;
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

        confirm_btn.setOnClickListener(v1 -> {
            //check all fields are filled
            if(String.valueOf(hip.getText()).isEmpty() || String.valueOf(waist.getText()).isEmpty() ||
                    String.valueOf(calf.getText()).isEmpty() || String.valueOf(bust.getText()).isEmpty() ||
                    String.valueOf(sleeve_length.getText()).isEmpty() || String.valueOf(salwar_length.getText()).isEmpty()||
                    String.valueOf(kameez_length.getText()).isEmpty() || String.valueOf(bottom.getText()).isEmpty() ||
                    String.valueOf(neck_front.getText()).isEmpty() || String.valueOf(neck_back.getText()).isEmpty() ||
                    String.valueOf(sleeve_width.getText()).isEmpty() || String.valueOf(above_around_waist.getText()).isEmpty() ||
                    String.valueOf(knee_around.getText()).isEmpty() || String.valueOf(thigh_around.getText()).isEmpty()){
                Toast.makeText(getContext(),"All fields should be filled", Toast.LENGTH_SHORT).show();
            }
            else{

                Bundle bundle = new Bundle();
                bundle.putString("hip", String.valueOf(hip.getText()));
                bundle.putString("waist", String.valueOf(waist.getText()));
                bundle.putString("calf", String.valueOf(calf.getText()));
                bundle.putString("bust", String.valueOf(bust.getText()));
                bundle.putString("sleeve_length", String.valueOf(sleeve_length.getText()));
                bundle.putString("salwar_length", String.valueOf(salwar_length.getText()));
                bundle.putString("kameez_length", String.valueOf(kameez_length.getText()));
                bundle.putString("bottom", String.valueOf(bottom.getText()));
                bundle.putString("neck_front", String.valueOf(neck_front.getText()));
                bundle.putString("neck_back", String.valueOf(neck_back.getText()));
                bundle.putString("sleeve_width", String.valueOf(sleeve_width.getText()));
                bundle.putString("above_around_waist", String.valueOf(above_around_waist.getText()));
                bundle.putString("knee_around", String.valueOf(knee_around.getText()));
                bundle.putString("thigh_around", String.valueOf(thigh_around.getText()));

                getParentFragmentManager().setFragmentResult("Salwar",bundle);

                Toast.makeText(getContext(),"Saved", Toast.LENGTH_SHORT).show();

            }
        });

        return v;
    }
}