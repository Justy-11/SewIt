package com.example.sewit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sewit.Service.sendFCM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OrderStatusDetails extends AppCompatActivity implements RatingDialog.DialogListener{

    TextView neck,shoulder,sleeve,chest,waist,customerName,tailorName
            ,hip,centerBack,crotchLength,wrist,calf,inseam,outSeam,note,orderId,item,orderPlaced,distance,price,status,orderFinished,
            rating,feedback;
    ImageView photo;
    StorageReference storageReference;
    FirebaseFirestore fStore;
    ProgressDialog progressDialog;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status_details);

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
        item = findViewById(R.id.tvGetRequestedItem);
        photo = findViewById(R.id.tvGetPhoto);
        price = findViewById(R.id.tvGetPrice);
        orderPlaced = findViewById(R.id.tvGetOrderPlaced);
        distance = findViewById(R.id.tvGetDistance);
        customerName = findViewById(R.id.tvGetName);
        tailorName = findViewById(R.id.tvGetTo);
        status = findViewById(R.id.tvGetStatus);
        orderFinished = findViewById(R.id.tvFinishedAt);
        rating = findViewById(R.id.rating);
        feedback = findViewById(R.id.feedback);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("uploads_orders");

        progressDialog = new ProgressDialog(OrderStatusDetails.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        if(getIntent().hasExtra("Selected_card")){
            OrderStatusData orderStatusData = getIntent().getParcelableExtra("Selected_card");

            String order_id = orderStatusData.getOrderId();

            CollectionReference df = fStore.collection("OrderStatus");

            df.whereEqualTo("orderId",order_id)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots){

                            String id = snapshot.getId();

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
                                Glide.with(OrderStatusDetails.this)
                                        .load(uri)
                                        .into(photo);
                            }
                            item.setText(snapshot.getString("item"));
                            orderPlaced.setText(String.valueOf(snapshot.getDate("orderPlacedAt")));
                            distance.setText(String.valueOf(snapshot.get("distance")));
                            price.setText(String.valueOf(snapshot.get("price")));
                            status.setText(snapshot.getString("status"));
                            if(Objects.equals(snapshot.getString("status"), "finished")){
                                orderFinished.setText(String.valueOf(snapshot.getDate("orderFinishedAt")));
                            }

                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            if(Objects.equals(snapshot.getString("status"), "delivered") && !snapshot.contains("rating")){
                                RatingDialog ratingDialog = new RatingDialog();
                                ratingDialog.show(getSupportFragmentManager(),"rating dialog");

                            }

                            rating.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    //update to firebase
                                    int Rating = Integer.parseInt(rating.getText().toString());
                                    Map<String,Object> map = new HashMap<>();
                                    map.put("rating",Rating);

                                    fStore.collection("OrderStatus").document(id).update(map);

                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });

                            feedback.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    String Feedback = feedback.getText().toString();
                                    Map<String,Object> map = new HashMap<>();
                                    map.put("feedback",Feedback);

                                    fStore.collection("OrderStatus").document(id).update(map);

                                    //send feedback notification to specific tailor
                                    fStore.collection("Tokens").document( tailorName.getText().toString() +"sewIt")
                                            .get()
                                            .addOnSuccessListener(snapshot -> {
                                                String token = snapshot.getString("token");
                                                sendFCM.pushNotification(getApplicationContext(),token,"Feedback (orderId: "
                                                                + orderId.getText().toString() + ")",feedback.getText().toString()
                                                                + " :" + customerName.getText().toString()
                                                        ,"OrdersFragment");
                                            });
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });


                        }
                    }).addOnFailureListener(e -> Toast.makeText(OrderStatusDetails.this,"Error", Toast.LENGTH_SHORT).show());
        }




    }

    @Override
    public void applyDetails(int Rating, String Feedback) {
        rating.setText(String.valueOf(Rating));
        feedback.setText(Feedback);
    }
}