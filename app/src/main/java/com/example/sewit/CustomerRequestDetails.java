package com.example.sewit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Network;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sewit.Service.sendFCM;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CustomerRequestDetails extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    TextView customerNumber,neck,shoulder,sleeve,chest,waist,customerName,tailorName
            ,hip,centerBack,crotchLength,wrist,calf,inseam,outSeam,note,reqId,gender,item,created,accepted,distance,id,price;
    ImageView photo;
    Button placeOrderBtn,addLocBtn;
    Uri imageUri;
    StorageReference storageReference;
    FirebaseFirestore fStore;
    ProgressDialog progressDialog;
    FirebaseAuth fAuth;
    TextInputEditText lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_request_details);

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
        reqId = findViewById(R.id.tvGetReqId);
        gender = findViewById(R.id.tvGetGender1);
        item = findViewById(R.id.tvGetRequestedItem);
        photo = findViewById(R.id.tvGetPhoto);
        price = findViewById(R.id.tvGetPrice);
        created = findViewById(R.id.tvGetCreated);
        accepted = findViewById(R.id.tvGetAccepted);
        placeOrderBtn = findViewById(R.id.btnAccept);
        distance = findViewById(R.id.tvGetDistance);
        id = findViewById(R.id.tvGetTailorID);
        customerName = findViewById(R.id.tvGetName);
        tailorName = findViewById(R.id.tvGetTo);
        addLocBtn = findViewById(R.id.btnAddLocationAddress);
        lat = findViewById(R.id.inputLat);
        lng = findViewById(R.id.inputLng);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("uploads_orders");

        progressDialog = new ProgressDialog(CustomerRequestDetails.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        if(getIntent().hasExtra("Selected_card")){
            CustomerAcceptedReqData customerAcceptedReqData = getIntent().getParcelableExtra("Selected_card");

            String req_id = customerAcceptedReqData.getReqId();

            CollectionReference df = fStore.collection("Users");

            df.whereEqualTo("isTailor","1")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots){
                            String tailorId = snapshot.getId();
                            CollectionReference cr =  fStore.collection("Users")
                                    .document(tailorId).collection("AcceptedCustomerRequests");

                            cr.whereEqualTo("reqId",req_id)
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                        for(QueryDocumentSnapshot snapshot1: queryDocumentSnapshots1) {
                                            //TODO: retrieve Data to this activity and set texts
                                            reqId.setText(snapshot1.getString("reqId"));
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
                                                Glide.with(CustomerRequestDetails.this)
                                                        .load(uri)
                                                        .into(photo);
                                            }
                                            gender.setText(snapshot1.getString("gender"));
                                            item.setText(snapshot1.getString("item"));
                                            created.setText(String.valueOf(snapshot1.get("createdAt")));
                                            accepted.setText(String.valueOf(snapshot1.getDate("acceptedAt")));
                                            distance.setText(String.valueOf(snapshot1.get("distance")));
                                            price.setText(String.valueOf(snapshot1.get("price")));
                                            id.setText(tailorId);

                                            if(progressDialog.isShowing()){
                                                progressDialog.dismiss();
                                            }

                                        }
                                    }).addOnFailureListener(e -> Toast.makeText(CustomerRequestDetails.this,"Could find the reqId", Toast.LENGTH_SHORT).show());

                        }
                    }).addOnFailureListener(e -> Toast.makeText(CustomerRequestDetails.this,"Could not find customers", Toast.LENGTH_SHORT).show());
        }

        addLocBtn.setOnClickListener(v -> {
            FirebaseUser user = fAuth.getCurrentUser();
            assert user != null;
            DocumentReference df = fStore.collection("Users").document(user.getUid());
            df.get()
                    .addOnSuccessListener(snapshot -> {
                        String latitude = String.valueOf(snapshot.get("latitude"));
                        String longitude = String.valueOf(snapshot.get("longitude"));

                        lat.setText(latitude);
                        lng.setText(longitude);

                    });
        });

        placeOrderBtn.setOnClickListener(v -> {

            if(!(String.valueOf(lat.getText()).isEmpty()) && !(String.valueOf(lng.getText()).isEmpty())){

                Toast.makeText(CustomerRequestDetails.this,"Order placed", Toast.LENGTH_SHORT).show();

                //send order notification to tailor - 13/7/2022
                fStore.collection("Tokens").document(tailorName.getText().toString() +"sewIt")
                        .get()
                        .addOnSuccessListener(snapshot -> {
                            String token = snapshot.getString("token");
                            sendFCM.pushNotification(this,token, "Order","New order from " + customerName.getText().toString()
                            ,"OrdersFragment");
                        });

                FirebaseUser user = fAuth.getCurrentUser();
                assert user != null;
                DocumentReference df = fStore.collection("Users").document(user.getUid())
                        .collection("Orders").document();
                String orderId = df.getId();
                DocumentReference df2 = fStore.collection("OrderStatus").document();
                String customerId = user.getUid();

                /*
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }*/

                if(photo.getDrawable() != null){
                    imageUri = getImageUri(getApplicationContext(), ((BitmapDrawable)photo.getDrawable()).getBitmap());
                }


                if(imageUri != null) {
                    //save it to cloud storage then add the url of it to fireStore
                    StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                    fileRef.putFile(imageUri)
                            .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                                    .addOnSuccessListener(uri -> {

                                        String uploadUrl = uri.toString();
                                        Map<String,Object> reqInfo = new HashMap<>();
                                        reqInfo.put("reqId", Objects.requireNonNull(reqId.getText()).toString());
                                        reqInfo.put("customerName", Objects.requireNonNull(customerName.getText()).toString());
                                        reqInfo.put("customerNumber", Objects.requireNonNull(customerNumber.getText()).toString());
                                        reqInfo.put("tailorName",Objects.requireNonNull(tailorName.getText()).toString());
                                        reqInfo.put("distance",Objects.requireNonNull(distance.getText()).toString());
                                        reqInfo.put("gender",Objects.requireNonNull(gender.getText()).toString());
                                        reqInfo.put("item",Objects.requireNonNull(item.getText()).toString());

                                        if(!(note.getText().toString()).equals("null")){
                                            reqInfo.put("note", Objects.requireNonNull(note.getText()).toString());
                                        }
                                        reqInfo.put("imageUrl",uploadUrl);
                                        reqInfo.put("neck", Objects.requireNonNull(neck.getText()).toString());
                                        reqInfo.put("shoulder", Objects.requireNonNull(shoulder.getText()).toString());
                                        reqInfo.put("sleeve", Objects.requireNonNull(sleeve.getText()).toString());
                                        reqInfo.put("chest", Objects.requireNonNull(chest.getText()).toString());
                                        reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                        reqInfo.put("hip", Objects.requireNonNull(hip.getText()).toString());
                                        reqInfo.put("centerBack", Objects.requireNonNull(centerBack.getText()).toString());
                                        reqInfo.put("crotchLength", Objects.requireNonNull(crotchLength.getText()).toString());
                                        reqInfo.put("wrist", Objects.requireNonNull(wrist.getText()).toString());
                                        reqInfo.put("calf", Objects.requireNonNull(calf.getText()).toString());
                                        reqInfo.put("inseam", Objects.requireNonNull(inseam.getText()).toString());
                                        reqInfo.put("outSeam", Objects.requireNonNull(outSeam.getText()).toString());
                                        reqInfo.put("reqCreatedAt",Objects.requireNonNull(created.getText()).toString());
                                        reqInfo.put("reqAcceptedAt", Objects.requireNonNull(accepted.getText()).toString());
                                        reqInfo.put("orderPlacedAt", Timestamp.now());
                                        reqInfo.put("tailorId",Objects.requireNonNull(id.getText()).toString());
                                        reqInfo.put("price",Objects.requireNonNull(price.getText()).toString());
                                        reqInfo.put("orderId",orderId);
                                        reqInfo.put("status","pending");
                                        reqInfo.put("customerLat",Objects.requireNonNull(lat.getText()).toString());
                                        reqInfo.put("customerLng",Objects.requireNonNull(lng.getText()).toString());
                                        reqInfo.put("customerId",customerId);

                                        df.set(reqInfo);
                                        df2.set(reqInfo);
                                    })
                                    .addOnFailureListener(e -> {

                                    })).addOnFailureListener(e -> Toast.makeText(CustomerRequestDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                }

                if(imageUri == null){
                    Map<String,Object> reqInfo = new HashMap<>();
                    reqInfo.put("reqId", Objects.requireNonNull(reqId.getText()).toString());
                    reqInfo.put("customerName", Objects.requireNonNull(customerName.getText()).toString());
                    reqInfo.put("customerNumber", Objects.requireNonNull(customerNumber.getText()).toString());
                    reqInfo.put("tailorName",Objects.requireNonNull(tailorName.getText()).toString());
                    reqInfo.put("distance",Objects.requireNonNull(distance.getText()).toString());
                    reqInfo.put("gender",Objects.requireNonNull(gender.getText()).toString());
                    reqInfo.put("item",Objects.requireNonNull(item.getText()).toString());

                    if(!(note.getText().toString()).equals("null")){
                        reqInfo.put("note", Objects.requireNonNull(note.getText()).toString());
                    }

                    reqInfo.put("neck", Objects.requireNonNull(neck.getText()).toString());
                    reqInfo.put("shoulder", Objects.requireNonNull(shoulder.getText()).toString());
                    reqInfo.put("sleeve", Objects.requireNonNull(sleeve.getText()).toString());
                    reqInfo.put("chest", Objects.requireNonNull(chest.getText()).toString());
                    reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                    reqInfo.put("hip", Objects.requireNonNull(hip.getText()).toString());
                    reqInfo.put("centerBack", Objects.requireNonNull(centerBack.getText()).toString());
                    reqInfo.put("crotchLength", Objects.requireNonNull(crotchLength.getText()).toString());
                    reqInfo.put("wrist", Objects.requireNonNull(wrist.getText()).toString());
                    reqInfo.put("calf", Objects.requireNonNull(calf.getText()).toString());
                    reqInfo.put("inseam", Objects.requireNonNull(inseam.getText()).toString());
                    reqInfo.put("outSeam", Objects.requireNonNull(outSeam.getText()).toString());
                    reqInfo.put("reqCreatedAt",Objects.requireNonNull(created.getText()).toString());
                    reqInfo.put("reqAcceptedAt", Objects.requireNonNull(accepted.getText()).toString());
                    reqInfo.put("orderPlacedAt", Timestamp.now());
                    reqInfo.put("tailorId",Objects.requireNonNull(id.getText()).toString());
                    reqInfo.put("price",Objects.requireNonNull(price.getText()).toString());
                    reqInfo.put("orderId",orderId);
                    reqInfo.put("status","pending");
                    reqInfo.put("customerLat",Objects.requireNonNull(lat.getText()).toString());
                    reqInfo.put("customerLng",Objects.requireNonNull(lng.getText()).toString());
                    reqInfo.put("customerId",customerId);

                    df.set(reqInfo);
                    df2.set(reqInfo);
                }
            }
            else{
                Toast.makeText(CustomerRequestDetails.this, "Location is required", Toast.LENGTH_SHORT).show();
            }


        });
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