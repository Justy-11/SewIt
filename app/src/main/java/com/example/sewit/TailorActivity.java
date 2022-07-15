package com.example.sewit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TailorActivity extends AppCompatActivity {

    private static final String TAG = "TailorActivity";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailor);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        df.get().addOnSuccessListener(documentSnapshot -> {
            if(!(documentSnapshot.contains("longitude") && documentSnapshot.contains("latitude"))){
                // alert dialog box to update the location
                LocationUpdateAlertDialog locationUpdateAlertDialog = new LocationUpdateAlertDialog();
                locationUpdateAlertDialog.show(getSupportFragmentManager(),"Alert dialog");
            }
        });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    //send token to firebase
                    fStore.collection("Users").document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid()).get()
                            .addOnSuccessListener(snapshot -> {
                                String username = snapshot.getString("UserName");
                                DocumentReference tokenDocRef = fStore.collection("Tokens").document(username+"sewIt");
                                Map<String,Object> map = new HashMap<>();
                                map.put("name",username);
                                map.put("token",token);

                                tokenDocRef.set(map);
                            });

                    Log.d(TAG, "Refreshed token: " + token);

                });


        BottomNavigationView navigationView = findViewById(R.id.botNavTailor);
        navigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer2, new RequestsFragment()).commit();

    }

    @SuppressLint("NonConstantResourceId")
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.newReq:
                        selectedFragment = new RequestsFragment();
                        break;
                    case R.id.newOrders:
                        selectedFragment = new OrdersFragment();
                        break;
                    case R.id.pendingOrders:
                        selectedFragment = new PendingOrdersFragment();
                        break;
                    case R.id.moreItemTailor:
                        selectedFragment = new MoreTailorFragment();
                        break;
                }
                assert selectedFragment != null;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer2, selectedFragment).commit();
                return true;
            };

    @Override
    protected void onStart() {
        super.onStart();
        // retrieve tailor rating
        fStore.collection("OrderStatus").whereEqualTo("tailorId", Objects.requireNonNull(fAuth.getCurrentUser()).getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    float rating = 0;
                    int count = 0;

                    for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots)
                    {
                        if(queryDocumentSnapshot.contains("rating")){
                            count++;
                            //get rating
                            rating = rating + Integer.parseInt(String.valueOf(queryDocumentSnapshot.get("rating")));
                        }

                    }

                    float ratingAvg = rating/count;

                    //update to firebase
                    Map <String,Object> map  = new HashMap<>();
                    map.put("rating",Math.round(ratingAvg));
                    fStore.collection("Users").document(fAuth.getCurrentUser().getUid()).update(map);
                });

    }
}
