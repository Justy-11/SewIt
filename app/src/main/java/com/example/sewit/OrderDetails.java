package com.example.sewit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class OrderDetails extends AppCompatActivity {

    TextView customerNumber,customerName,tailorName
            ,note,orderId,gender,item,orderPlaced,distance,price;
    ImageView photo;
    Uri imageUri;
    StorageReference storageReference;
    FirebaseFirestore fStore;
    ProgressDialog progressDialog;
    FirebaseAuth fAuth;

    //29/09/2022
    TextInputEditText neck,blouseLength,armhole,lowerBust,chest,shoulderLength,neckFront,neckBack,sleeveLength,sleeveWidth,
            hip,waist,crotchLength,inseam,outSeam,thighAround,bottom,kneeAround,calf,salwarLength,kameezLength,aboveAroundWaist,
            shirtLength,wrist,bicepAround,shortsLength,legOpening,tShirtLength;

    TextInputLayout neckL,blouseLengthL,armholeL,lowerBustL,chestL,shoulderLengthL,neckFrontL,neckBackL,sleeveLengthL,sleeveWidthL,
            hipL,waistL,crotchLengthL,inseamL,outSeamL,thighAroundL,bottomL,kneeAroundL,calfL,salwarLengthL,kameezLengthL,aboveAroundWaistL,
            shirtLengthL,wristL,bicepAroundL,shortsLengthL,legOpeningL,tShirtLengthL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        customerNumber = findViewById(R.id.tvGetContact);

        //29/09/2022
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
        orderId = findViewById(R.id.tvGetOrderId);
        gender = findViewById(R.id.tvGetGender);
        item = findViewById(R.id.tvGetRequestedItem);
        photo = findViewById(R.id.tvGetPhoto);
        price = findViewById(R.id.tvGetPrice);
        orderPlaced = findViewById(R.id.tvGetOrderPlaced);
        distance = findViewById(R.id.tvGetDistance);
        customerName = findViewById(R.id.tvGetName);
        tailorName = findViewById(R.id.tvGetTo);

        //29/09/2022
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
                                            customerNumber.setText(snapshot1.getString("customerNumber"));
                                            //29/09/2022
                                            /*neck.setText(String.valueOf(snapshot1.get("neck")));
                                            shoulder.setText(String.valueOf(snapshot1.get("shoulder")));
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