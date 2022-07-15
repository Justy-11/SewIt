package com.example.sewit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TailorRequestsDetails extends AppCompatActivity {

    TextView name,number,neck,shoulder,sleeve,chest,waist
            ,hip,centerBack,crotchLength,wrist,calf,inseam,outSeam,note,reqId,gender,item,created,distance,to,id;
    ImageView photo;
    Button acceptBtn;
    TextInputEditText price;
    Uri imageUri;
    StorageReference storageReference;
    FirebaseFirestore fStore;
    ProgressDialog progressDialog;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailor_requests_details);

        name = findViewById(R.id.tvGetName);
        number = findViewById(R.id.tvGetContact);
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
        price = findViewById(R.id.InputPrice);
        created = findViewById(R.id.tvGetCreated);
        acceptBtn = findViewById(R.id.btnAccept);
        distance = findViewById(R.id.tvGetDistance);
        to = findViewById(R.id.tvGetTo);
        id = findViewById(R.id.tvGetCustomerID);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        progressDialog = new ProgressDialog(TailorRequestsDetails.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        if(getIntent().hasExtra("Selected_card")){
            TailorReqData tailorReqData = getIntent().getParcelableExtra("Selected_card");

            String req_id = tailorReqData.getReqId();

            CollectionReference df = fStore.collection("Users");

            df.whereEqualTo("isCustomer","1")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots){
                            String customerId = snapshot.getId();
                            CollectionReference cr =  fStore.collection("Users")
                                    .document(customerId).collection("Requests");

                            cr.whereEqualTo("reqId",req_id)
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                        for(QueryDocumentSnapshot snapshot1: queryDocumentSnapshots1) {
                                            //TODO: retrieve Data to this activity and set texts
                                            reqId.setText(snapshot1.getString("reqId"));
                                            name.setText(snapshot1.getString("customerName"));
                                            shoulder.setText(String.valueOf(snapshot1.get("shoulder")));
                                            number.setText(snapshot1.getString("number"));
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
                                            to.setText(snapshot1.getString("tailorName"));
                                            if(snapshot1.contains("note")) {
                                                note.setText(snapshot1.getString("note"));
                                            }
                                            if(snapshot1.contains("imageUrl")) {
                                                String uri = snapshot1.getString("imageUrl");
                                                //TODO: Also can change dimensions of photos
                                                Glide.with(TailorRequestsDetails.this)
                                                        .load(uri)
                                                        .into(photo);
                                            }
                                            gender.setText(snapshot1.getString("gender"));
                                            item.setText(snapshot1.getString("item"));
                                            created.setText(String.valueOf(snapshot1.getDate("created")));
                                            distance.setText(String.valueOf(snapshot1.get("distance")));
                                            id.setText(customerId);

                                            if(progressDialog.isShowing()){
                                                progressDialog.dismiss();
                                            }

                                        }
                                    }).addOnFailureListener(e -> Toast.makeText(TailorRequestsDetails.this,"Could find the reqId", Toast.LENGTH_SHORT).show());

                        }
                    }).addOnFailureListener(e -> Toast.makeText(TailorRequestsDetails.this,"Could not find customers", Toast.LENGTH_SHORT).show());
        }

        acceptBtn.setOnClickListener(v -> {

            //check whether price field is set
            if(String.valueOf(price.getText()).isEmpty()){
                Toast.makeText(TailorRequestsDetails.this,"Price is not set", Toast.LENGTH_SHORT).show();
            }

            if(!String.valueOf(price.getText()).isEmpty()) {
                //if image is attached
                //if image is not attached
                Toast.makeText(TailorRequestsDetails.this,"Request accepted", Toast.LENGTH_SHORT).show();

                //send notification to customer - 13/7/2022  - not on api 24 ??
                fStore.collection("Tokens").document(name.getText().toString() +"sewIt")
                        .get()
                        .addOnSuccessListener(snapshot -> {
                            String token = snapshot.getString("token");
                            sendFCM.pushNotification(this,token, "Request accepted", to.getText().toString()
                                    + " has accepted the request for " + item.getText().toString()
                                    + " (Request Id : " + reqId.getText().toString() +")"
                            ,"AcceptedReqFragment");
                        });

                FirebaseUser user = fAuth.getCurrentUser();
                assert user != null;
                DocumentReference df = fStore.collection("Users").document(user.getUid())
                        .collection("AcceptedCustomerRequests").document();


                if(photo.getDrawable() != null){
                    imageUri = getImageUri(getApplicationContext(), ((BitmapDrawable)photo.getDrawable()).getBitmap());
                }

                fStore.collection("Users").document(user.getUid())
                        .get()
                        .addOnSuccessListener(snapshot -> {

                            int Rating = Integer.parseInt(String.valueOf(snapshot.get("rating")));

                            if(imageUri != null) {
                                //save it to cloud storage then add the url of it to fireStore
                                StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                                fileRef.putFile(imageUri)
                                        .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                                                .addOnSuccessListener(uri -> {

                                                    String uploadUrl = uri.toString();
                                                    Map<String,Object> reqInfo = new HashMap<>();
                                                    reqInfo.put("reqId",Objects.requireNonNull(reqId.getText()).toString());
                                                    reqInfo.put("customerName", Objects.requireNonNull(name.getText()).toString());
                                                    reqInfo.put("customerNumber", Objects.requireNonNull(number.getText()).toString());
                                                    reqInfo.put("tailorName",Objects.requireNonNull(to.getText()).toString());
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
                                                    reqInfo.put("reqAccepted",true);
                                                    reqInfo.put("createdAt",Objects.requireNonNull(created.getText()).toString());
                                                    reqInfo.put("acceptedAt", Timestamp.now());
                                                    // commented on 23/6/20222
                                                    reqInfo.put("customerId",Objects.requireNonNull(id.getText()).toString());
                                                    reqInfo.put("price",String.valueOf(price.getText()));
                                                    reqInfo.put("rating",Rating);

                                                    df.set(reqInfo);
                                                })
                                                .addOnFailureListener(e -> {

                                                })).addOnFailureListener(e -> Toast.makeText(TailorRequestsDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show());

                            }

                            if(imageUri == null){
                                Map<String,Object> reqInfo = new HashMap<>();
                                reqInfo.put("reqId",Objects.requireNonNull(reqId.getText()).toString());
                                reqInfo.put("customerName", Objects.requireNonNull(name.getText()).toString());
                                reqInfo.put("customerNumber", Objects.requireNonNull(number.getText()).toString());
                                reqInfo.put("tailorName",Objects.requireNonNull(to.getText()).toString());
                                reqInfo.put("distance",Objects.requireNonNull(distance.getText()).toString());
                                reqInfo.put("gender",Objects.requireNonNull(gender.getText()).toString());
                                reqInfo.put("item",Objects.requireNonNull(item.getText()).toString());

                                if((note.getText().toString()).equals("null")){
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
                                reqInfo.put("reqAccepted",true);
                                reqInfo.put("createdAt",Objects.requireNonNull(created.getText()).toString());
                                reqInfo.put("acceptedAt", Timestamp.now());
                                reqInfo.put("customerId",Objects.requireNonNull(id.getText()).toString());
                                reqInfo.put("price",String.valueOf(price.getText()));
                                reqInfo.put("rating",Rating);

                                df.set(reqInfo);
                            }

                        });

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