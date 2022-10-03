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
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sewit.Service.sendFCM;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

    TextView name,number,note,reqId,gender,item,created,distance,to,id;
    ImageView photo;
    Button acceptBtn;
    TextInputEditText price;
    Uri imageUri;
    StorageReference storageReference;
    FirebaseFirestore fStore;
    ProgressDialog progressDialog;
    FirebaseAuth fAuth;

    //28/09/2022
    TextInputEditText neck,blouseLength,armhole,lowerBust,chest,shoulderLength,neckFront,neckBack,sleeveLength,sleeveWidth,
            hip,waist,crotchLength,inseam,outSeam,thighAround,bottom,kneeAround,calf,salwarLength,kameezLength,aboveAroundWaist,
            shirtLength,wrist,bicepAround,shortsLength,legOpening,tShirtLength;

    TextInputLayout neckL,blouseLengthL,armholeL,lowerBustL,chestL,shoulderLengthL,neckFrontL,neckBackL,sleeveLengthL,sleeveWidthL,
            hipL,waistL,crotchLengthL,inseamL,outSeamL,thighAroundL,bottomL,kneeAroundL,calfL,salwarLengthL,kameezLengthL,aboveAroundWaistL,
            shirtLengthL,wristL,bicepAroundL,shortsLengthL,legOpeningL,tShirtLengthL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailor_requests_details);

        name = findViewById(R.id.tvGetName);
        number = findViewById(R.id.tvGetContact);
        //28/09/2022
        /*neck = findViewById(R.id.tvGetNeckSize);
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
        outSeam = findViewById(R.id.tvGetOutSeam);*/
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

        //28/09/2022
        neck = findViewById(R.id.Neck);
        blouseLength = findViewById(R.id.BlouseLength);
        armhole = findViewById(R.id.Armhole);
        lowerBust = findViewById(R.id.LowerBust);
        chest = findViewById(R.id.Bust);
        shoulderLength = findViewById(R.id.ShoulderLength);
        neckFront = findViewById(R.id.NeckFront);
        neckBack = findViewById(R.id.NeckBack);
        sleeveLength = findViewById(R.id.SleeveLength);
        sleeveWidth = findViewById(R.id.SleeveWidth);
        hip = findViewById(R.id.Hip);
        waist = findViewById(R.id.Waist);
        crotchLength = findViewById(R.id.CrotchLength);
        inseam = findViewById(R.id.Inseam);
        outSeam = findViewById(R.id.OutSeam);
        thighAround = findViewById(R.id.ThighAround);
        bottom = findViewById(R.id.Bottom);
        kneeAround = findViewById(R.id.KneeAround);
        calf = findViewById(R.id.Calf);
        salwarLength = findViewById(R.id.SalwarLength);
        kameezLength = findViewById(R.id.KameezLength);
        aboveAroundWaist = findViewById(R.id.AroundAboveWaist);
        shirtLength = findViewById(R.id.ShirtLength);
        wrist = findViewById(R.id.Wrist);
        bicepAround = findViewById(R.id.BicepAround);
        shortsLength = findViewById(R.id.ShortLength);
        legOpening = findViewById(R.id.LegOpening);
        tShirtLength = findViewById(R.id.TShirtLength);

        neckL = findViewById(R.id.NeckTIL);
        blouseLengthL = findViewById(R.id.BlouseLengthTIL);
        armholeL = findViewById(R.id.ArmholeTIL);
        lowerBustL = findViewById(R.id.LowerBustTIL);
        chestL = findViewById(R.id.BustTIL);
        shoulderLengthL = findViewById(R.id.ShoulderLengthTIL);
        neckFrontL = findViewById(R.id.NeckFrontTIL);
        neckBackL = findViewById(R.id.NeckBackTIL);
        sleeveLengthL = findViewById(R.id.SleeveLengthTIL);
        sleeveWidthL = findViewById(R.id.SleeveWidthTIL);
        hipL = findViewById(R.id.HipTIL);
        waistL = findViewById(R.id.WaistTIL);
        crotchLengthL = findViewById(R.id.CrotchLengthTIL);
        inseamL = findViewById(R.id.InseamTIL);
        outSeamL = findViewById(R.id.OutSeamTIL);
        thighAroundL = findViewById(R.id.ThighAroundTIL);
        bottomL = findViewById(R.id.BottomTIL);
        kneeAroundL = findViewById(R.id.KneeAroundTIL);
        calfL = findViewById(R.id.CalfTIL);
        salwarLengthL = findViewById(R.id.SalwarLengthTIL);
        kameezLengthL = findViewById(R.id.KameezLengthTIL);
        aboveAroundWaistL = findViewById(R.id.AroundAboveWaistTIL);
        shirtLengthL = findViewById(R.id.ShirtLengthTIL);
        wristL = findViewById(R.id.WristTIL);
        bicepAroundL = findViewById(R.id.BicepAroundTIL);
        shortsLengthL = findViewById(R.id.ShortLengthTIL);
        legOpeningL = findViewById(R.id.LegOpeningTIL);
        tShirtLengthL = findViewById(R.id.TShirtLengthTIL);

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
                                            number.setText(snapshot1.getString("number"));
                                            //28/09/2022
                                            /*shoulder.setText(String.valueOf(snapshot1.get("shoulder")));
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
                                            outSeam.setText(String.valueOf(snapshot1.get("outSeam")));*/

                                            if(Objects.equals(snapshot1.getString("item"), "Shirt (Short sleeve)")){
                                                neckL.setVisibility(View.VISIBLE);
                                                waistL.setVisibility(View.VISIBLE);
                                                shoulderLengthL.setVisibility(View.VISIBLE);
                                                chestL.setVisibility(View.VISIBLE);
                                                sleeveLengthL.setVisibility(View.VISIBLE);
                                                shirtLengthL.setVisibility(View.VISIBLE);
                                                armholeL.setVisibility(View.VISIBLE);
                                                sleeveWidthL.setVisibility(View.VISIBLE);
                                                blouseLengthL.setVisibility(View.GONE);
                                                lowerBustL.setVisibility(View.GONE);
                                                neckFrontL.setVisibility(View.GONE);
                                                neckBackL.setVisibility(View.GONE);
                                                hipL.setVisibility(View.GONE);
                                                crotchLengthL.setVisibility(View.GONE);
                                                inseamL.setVisibility(View.GONE);
                                                outSeamL.setVisibility(View.GONE);
                                                thighAroundL.setVisibility(View.GONE);
                                                bottomL.setVisibility(View.GONE);
                                                kneeAroundL.setVisibility(View.GONE);
                                                calfL.setVisibility(View.GONE);
                                                salwarLengthL.setVisibility(View.GONE);
                                                kameezLengthL.setVisibility(View.GONE);
                                                aboveAroundWaistL.setVisibility(View.GONE);
                                                wristL.setVisibility(View.GONE);
                                                bicepAroundL.setVisibility(View.GONE);
                                                shortsLengthL.setVisibility(View.GONE);
                                                legOpeningL.setVisibility(View.GONE);
                                                tShirtLengthL.setVisibility(View.GONE);

                                                neck.setText(String.valueOf(snapshot1.get("neck")));
                                                waist.setText(String.valueOf(snapshot1.get("waist")));
                                                shoulderLength.setText(String.valueOf(snapshot1.get("shoulder length")));
                                                chest.setText(String.valueOf(snapshot1.get("chest")));
                                                sleeveLength.setText(String.valueOf(snapshot1.get("sleeve length")));
                                                shirtLength.setText(String.valueOf(snapshot1.get("shirt length")));
                                                armhole.setText(String.valueOf(snapshot1.get("armhole")));
                                                sleeveWidth.setText(String.valueOf(snapshot1.get("sleeve width")));
                                            }
                                            else if(Objects.equals(snapshot1.getString("item"), "Shirt (Long sleeve)")){
                                                neckL.setVisibility(View.VISIBLE);
                                                waistL.setVisibility(View.VISIBLE);
                                                shoulderLengthL.setVisibility(View.VISIBLE);
                                                chestL.setVisibility(View.VISIBLE);
                                                sleeveLengthL.setVisibility(View.VISIBLE);
                                                sleeveWidthL.setVisibility(View.VISIBLE);
                                                shirtLengthL.setVisibility(View.VISIBLE);
                                                armholeL.setVisibility(View.GONE);
                                                blouseLengthL.setVisibility(View.GONE);
                                                lowerBustL.setVisibility(View.GONE);
                                                neckFrontL.setVisibility(View.GONE);
                                                neckBackL.setVisibility(View.GONE);
                                                hipL.setVisibility(View.GONE);
                                                crotchLengthL.setVisibility(View.GONE);
                                                inseamL.setVisibility(View.GONE);
                                                outSeamL.setVisibility(View.GONE);
                                                thighAroundL.setVisibility(View.GONE);
                                                bottomL.setVisibility(View.GONE);
                                                kneeAroundL.setVisibility(View.GONE);
                                                calfL.setVisibility(View.GONE);
                                                salwarLengthL.setVisibility(View.GONE);
                                                kameezLengthL.setVisibility(View.GONE);
                                                aboveAroundWaistL.setVisibility(View.GONE);
                                                wristL.setVisibility(View.VISIBLE);
                                                bicepAroundL.setVisibility(View.VISIBLE);
                                                shortsLengthL.setVisibility(View.GONE);
                                                legOpeningL.setVisibility(View.GONE);
                                                tShirtLengthL.setVisibility(View.GONE);

                                                neck.setText(String.valueOf(snapshot1.get("neck")));
                                                waist.setText(String.valueOf(snapshot1.get("waist")));
                                                shoulderLength.setText(String.valueOf(snapshot1.get("shoulder length")));
                                                chest.setText(String.valueOf(snapshot1.get("chest")));
                                                sleeveLength.setText(String.valueOf(snapshot1.get("sleeve length")));
                                                shirtLength.setText(String.valueOf(snapshot1.get("shirt length")));
                                                sleeveWidth.setText(String.valueOf(snapshot1.get("sleeve width")));
                                                wrist.setText(String.valueOf(snapshot1.get("wrist")));
                                                bicepAround.setText(String.valueOf(snapshot1.get("bicep around")));
                                            }
                                            else if(Objects.equals(snapshot1.getString("item"), "T-shirt (Long sleeve)")){
                                                neckL.setVisibility(View.VISIBLE);
                                                waistL.setVisibility(View.VISIBLE);
                                                shoulderLengthL.setVisibility(View.VISIBLE);
                                                chestL.setVisibility(View.VISIBLE);
                                                sleeveLengthL.setVisibility(View.VISIBLE);
                                                sleeveWidthL.setVisibility(View.VISIBLE);
                                                shirtLengthL.setVisibility(View.GONE);
                                                armholeL.setVisibility(View.VISIBLE);
                                                blouseLengthL.setVisibility(View.GONE);
                                                lowerBustL.setVisibility(View.GONE);
                                                neckFrontL.setVisibility(View.GONE);
                                                neckBackL.setVisibility(View.GONE);
                                                hipL.setVisibility(View.GONE);
                                                crotchLengthL.setVisibility(View.GONE);
                                                inseamL.setVisibility(View.GONE);
                                                outSeamL.setVisibility(View.GONE);
                                                thighAroundL.setVisibility(View.GONE);
                                                bottomL.setVisibility(View.GONE);
                                                kneeAroundL.setVisibility(View.GONE);
                                                calfL.setVisibility(View.GONE);
                                                salwarLengthL.setVisibility(View.GONE);
                                                kameezLengthL.setVisibility(View.GONE);
                                                aboveAroundWaistL.setVisibility(View.GONE);
                                                wristL.setVisibility(View.GONE);
                                                bicepAroundL.setVisibility(View.GONE);
                                                shortsLengthL.setVisibility(View.GONE);
                                                legOpeningL.setVisibility(View.GONE);
                                                tShirtLengthL.setVisibility(View.VISIBLE);

                                                neck.setText(String.valueOf(snapshot1.get("neck")));
                                                waist.setText(String.valueOf(snapshot1.get("waist")));
                                                shoulderLength.setText(String.valueOf(snapshot1.get("shoulder length")));
                                                chest.setText(String.valueOf(snapshot1.get("chest")));
                                                sleeveLength.setText(String.valueOf(snapshot1.get("sleeve length")));
                                                tShirtLength.setText(String.valueOf(snapshot1.get("t-shirt length")));
                                                sleeveWidth.setText(String.valueOf(snapshot1.get("sleeve width")));
                                                armhole.setText(String.valueOf(snapshot1.get("armhole")));
                                            }
                                            else if(Objects.equals(snapshot1.getString("item"), "T-shirt (Short sleeve)")){
                                                neckL.setVisibility(View.VISIBLE);
                                                waistL.setVisibility(View.VISIBLE);
                                                shoulderLengthL.setVisibility(View.VISIBLE);
                                                chestL.setVisibility(View.VISIBLE);
                                                sleeveLengthL.setVisibility(View.VISIBLE);
                                                sleeveWidthL.setVisibility(View.VISIBLE);
                                                shirtLengthL.setVisibility(View.GONE);
                                                armholeL.setVisibility(View.VISIBLE);
                                                blouseLengthL.setVisibility(View.GONE);
                                                lowerBustL.setVisibility(View.GONE);
                                                neckFrontL.setVisibility(View.GONE);
                                                neckBackL.setVisibility(View.GONE);
                                                hipL.setVisibility(View.GONE);
                                                crotchLengthL.setVisibility(View.GONE);
                                                inseamL.setVisibility(View.GONE);
                                                outSeamL.setVisibility(View.GONE);
                                                thighAroundL.setVisibility(View.GONE);
                                                bottomL.setVisibility(View.GONE);
                                                kneeAroundL.setVisibility(View.GONE);
                                                calfL.setVisibility(View.GONE);
                                                salwarLengthL.setVisibility(View.GONE);
                                                kameezLengthL.setVisibility(View.GONE);
                                                aboveAroundWaistL.setVisibility(View.GONE);
                                                wristL.setVisibility(View.GONE);
                                                bicepAroundL.setVisibility(View.GONE);
                                                shortsLengthL.setVisibility(View.GONE);
                                                legOpeningL.setVisibility(View.GONE);
                                                tShirtLengthL.setVisibility(View.VISIBLE);

                                                neck.setText(String.valueOf(snapshot1.get("neck")));
                                                waist.setText(String.valueOf(snapshot1.get("waist")));
                                                shoulderLength.setText(String.valueOf(snapshot1.get("shoulder length")));
                                                chest.setText(String.valueOf(snapshot1.get("chest")));
                                                sleeveLength.setText(String.valueOf(snapshot1.get("sleeve length")));
                                                tShirtLength.setText(String.valueOf(snapshot1.get("t-shirt length")));
                                                sleeveWidth.setText(String.valueOf(snapshot1.get("sleeve width")));
                                                armhole.setText(String.valueOf(snapshot1.get("armhole")));
                                            }
                                            else if(Objects.equals(snapshot1.getString("item"), "Pants")){
                                                neckL.setVisibility(View.GONE);
                                                waistL.setVisibility(View.VISIBLE);
                                                shoulderLengthL.setVisibility(View.GONE);
                                                chestL.setVisibility(View.GONE);
                                                sleeveLengthL.setVisibility(View.GONE);
                                                sleeveWidthL.setVisibility(View.GONE);
                                                shirtLengthL.setVisibility(View.GONE);
                                                armholeL.setVisibility(View.GONE);
                                                blouseLengthL.setVisibility(View.GONE);
                                                lowerBustL.setVisibility(View.GONE);
                                                neckFrontL.setVisibility(View.GONE);
                                                neckBackL.setVisibility(View.GONE);
                                                hipL.setVisibility(View.VISIBLE);
                                                crotchLengthL.setVisibility(View.VISIBLE);
                                                inseamL.setVisibility(View.VISIBLE);
                                                outSeamL.setVisibility(View.VISIBLE);
                                                thighAroundL.setVisibility(View.VISIBLE);
                                                bottomL.setVisibility(View.VISIBLE);
                                                kneeAroundL.setVisibility(View.VISIBLE);
                                                calfL.setVisibility(View.VISIBLE);
                                                salwarLengthL.setVisibility(View.GONE);
                                                kameezLengthL.setVisibility(View.GONE);
                                                aboveAroundWaistL.setVisibility(View.GONE);
                                                wristL.setVisibility(View.GONE);
                                                bicepAroundL.setVisibility(View.GONE);
                                                shortsLengthL.setVisibility(View.GONE);
                                                legOpeningL.setVisibility(View.GONE);
                                                tShirtLengthL.setVisibility(View.GONE);

                                                hip.setText(String.valueOf(snapshot1.get("hip")));
                                                waist.setText(String.valueOf(snapshot1.get("waist")));
                                                crotchLength.setText(String.valueOf(snapshot1.get("crotch length")));
                                                inseam.setText(String.valueOf(snapshot1.get("inseam")));
                                                outSeam.setText(String.valueOf(snapshot1.get("outseam")));
                                                thighAround.setText(String.valueOf(snapshot1.get("thigh around")));
                                                bottom.setText(String.valueOf(snapshot1.get("bottom")));
                                                kneeAround.setText(String.valueOf(snapshot1.get("knee around")));
                                                calf.setText(String.valueOf(snapshot1.get("calf")));
                                            }
                                            else if(Objects.equals(snapshot1.getString("item"), "Shorts")){
                                                neckL.setVisibility(View.GONE);
                                                waistL.setVisibility(View.VISIBLE);
                                                shoulderLengthL.setVisibility(View.GONE);
                                                chestL.setVisibility(View.GONE);
                                                sleeveLengthL.setVisibility(View.GONE);
                                                sleeveWidthL.setVisibility(View.GONE);
                                                shirtLengthL.setVisibility(View.GONE);
                                                armholeL.setVisibility(View.GONE);
                                                blouseLengthL.setVisibility(View.GONE);
                                                lowerBustL.setVisibility(View.GONE);
                                                neckFrontL.setVisibility(View.GONE);
                                                neckBackL.setVisibility(View.GONE);
                                                hipL.setVisibility(View.VISIBLE);
                                                crotchLengthL.setVisibility(View.VISIBLE);
                                                inseamL.setVisibility(View.GONE);
                                                outSeamL.setVisibility(View.GONE);
                                                thighAroundL.setVisibility(View.VISIBLE);
                                                bottomL.setVisibility(View.GONE);
                                                kneeAroundL.setVisibility(View.GONE);
                                                calfL.setVisibility(View.GONE);
                                                salwarLengthL.setVisibility(View.GONE);
                                                kameezLengthL.setVisibility(View.GONE);
                                                aboveAroundWaistL.setVisibility(View.GONE);
                                                wristL.setVisibility(View.GONE);
                                                bicepAroundL.setVisibility(View.GONE);
                                                shortsLengthL.setVisibility(View.VISIBLE);
                                                legOpeningL.setVisibility(View.VISIBLE);
                                                tShirtLengthL.setVisibility(View.GONE);

                                                hip.setText(String.valueOf(snapshot1.get("hip")));
                                                waist.setText(String.valueOf(snapshot1.get("waist")));
                                                crotchLength.setText(String.valueOf(snapshot1.get("crotch length")));
                                                thighAround.setText(String.valueOf(snapshot1.get("thigh around")));
                                                shortsLength.setText(String.valueOf(snapshot1.get("shorts length")));
                                                legOpening.setText(String.valueOf(snapshot1.get("leg opening")));
                                            }
                                            else if(Objects.equals(snapshot1.getString("item"), "Salwar")){
                                                neckL.setVisibility(View.GONE);
                                                waistL.setVisibility(View.VISIBLE);
                                                shoulderLengthL.setVisibility(View.GONE);
                                                chestL.setVisibility(View.VISIBLE);
                                                sleeveLengthL.setVisibility(View.VISIBLE);
                                                shirtLengthL.setVisibility(View.GONE);
                                                armholeL.setVisibility(View.GONE);
                                                sleeveWidthL.setVisibility(View.VISIBLE);
                                                blouseLengthL.setVisibility(View.GONE);
                                                lowerBustL.setVisibility(View.GONE);
                                                neckFrontL.setVisibility(View.VISIBLE);
                                                neckBackL.setVisibility(View.VISIBLE);
                                                hipL.setVisibility(View.VISIBLE);
                                                crotchLengthL.setVisibility(View.GONE);
                                                inseamL.setVisibility(View.GONE);
                                                outSeamL.setVisibility(View.GONE);
                                                thighAroundL.setVisibility(View.VISIBLE);
                                                bottomL.setVisibility(View.VISIBLE);
                                                kneeAroundL.setVisibility(View.VISIBLE);
                                                calfL.setVisibility(View.VISIBLE);
                                                salwarLengthL.setVisibility(View.VISIBLE);
                                                kameezLengthL.setVisibility(View.VISIBLE);
                                                aboveAroundWaistL.setVisibility(View.VISIBLE);
                                                wristL.setVisibility(View.GONE);
                                                bicepAroundL.setVisibility(View.GONE);
                                                shortsLengthL.setVisibility(View.GONE);
                                                legOpeningL.setVisibility(View.GONE);
                                                tShirtLengthL.setVisibility(View.GONE);

                                                hip.setText(String.valueOf(snapshot1.get("hip")));
                                                waist.setText(String.valueOf(snapshot1.get("waist")));
                                                calf.setText(String.valueOf(snapshot1.get("calf")));
                                                chest.setText(String.valueOf(snapshot1.get("bust")));
                                                sleeveLength.setText(String.valueOf(snapshot1.get("sleeve length")));
                                                salwarLength.setText(String.valueOf(snapshot1.get("salwar length")));
                                                kameezLength.setText(String.valueOf(snapshot1.get("kameez length")));
                                                bottom.setText(String.valueOf(snapshot1.get("bottom")));
                                                neckFront.setText(String.valueOf(snapshot1.get("neck front")));
                                                neckBack.setText(String.valueOf(snapshot1.get("neck back")));
                                                sleeveWidth.setText(String.valueOf(snapshot1.get("sleeve width")));
                                                thighAround.setText(String.valueOf(snapshot1.get("thigh around")));
                                                kneeAround.setText(String.valueOf(snapshot1.get("knee around")));
                                                aboveAroundWaist.setText(String.valueOf(snapshot1.get("above around waist")));
                                            }
                                            else{
                                                neckL.setVisibility(View.GONE);
                                                waistL.setVisibility(View.GONE);
                                                shoulderLengthL.setVisibility(View.VISIBLE);
                                                chestL.setVisibility(View.VISIBLE);
                                                sleeveLengthL.setVisibility(View.VISIBLE);
                                                shirtLengthL.setVisibility(View.GONE);
                                                armholeL.setVisibility(View.VISIBLE);
                                                sleeveWidthL.setVisibility(View.VISIBLE);
                                                blouseLengthL.setVisibility(View.VISIBLE);
                                                lowerBustL.setVisibility(View.VISIBLE);
                                                neckFrontL.setVisibility(View.VISIBLE);
                                                neckBackL.setVisibility(View.VISIBLE);
                                                hipL.setVisibility(View.GONE);
                                                crotchLengthL.setVisibility(View.GONE);
                                                inseamL.setVisibility(View.GONE);
                                                outSeamL.setVisibility(View.GONE);
                                                thighAroundL.setVisibility(View.GONE);
                                                bottomL.setVisibility(View.GONE);
                                                kneeAroundL.setVisibility(View.GONE);
                                                calfL.setVisibility(View.GONE);
                                                salwarLengthL.setVisibility(View.GONE);
                                                kameezLengthL.setVisibility(View.GONE);
                                                aboveAroundWaistL.setVisibility(View.GONE);
                                                wristL.setVisibility(View.GONE);
                                                bicepAroundL.setVisibility(View.GONE);
                                                shortsLengthL.setVisibility(View.GONE);
                                                legOpeningL.setVisibility(View.GONE);
                                                tShirtLengthL.setVisibility(View.GONE);

                                                blouseLength.setText(String.valueOf(snapshot1.get("blouse length")));
                                                armhole.setText(String.valueOf(snapshot1.get("armhole")));
                                                lowerBust.setText(String.valueOf(snapshot1.get("lower bust")));
                                                chest.setText(String.valueOf(snapshot1.get("bust")));
                                                shoulderLength.setText(String.valueOf(snapshot1.get("shoulder length")));
                                                sleeveLength.setText(String.valueOf(snapshot1.get("sleeve length")));
                                                sleeveWidth.setText(String.valueOf(snapshot1.get("sleeve width")));
                                                neckFront.setText(String.valueOf(snapshot1.get("neck front")));
                                                neckBack.setText(String.valueOf(snapshot1.get("neck back")));
                                            }

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

                /*//send notification to customer - 13/7/2022  - not on api 24 ??
                fStore.collection("Tokens").document(name.getText().toString() +"sewIt")
                        .get()
                        .addOnSuccessListener(snapshot -> {
                            String token = snapshot.getString("token");
                            sendFCM.pushNotification(this,token, "Request accepted", to.getText().toString()
                                    + " has accepted the request for " + item.getText().toString()
                                    + " (Request Id : " + reqId.getText().toString() +")"
                            ,"AcceptedReqFragment");
                        });*/

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
                                                    //28/09/2022
                                                    /*reqInfo.put("neck", Objects.requireNonNull(neck.getText()).toString());
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
                                                    reqInfo.put("outSeam", Objects.requireNonNull(outSeam.getText()).toString());*/

                                                    // 28/09/2022
                                                    if(item.getText().toString().equals("Shirt (Short sleeve)")){

                                                        reqInfo.put("neck", Objects.requireNonNull(neck.getText()).toString());
                                                        reqInfo.put("shoulder length", Objects.requireNonNull(shoulderLength.getText()).toString());
                                                        reqInfo.put("sleeve length", Objects.requireNonNull(sleeveLength.getText()).toString());
                                                        reqInfo.put("chest", Objects.requireNonNull(chest.getText()).toString());
                                                        reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                                        reqInfo.put("shirt length", Objects.requireNonNull(shirtLength.getText()).toString());
                                                        reqInfo.put("armhole", Objects.requireNonNull(armhole.getText()).toString());
                                                        reqInfo.put("sleeve width", Objects.requireNonNull(sleeveWidth.getText()).toString());

                                                    }
                                                    else if(item.getText().toString().equals("Shirt (Long sleeve)")){

                                                        reqInfo.put("neck", Objects.requireNonNull(neck.getText()).toString());
                                                        reqInfo.put("shoulder length", Objects.requireNonNull(shoulderLength.getText()).toString());
                                                        reqInfo.put("sleeve length", Objects.requireNonNull(sleeveLength.getText()).toString());
                                                        reqInfo.put("chest", Objects.requireNonNull(chest.getText()).toString());
                                                        reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                                        reqInfo.put("shirt length", Objects.requireNonNull(shirtLength.getText()).toString());
                                                        reqInfo.put("wrist", Objects.requireNonNull(wrist.getText()).toString());
                                                        reqInfo.put("sleeve width", Objects.requireNonNull(sleeveWidth.getText()).toString());
                                                        reqInfo.put("bicep around", Objects.requireNonNull(bicepAround.getText()).toString());

                                                    }
                                                    else if(item.getText().toString().equals("T-shirt (Short sleeve)")){

                                                        reqInfo.put("neck", Objects.requireNonNull(neck.getText()).toString());
                                                        reqInfo.put("shoulder length", Objects.requireNonNull(shoulderLength.getText()).toString());
                                                        reqInfo.put("sleeve length", Objects.requireNonNull(sleeveLength.getText()).toString());
                                                        reqInfo.put("chest", Objects.requireNonNull(chest.getText()).toString());
                                                        reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                                        reqInfo.put("t-shirt length", Objects.requireNonNull(tShirtLength.getText()).toString());
                                                        reqInfo.put("armhole", Objects.requireNonNull(armhole.getText()).toString());
                                                        reqInfo.put("sleeve width", Objects.requireNonNull(sleeveWidth.getText()).toString());

                                                    }

                                                    else if(item.getText().toString().equals("T-shirt (Long sleeve)")){

                                                        reqInfo.put("neck", Objects.requireNonNull(neck.getText()).toString());
                                                        reqInfo.put("shoulder length", Objects.requireNonNull(shoulderLength.getText()).toString());
                                                        reqInfo.put("sleeve length", Objects.requireNonNull(sleeveLength.getText()).toString());
                                                        reqInfo.put("chest", Objects.requireNonNull(chest.getText()).toString());
                                                        reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                                        reqInfo.put("t-shirt length", Objects.requireNonNull(tShirtLength.getText()).toString());
                                                        reqInfo.put("armhole", Objects.requireNonNull(armhole.getText()).toString());
                                                        reqInfo.put("sleeve width", Objects.requireNonNull(sleeveWidth.getText()).toString());

                                                    }

                                                    else if(item.getText().toString().equals("Pants")){

                                                        reqInfo.put("hip", Objects.requireNonNull(hip.getText()).toString());
                                                        reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                                        reqInfo.put("crotch length", Objects.requireNonNull(crotchLength.getText()).toString());
                                                        reqInfo.put("inseam", Objects.requireNonNull(inseam.getText()).toString());
                                                        reqInfo.put("outseam", Objects.requireNonNull(outSeam.getText()).toString());
                                                        reqInfo.put("thigh around", Objects.requireNonNull(thighAround.getText()).toString());
                                                        reqInfo.put("bottom", Objects.requireNonNull(bottom.getText()).toString());
                                                        reqInfo.put("knee around", Objects.requireNonNull(kneeAround.getText()).toString());
                                                        reqInfo.put("calf", Objects.requireNonNull(calf.getText()).toString());


                                                    }
                                                    else if(item.getText().toString().equals("Shorts")){

                                                        reqInfo.put("hip", Objects.requireNonNull(hip.getText()).toString());
                                                        reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                                        reqInfo.put("crotch length", Objects.requireNonNull(crotchLength.getText()).toString());
                                                        reqInfo.put("thigh around", Objects.requireNonNull(thighAround.getText()).toString());
                                                        reqInfo.put("shorts length", Objects.requireNonNull(shortsLength.getText()).toString());
                                                        reqInfo.put("leg opening", Objects.requireNonNull(legOpening.getText()).toString());

                                                    }
                                                    else if(item.getText().toString().equals("Salwar")){

                                                        reqInfo.put("hip", Objects.requireNonNull(hip.getText()).toString());
                                                        reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                                        reqInfo.put("calf", Objects.requireNonNull(calf.getText()).toString());
                                                        reqInfo.put("bust", Objects.requireNonNull(chest.getText()).toString());
                                                        reqInfo.put("sleeve length", Objects.requireNonNull(sleeveLength.getText()).toString());
                                                        reqInfo.put("salwar length", Objects.requireNonNull(salwarLength.getText()).toString());
                                                        reqInfo.put("kameez length", Objects.requireNonNull(kameezLength.getText()).toString());
                                                        reqInfo.put("bottom", Objects.requireNonNull(bottom.getText()).toString());
                                                        reqInfo.put("neck front", Objects.requireNonNull(neckFront.getText()).toString());
                                                        reqInfo.put("neck back", Objects.requireNonNull(neckBack.getText()).toString());
                                                        reqInfo.put("sleeve width", Objects.requireNonNull(sleeveWidth.getText()).toString());
                                                        reqInfo.put("thigh around", Objects.requireNonNull(thighAround.getText()).toString());
                                                        reqInfo.put("knee around", Objects.requireNonNull(kneeAround.getText()).toString());
                                                        reqInfo.put("above around waist", Objects.requireNonNull(aboveAroundWaist.getText()).toString());
                                                    }
                                                    else {

                                                        reqInfo.put("blouse length", Objects.requireNonNull(blouseLength.getText()).toString());
                                                        reqInfo.put("armhole", Objects.requireNonNull(armhole.getText()).toString());
                                                        reqInfo.put("lower bust", Objects.requireNonNull(lowerBust.getText()).toString());
                                                        reqInfo.put("bust", Objects.requireNonNull(chest.getText()).toString());
                                                        reqInfo.put("shoulder length", Objects.requireNonNull(shoulderLength.getText()).toString());
                                                        reqInfo.put("neck front", Objects.requireNonNull(neckFront.getText()).toString());
                                                        reqInfo.put("neck back", Objects.requireNonNull(neckBack.getText()).toString());
                                                        reqInfo.put("sleeve length", Objects.requireNonNull(sleeveLength.getText()).toString());
                                                        reqInfo.put("sleeve width", Objects.requireNonNull(sleeveWidth.getText()).toString());

                                                    }

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
                                //28/09/2022
                                /*reqInfo.put("neck", Objects.requireNonNull(neck.getText()).toString());
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
                                reqInfo.put("outSeam", Objects.requireNonNull(outSeam.getText()).toString());*/

                                // 28/09/2022
                                if(item.getText().toString().equals("Shirt (Short sleeve)")){

                                    reqInfo.put("neck", Objects.requireNonNull(neck.getText()).toString());
                                    reqInfo.put("shoulder length", Objects.requireNonNull(shoulderLength.getText()).toString());
                                    reqInfo.put("sleeve length", Objects.requireNonNull(sleeveLength.getText()).toString());
                                    reqInfo.put("chest", Objects.requireNonNull(chest.getText()).toString());
                                    reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                    reqInfo.put("shirt length", Objects.requireNonNull(shirtLength.getText()).toString());
                                    reqInfo.put("armhole", Objects.requireNonNull(armhole.getText()).toString());
                                    reqInfo.put("sleeve width", Objects.requireNonNull(sleeveWidth.getText()).toString());

                                }
                                else if(item.getText().toString().equals("Shirt (Long sleeve)")){

                                    reqInfo.put("neck", Objects.requireNonNull(neck.getText()).toString());
                                    reqInfo.put("shoulder length", Objects.requireNonNull(shoulderLength.getText()).toString());
                                    reqInfo.put("sleeve length", Objects.requireNonNull(sleeveLength.getText()).toString());
                                    reqInfo.put("chest", Objects.requireNonNull(chest.getText()).toString());
                                    reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                    reqInfo.put("shirt length", Objects.requireNonNull(shirtLength.getText()).toString());
                                    reqInfo.put("wrist", Objects.requireNonNull(wrist.getText()).toString());
                                    reqInfo.put("sleeve width", Objects.requireNonNull(sleeveWidth.getText()).toString());
                                    reqInfo.put("bicep around", Objects.requireNonNull(bicepAround.getText()).toString());

                                }
                                else if(item.getText().toString().equals("T-shirt (Short sleeve)")){

                                    reqInfo.put("neck", Objects.requireNonNull(neck.getText()).toString());
                                    reqInfo.put("shoulder length", Objects.requireNonNull(shoulderLength.getText()).toString());
                                    reqInfo.put("sleeve length", Objects.requireNonNull(sleeveLength.getText()).toString());
                                    reqInfo.put("chest", Objects.requireNonNull(chest.getText()).toString());
                                    reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                    reqInfo.put("t-shirt length", Objects.requireNonNull(tShirtLength.getText()).toString());
                                    reqInfo.put("armhole", Objects.requireNonNull(armhole.getText()).toString());
                                    reqInfo.put("sleeve width", Objects.requireNonNull(sleeveWidth.getText()).toString());

                                }

                                else if(item.getText().toString().equals("T-shirt (Long sleeve)")){

                                    reqInfo.put("neck", Objects.requireNonNull(neck.getText()).toString());
                                    reqInfo.put("shoulder length", Objects.requireNonNull(shoulderLength.getText()).toString());
                                    reqInfo.put("sleeve length", Objects.requireNonNull(sleeveLength.getText()).toString());
                                    reqInfo.put("chest", Objects.requireNonNull(chest.getText()).toString());
                                    reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                    reqInfo.put("t-shirt length", Objects.requireNonNull(tShirtLength.getText()).toString());
                                    reqInfo.put("armhole", Objects.requireNonNull(armhole.getText()).toString());
                                    reqInfo.put("sleeve width", Objects.requireNonNull(sleeveWidth.getText()).toString());

                                }

                                else if(item.getText().toString().equals("Pants")){

                                    reqInfo.put("hip", Objects.requireNonNull(hip.getText()).toString());
                                    reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                    reqInfo.put("crotch length", Objects.requireNonNull(crotchLength.getText()).toString());
                                    reqInfo.put("inseam", Objects.requireNonNull(inseam.getText()).toString());
                                    reqInfo.put("outseam", Objects.requireNonNull(outSeam.getText()).toString());
                                    reqInfo.put("thigh around", Objects.requireNonNull(thighAround.getText()).toString());
                                    reqInfo.put("bottom", Objects.requireNonNull(bottom.getText()).toString());
                                    reqInfo.put("knee around", Objects.requireNonNull(kneeAround.getText()).toString());
                                    reqInfo.put("calf", Objects.requireNonNull(calf.getText()).toString());


                                }
                                else if(item.getText().toString().equals("Shorts")){

                                    reqInfo.put("hip", Objects.requireNonNull(hip.getText()).toString());
                                    reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                    reqInfo.put("crotch length", Objects.requireNonNull(crotchLength.getText()).toString());
                                    reqInfo.put("thigh around", Objects.requireNonNull(thighAround.getText()).toString());
                                    reqInfo.put("shorts length", Objects.requireNonNull(shortsLength.getText()).toString());
                                    reqInfo.put("leg opening", Objects.requireNonNull(legOpening.getText()).toString());

                                }
                                else if(item.getText().toString().equals("Salwar")){

                                    reqInfo.put("hip", Objects.requireNonNull(hip.getText()).toString());
                                    reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                    reqInfo.put("calf", Objects.requireNonNull(calf.getText()).toString());
                                    reqInfo.put("bust", Objects.requireNonNull(chest.getText()).toString());
                                    reqInfo.put("sleeve length", Objects.requireNonNull(sleeveLength.getText()).toString());
                                    reqInfo.put("salwar length", Objects.requireNonNull(salwarLength.getText()).toString());
                                    reqInfo.put("kameez length", Objects.requireNonNull(kameezLength.getText()).toString());
                                    reqInfo.put("bottom", Objects.requireNonNull(bottom.getText()).toString());
                                    reqInfo.put("neck front", Objects.requireNonNull(neckFront.getText()).toString());
                                    reqInfo.put("neck back", Objects.requireNonNull(neckBack.getText()).toString());
                                    reqInfo.put("sleeve width", Objects.requireNonNull(sleeveWidth.getText()).toString());
                                    reqInfo.put("thigh around", Objects.requireNonNull(thighAround.getText()).toString());
                                    reqInfo.put("knee around", Objects.requireNonNull(kneeAround.getText()).toString());
                                    reqInfo.put("above around waist", Objects.requireNonNull(aboveAroundWaist.getText()).toString());
                                }
                                else {

                                    reqInfo.put("blouse length", Objects.requireNonNull(blouseLength.getText()).toString());
                                    reqInfo.put("armhole", Objects.requireNonNull(armhole.getText()).toString());
                                    reqInfo.put("lower bust", Objects.requireNonNull(lowerBust.getText()).toString());
                                    reqInfo.put("bust", Objects.requireNonNull(chest.getText()).toString());
                                    reqInfo.put("shoulder length", Objects.requireNonNull(shoulderLength.getText()).toString());
                                    reqInfo.put("neck front", Objects.requireNonNull(neckFront.getText()).toString());
                                    reqInfo.put("neck back", Objects.requireNonNull(neckBack.getText()).toString());
                                    reqInfo.put("sleeve length", Objects.requireNonNull(sleeveLength.getText()).toString());
                                    reqInfo.put("sleeve width", Objects.requireNonNull(sleeveWidth.getText()).toString());

                                }

                                reqInfo.put("reqAccepted",true);
                                reqInfo.put("createdAt",Objects.requireNonNull(created.getText()).toString());
                                reqInfo.put("acceptedAt", Timestamp.now());
                                reqInfo.put("customerId",Objects.requireNonNull(id.getText()).toString());
                                reqInfo.put("price",String.valueOf(price.getText()));
                                reqInfo.put("rating",Rating);

                                df.set(reqInfo);
                            }

                        });

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