package com.example.sewit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AccountDetails extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView updateUsername, updateEmail, updateNumber, updateLat, updateLng, updateAddress,updateRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        updateUsername = findViewById(R.id.DBUsername);
        updateEmail = findViewById(R.id.DBEmail);
        updateNumber = findViewById(R.id.DBNumber);
        updateLat = findViewById(R.id.DBLatitude);
        updateLng = findViewById(R.id.DBLongitude);
        updateAddress = findViewById(R.id.DBAddress);
        updateRating = findViewById(R.id.DBRating);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        FirebaseUser user = fAuth.getCurrentUser();
        DocumentReference df = fStore.collection("Users").document(user.getUid());


        df.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){

                        String user_name = Objects.requireNonNull(documentSnapshot.get("UserName")).toString();
                        String email = Objects.requireNonNull(documentSnapshot.get("Email")).toString();
                        String phone_number = Objects.requireNonNull(documentSnapshot.get("PhoneNumber")).toString();

                        updateUsername.setText(user_name);
                        updateEmail.setText(email);
                        updateNumber.setText(phone_number);

                        //if specific fields are there only - then update the field values
                        if(documentSnapshot.contains("latitude")){
                            String lat = Objects.requireNonNull(documentSnapshot.get("latitude")).toString();
                            updateLat.setText(lat);
                        }

                        if(documentSnapshot.contains("longitude")){
                            String lng = Objects.requireNonNull(documentSnapshot.get("longitude")).toString();
                            updateLng.setText(lng);
                        }

                        if(documentSnapshot.contains("latitude")){
                            String address = Objects.requireNonNull(documentSnapshot.get("StreetAddress")).toString();
                            updateAddress.setText(address);
                        }

                        if(documentSnapshot.contains("rating")){
                            String rating = Objects.requireNonNull(documentSnapshot.get("rating")).toString();
                            updateRating.setText(rating);
                            updateRating.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_star,0);
                            updateRating.setCompoundDrawablePadding(4);
                        }

                    }else{
                        Toast.makeText(AccountDetails.this,"Document does not exist", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AccountDetails.this,"Error while loading", Toast.LENGTH_SHORT).show();
                });

    }

}