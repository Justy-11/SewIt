package com.example.sewit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


        BottomNavigationView navigationView = findViewById(R.id.botNavCustomer);
        navigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new CreateReqFragment()).commit();

    }

    @SuppressLint("NonConstantResourceId")
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.createNewReq:
                        selectedFragment = new CreateReqFragment();
                        break;
                    case R.id.acceptedReq:
                        selectedFragment = new AcceptedReqFragment();
                        break;
                    case R.id.orderStatus:
                        selectedFragment = new OrderStatusFragment();
                        break;
                    case R.id.moreItem:
                        selectedFragment = new MoreCustFragment();
                        break;
                }
                assert selectedFragment != null;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
                return true;
            };
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
       inflater.inflate(R.menu.example_menu,menu);
        return true;
    }
    */

}