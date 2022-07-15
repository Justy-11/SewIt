package com.example.sewit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
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

import java.io.ByteArrayOutputStream;

public class OrderDetails extends AppCompatActivity {

    TextView customerNumber,neck,shoulder,sleeve,chest,waist,customerName,tailorName
            ,hip,centerBack,crotchLength,wrist,calf,inseam,outSeam,note,orderId,gender,item,orderPlaced,distance,price;
    ImageView photo;
    Uri imageUri;
    StorageReference storageReference;
    FirebaseFirestore fStore;
    ProgressDialog progressDialog;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        customerNumber = findViewById(R.id.tvGetContact);
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
        gender = findViewById(R.id.tvGetGender);
        item = findViewById(R.id.tvGetRequestedItem);
        photo = findViewById(R.id.tvGetPhoto);
        price = findViewById(R.id.tvGetPrice);
        orderPlaced = findViewById(R.id.tvGetOrderPlaced);
        distance = findViewById(R.id.tvGetDistance);
        customerName = findViewById(R.id.tvGetName);
        tailorName = findViewById(R.id.tvGetTo);


        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("uploads_orders");

        progressDialog = new ProgressDialog(OrderDetails.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        if(getIntent().hasExtra("Selected_card")){
            TailorOrderData tailorOrderData = getIntent().getParcelableExtra("Selected_card");

            String order_id = tailorOrderData.getOrderId();

            CollectionReference df = fStore.collection("Users");

            df.whereEqualTo("isCustomer","1")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots){
                            String customerId = snapshot.getId();
                            CollectionReference cr =  fStore.collection("Users")
                                    .document(customerId).collection("Orders");

                            cr.whereEqualTo("orderId",order_id)
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                        for(QueryDocumentSnapshot snapshot1: queryDocumentSnapshots1) {

                                            orderId.setText(snapshot1.getString("orderId"));
                                            customerName.setText(snapshot1.getString("customerName"));
                                            shoulder.setText(String.valueOf(snapshot1.get("shoulder")));
                                            customerNumber.setText(snapshot1.getString("customerNumber"));
                                            neck.setText(String.valueOf(snapshot1.get("neck")));
                                            sleeve.setText(String.valueOf(snapshot1.get("sleeve")));
                                            chest.setText(String.valueOf(snapshot1.get("chest")));
                                            waist.setText(String.valueOf(snapshot1.get("waist")));
                                            centerBack.setText(String.valueOf(snapshot1.get("centerBack")));
                                            crotchLength.setText(String.valueOf(snapshot1.get("crotchLength")));
                                            wrist.setText(String.valueOf(snapshot1.get("wrist")));
                                            calf.setText(String.valueOf(snapshot1.get("calf")));
                                            hip.setText(String.valueOf(snapshot1.get("hip")));
                                            inseam.setText(String.valueOf(snapshot1.get("inseam")));
                                            outSeam.setText(String.valueOf(snapshot1.get("outSeam")));
                                            tailorName.setText(snapshot1.getString("tailorName"));
                                            if(snapshot1.contains("note")) {
                                                note.setText(snapshot1.getString("note"));
                                            }
                                            if(snapshot1.contains("imageUrl")) {
                                                String uri = snapshot1.getString("imageUrl");
                                                //TODO: Also can change dimensions of photos
                                                Glide.with(OrderDetails.this)
                                                        .load(uri)
                                                        .into(photo);
                                            }
                                            gender.setText(snapshot1.getString("gender"));
                                            item.setText(snapshot1.getString("item"));
                                            orderPlaced.setText(String.valueOf(snapshot1.getDate("orderPlacedAt")));
                                            distance.setText(String.valueOf(snapshot1.get("distance")));
                                            price.setText(String.valueOf(snapshot1.get("price")));

                                            if(progressDialog.isShowing()){
                                                progressDialog.dismiss();
                                            }

                                        }
                                    }).addOnFailureListener(e -> Toast.makeText(OrderDetails.this,"Could not find orderId", Toast.LENGTH_SHORT).show());

                        }
                    }).addOnFailureListener(e -> Toast.makeText(OrderDetails.this,"Error", Toast.LENGTH_SHORT).show());
        }

    }

    public Uri getImageUri(Context context, Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),image,"Title",null);
        return Uri.parse(path);
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}