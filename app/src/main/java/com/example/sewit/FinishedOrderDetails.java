package com.example.sewit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class FinishedOrderDetails extends AppCompatActivity {

    public static final String EXTRA_LNG = "com.example.sewit.EXTRA_LNG";
    public static final String EXTRA_LAT = "com.example.sewit.EXTRA_LAT";
    TextView neck,shoulder,sleeve,chest,waist,customerName,tailorName
            ,hip,centerBack,crotchLength,wrist,calf,inseam,outSeam,note,
            orderId,item,orderPlaced,distance,price,status,orderFinished;
    ImageView photo;
    StorageReference storageReference;
    FirebaseFirestore fStore;
    ProgressDialog progressDialog;
    FirebaseAuth fAuth;
    Button showLocBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_order_details);

        neck = findViewById(R.id.tvGetNeckSize);
        shoulder = findViewById(R.id.tvGetShoulder);
        sleeve = findViewById(R.id.tvGetSleeve);
        chest = findViewById(R.id.tvGetChest);
        waist = findViewById(R.id.tvGetWaist);
        hip = findViewById(R.id.tvGetHip);
        centerBack = findViewById(R.id.tvGetCenterBack);
        crotchLength = findViewById(R.id.tvGetCrotch);
        wrist = findViewById(R.id.tvGetWrist);
        calf = findViewById(R.id.tvGetCalf);
        inseam = findViewById(R.id.tvGetInseam);
        outSeam = findViewById(R.id.tvGetOutSeam);
        note = findViewById(R.id.tvGetNote);
        orderId = findViewById(R.id.tvGetOrderId);
        item = findViewById(R.id.tvGetItem);
        photo = findViewById(R.id.tvGetPhoto);
        price = findViewById(R.id.tvGetPrice);
        orderPlaced = findViewById(R.id.tvGetOrderPlaced);
        distance = findViewById(R.id.tvGetDistance);
        customerName = findViewById(R.id.tvGetName);
        tailorName = findViewById(R.id.tvGetTo);
        status = findViewById(R.id.tvGetStatus);
        orderFinished = findViewById(R.id.tvGetOrderFinished);
        showLocBtn = findViewById(R.id.btnShowLocation);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("uploads_orders");

        progressDialog = new ProgressDialog(FinishedOrderDetails.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();


        if(getIntent().hasExtra("Selected_card")){
            FinishedOrderData finishedOrderData = getIntent().getParcelableExtra("Selected_card");

            String order_id = finishedOrderData.getOrderId();

            CollectionReference df = fStore.collection("OrderStatus");

            df.whereEqualTo("orderId",order_id)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots){

                            orderId.setText(snapshot.getString("orderId"));
                            customerName.setText(snapshot.getString("customerName"));
                            shoulder.setText(String.valueOf(snapshot.get("shoulder")));
                            neck.setText(String.valueOf(snapshot.get("neck")));
                            sleeve.setText(String.valueOf(snapshot.get("sleeve")));
                            chest.setText(String.valueOf(snapshot.get("chest")));
                            waist.setText(String.valueOf(snapshot.get("waist")));
                            centerBack.setText(String.valueOf(snapshot.get("centerBack")));
                            crotchLength.setText(String.valueOf(snapshot.get("crotchLength")));
                            wrist.setText(String.valueOf(snapshot.get("wrist")));
                            calf.setText(String.valueOf(snapshot.get("calf")));
                            hip.setText(String.valueOf(snapshot.get("hip")));
                            inseam.setText(String.valueOf(snapshot.get("inseam")));
                            outSeam.setText(String.valueOf(snapshot.get("outSeam")));
                            tailorName.setText(snapshot.getString("tailorName"));
                            if(snapshot.contains("note")) {
                                note.setText(snapshot.getString("note"));
                            }
                            if(snapshot.contains("imageUrl")) {
                                String uri = snapshot.getString("imageUrl");
                                //TODO: Also can change dimensions of photos
                                Glide.with(FinishedOrderDetails.this)
                                        .load(uri)
                                        .into(photo);
                            }
                            item.setText(snapshot.getString("item"));
                            orderPlaced.setText(String.valueOf(snapshot.getDate("orderPlacedAt")));
                            distance.setText(String.valueOf(snapshot.get("distance")));
                            price.setText(String.valueOf(snapshot.get("price")));
                            status.setText(snapshot.getString("status"));
                            orderFinished.setText(String.valueOf(snapshot.getDate("orderFinishedAt")));


                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }


                            showLocBtn.setOnClickListener(v -> {
                                String lng = String.valueOf(snapshot.get("customerLng"));
                                String lat = String.valueOf(snapshot.get("customerLat"));

                                Intent intent = new Intent(FinishedOrderDetails.this,MapsShowLocation.class);
                                // to pass customer location to map activity
                                intent.putExtra(EXTRA_LNG,lng);
                                intent.putExtra(EXTRA_LAT,lat);
                                startActivity(intent);
                            });


                        }
                    }).addOnFailureListener(e -> Toast.makeText(FinishedOrderDetails.this,"Error", Toast.LENGTH_SHORT).show());

        }
    }
}