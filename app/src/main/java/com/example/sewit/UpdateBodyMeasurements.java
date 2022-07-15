package com.example.sewit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UpdateBodyMeasurements extends AppCompatActivity {

    TextInputEditText neck, shoulder, sleeve, waist, chest, hip, calf,
            wrist, centerBack, crotchLength, inseam, outSeam;
    Button saveBtn;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_body_measurements);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        saveBtn = findViewById(R.id.saveMeasurementsBtn);
        neck = findViewById(R.id.inputNeck);
        shoulder = findViewById(R.id.inputShoulder);
        sleeve = findViewById(R.id.inputSleeve);
        waist = findViewById(R.id.inputWaist);
        chest = findViewById(R.id.inputChest);
        hip = findViewById(R.id.inputHip);
        calf = findViewById(R.id.inputCalf);
        wrist = findViewById(R.id.inputWrist);
        centerBack = findViewById(R.id.inputCenterBack);
        crotchLength = findViewById(R.id.inputCrotchLength);
        inseam = findViewById(R.id.inputInseam);
        outSeam = findViewById(R.id.inputOutSeam);

        FirebaseUser user1 = fAuth.getCurrentUser();
        DocumentReference df2 = fStore.collection("Users").document(user1.getUid())
                .collection("Body measurements").document("measurements");

        df2.get()
                .addOnSuccessListener(documentSnapshot2 -> {
                    if(documentSnapshot2.exists()){

                        if(documentSnapshot2.contains("neck")){
                            String Neck = Objects.requireNonNull(documentSnapshot2.get("neck")).toString();
                            neck.setText(Neck);
                        }

                        if(documentSnapshot2.contains("shoulder length")){
                            String Shoulder = Objects.requireNonNull(documentSnapshot2.get("shoulder length")).toString();
                            shoulder.setText(Shoulder);
                        }
                        if(documentSnapshot2.contains("sleeve length")){
                            String Sleeve = Objects.requireNonNull(documentSnapshot2.get("sleeve length")).toString();
                            sleeve.setText(Sleeve);
                        }
                        if(documentSnapshot2.contains("chest")){
                            String Chest = Objects.requireNonNull(documentSnapshot2.get("chest")).toString();
                            chest.setText(Chest);
                        }
                        if(documentSnapshot2.contains("waist")){
                            String Waist = Objects.requireNonNull(documentSnapshot2.get("waist")).toString();
                            waist.setText(Waist);
                        }

                        if(documentSnapshot2.contains("hip")){
                            String Hip = Objects.requireNonNull(documentSnapshot2.get("hip")).toString();
                            hip.setText(Hip);
                        }

                        if(documentSnapshot2.contains("center back")){
                            String CenterBack = Objects.requireNonNull(documentSnapshot2.get("center back")).toString();
                            centerBack.setText(CenterBack);
                        }

                        if(documentSnapshot2.contains("crotch length")){
                            String CrotchLength = Objects.requireNonNull(documentSnapshot2.get("crotch length")).toString();
                            crotchLength.setText(CrotchLength);
                        }

                        if(documentSnapshot2.contains("wrist")){
                            String Wrist = Objects.requireNonNull(documentSnapshot2.get("wrist")).toString();
                            wrist.setText(Wrist);
                        }


                        if(documentSnapshot2.contains("calf")){
                            String Calf = Objects.requireNonNull(documentSnapshot2.get("calf")).toString();
                            calf.setText(Calf);
                        }

                        if(documentSnapshot2.contains("inseam")){
                            String Inseam = Objects.requireNonNull(documentSnapshot2.get("inseam")).toString();
                            inseam.setText(Inseam);
                        }

                        if(documentSnapshot2.contains("outSeam")){
                            String OutSeam = Objects.requireNonNull(documentSnapshot2.get("outSeam")).toString();
                            outSeam.setText(OutSeam);
                        }

                    }
                })
                .addOnFailureListener(e -> Toast.makeText(UpdateBodyMeasurements.this,"Error while loading", Toast.LENGTH_SHORT).show());

        saveBtn.setOnClickListener(v -> {
            checkValidity();

            if(valid){
                FirebaseUser user = fAuth.getCurrentUser();

                DocumentReference df = fStore.collection("Users").document(user.getUid());
                String id = fStore.collection("Users").document(user.getUid()).collection("Body measurements").document("measurements").getId();
                Map<String,Object> userInfo = new HashMap<>();
                userInfo.put("neck", Objects.requireNonNull(neck.getText()).toString());
                userInfo.put("shoulder length", Objects.requireNonNull(shoulder.getText()).toString());
                userInfo.put("sleeve length", Objects.requireNonNull(sleeve.getText()).toString());
                userInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                userInfo.put("chest", Objects.requireNonNull(chest.getText()).toString());
                userInfo.put("hip", Objects.requireNonNull(hip.getText()).toString());
                userInfo.put("calf", Objects.requireNonNull(calf.getText()).toString());
                userInfo.put("wrist", Objects.requireNonNull(wrist.getText()).toString());
                userInfo.put("center back", Objects.requireNonNull(centerBack.getText()).toString());
                userInfo.put("crotch length", Objects.requireNonNull(crotchLength.getText()).toString());
                userInfo.put("inseam", Objects.requireNonNull(inseam.getText()).toString());
                userInfo.put("outSeam", Objects.requireNonNull(outSeam.getText()).toString());
                userInfo.put("ID",id);

                df.collection("Body measurements").document("measurements").set(userInfo);
                Toast.makeText(UpdateBodyMeasurements.this,"Saved", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public boolean checkValidity(){
        if(neck.getText().toString().isEmpty()){
            neck.setError("neck size is required");
            neck.requestFocus();
            valid = false;
        }

        if(shoulder.getText().toString().isEmpty()){
            shoulder.setError("shoulder length is required");
            shoulder.requestFocus();
            valid = false;
        }

        if(sleeve.getText().toString().isEmpty()){
            sleeve.setError("sleeve length is required");
            sleeve.requestFocus();
            valid = false;
        }

        if(waist.getText().toString().isEmpty()){
            waist.setError("waist size is required");
            waist.requestFocus();
            valid = false;
        }

        if(chest.getText().toString().isEmpty()){
            chest.setError("chest size is required");
            chest.requestFocus();
            valid = false;
        }

        if(hip.getText().toString().isEmpty()){
            hip.setError("hip size is required");
            hip.requestFocus();
            valid = false;
        }

        if(calf.getText().toString().isEmpty()){
            calf.setError("calf size is required");
            calf.requestFocus();
            valid = false;
        }

        if(wrist.getText().toString().isEmpty()){
            wrist.setError("wrist size is required");
            wrist.requestFocus();
            valid = false;
        }

        if(centerBack.getText().toString().isEmpty()){
            centerBack.setError("center back is required");
            centerBack.requestFocus();
            valid = false;
        }

        if(crotchLength.getText().toString().isEmpty()){
            crotchLength.setError("Crotch length is required");
            crotchLength.requestFocus();
            valid = false;
        }

        if(inseam.getText().toString().isEmpty()){
            inseam.setError("Inseam is required");
            inseam.requestFocus();
            valid = false;
        }

        if(outSeam.getText().toString().isEmpty()){
            outSeam.setError("OutSeam is required");
            outSeam.requestFocus();
            valid = false;
        }
        else{
            valid = true;
        }
        return valid;
    }
}