package com.example.sewit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sewit.Service.sendFCM;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

    TextView customerName,tailorName
            ,note,orderId,item,orderPlaced,distance,price,status,orderFinished,
            rating,feedback;
    ImageView photo;
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
        setContentView(R.layout.activity_order_status_details);

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
                            //29/09/2022
                            /*shoulder.setText(String.valueOf(snapshot.get("shoulder")));
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
                            outSeam.setText(String.valueOf(snapshot.get("outSeam")));*/

                            if(Objects.equals(snapshot.getString("item"), "Shirt (Short sleeve)")){
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

                                neck.setText(String.valueOf(snapshot.get("neck")));
                                waist.setText(String.valueOf(snapshot.get("waist")));
                                shoulderLength.setText(String.valueOf(snapshot.get("shoulder length")));
                                chest.setText(String.valueOf(snapshot.get("chest")));
                                sleeveLength.setText(String.valueOf(snapshot.get("sleeve length")));
                                shirtLength.setText(String.valueOf(snapshot.get("shirt length")));
                                armhole.setText(String.valueOf(snapshot.get("armhole")));
                                sleeveWidth.setText(String.valueOf(snapshot.get("sleeve width")));
                            }
                            else if(Objects.equals(snapshot.getString("item"), "Shirt (Long sleeve)")){
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

                                neck.setText(String.valueOf(snapshot.get("neck")));
                                waist.setText(String.valueOf(snapshot.get("waist")));
                                shoulderLength.setText(String.valueOf(snapshot.get("shoulder length")));
                                chest.setText(String.valueOf(snapshot.get("chest")));
                                sleeveLength.setText(String.valueOf(snapshot.get("sleeve length")));
                                shirtLength.setText(String.valueOf(snapshot.get("shirt length")));
                                sleeveWidth.setText(String.valueOf(snapshot.get("sleeve width")));
                                wrist.setText(String.valueOf(snapshot.get("wrist")));
                                bicepAround.setText(String.valueOf(snapshot.get("bicep around")));
                            }
                            else if(Objects.equals(snapshot.getString("item"), "T-shirt (Long sleeve)")){
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

                                neck.setText(String.valueOf(snapshot.get("neck")));
                                waist.setText(String.valueOf(snapshot.get("waist")));
                                shoulderLength.setText(String.valueOf(snapshot.get("shoulder length")));
                                chest.setText(String.valueOf(snapshot.get("chest")));
                                sleeveLength.setText(String.valueOf(snapshot.get("sleeve length")));
                                tShirtLength.setText(String.valueOf(snapshot.get("t-shirt length")));
                                sleeveWidth.setText(String.valueOf(snapshot.get("sleeve width")));
                                armhole.setText(String.valueOf(snapshot.get("armhole")));
                            }
                            else if(Objects.equals(snapshot.getString("item"), "T-shirt (Short sleeve)")){
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

                                neck.setText(String.valueOf(snapshot.get("neck")));
                                waist.setText(String.valueOf(snapshot.get("waist")));
                                shoulderLength.setText(String.valueOf(snapshot.get("shoulder length")));
                                chest.setText(String.valueOf(snapshot.get("chest")));
                                sleeveLength.setText(String.valueOf(snapshot.get("sleeve length")));
                                tShirtLength.setText(String.valueOf(snapshot.get("t-shirt length")));
                                sleeveWidth.setText(String.valueOf(snapshot.get("sleeve width")));
                                armhole.setText(String.valueOf(snapshot.get("armhole")));
                            }
                            else if(Objects.equals(snapshot.getString("item"), "Pants")){
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

                                hip.setText(String.valueOf(snapshot.get("hip")));
                                waist.setText(String.valueOf(snapshot.get("waist")));
                                crotchLength.setText(String.valueOf(snapshot.get("crotch length")));
                                inseam.setText(String.valueOf(snapshot.get("inseam")));
                                outSeam.setText(String.valueOf(snapshot.get("outseam")));
                                thighAround.setText(String.valueOf(snapshot.get("thigh around")));
                                bottom.setText(String.valueOf(snapshot.get("bottom")));
                                kneeAround.setText(String.valueOf(snapshot.get("knee around")));
                                calf.setText(String.valueOf(snapshot.get("calf")));
                            }
                            else if(Objects.equals(snapshot.getString("item"), "Shorts")){
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

                                hip.setText(String.valueOf(snapshot.get("hip")));
                                waist.setText(String.valueOf(snapshot.get("waist")));
                                crotchLength.setText(String.valueOf(snapshot.get("crotch length")));
                                thighAround.setText(String.valueOf(snapshot.get("thigh around")));
                                shortsLength.setText(String.valueOf(snapshot.get("shorts length")));
                                legOpening.setText(String.valueOf(snapshot.get("leg opening")));
                            }
                            else if(Objects.equals(snapshot.getString("item"), "Salwar")){
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

                                hip.setText(String.valueOf(snapshot.get("hip")));
                                waist.setText(String.valueOf(snapshot.get("waist")));
                                calf.setText(String.valueOf(snapshot.get("calf")));
                                chest.setText(String.valueOf(snapshot.get("bust")));
                                sleeveLength.setText(String.valueOf(snapshot.get("sleeve length")));
                                salwarLength.setText(String.valueOf(snapshot.get("salwar length")));
                                kameezLength.setText(String.valueOf(snapshot.get("kameez length")));
                                bottom.setText(String.valueOf(snapshot.get("bottom")));
                                neckFront.setText(String.valueOf(snapshot.get("neck front")));
                                neckBack.setText(String.valueOf(snapshot.get("neck back")));
                                sleeveWidth.setText(String.valueOf(snapshot.get("sleeve width")));
                                thighAround.setText(String.valueOf(snapshot.get("thigh around")));
                                kneeAround.setText(String.valueOf(snapshot.get("knee around")));
                                aboveAroundWaist.setText(String.valueOf(snapshot.get("above around waist")));
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

                                blouseLength.setText(String.valueOf(snapshot.get("blouse length")));
                                armhole.setText(String.valueOf(snapshot.get("armhole")));
                                lowerBust.setText(String.valueOf(snapshot.get("lower bust")));
                                chest.setText(String.valueOf(snapshot.get("bust")));
                                shoulderLength.setText(String.valueOf(snapshot.get("shoulder length")));
                                sleeveLength.setText(String.valueOf(snapshot.get("sleeve length")));
                                sleeveWidth.setText(String.valueOf(snapshot.get("sleeve width")));
                                neckFront.setText(String.valueOf(snapshot.get("neck front")));
                                neckBack.setText(String.valueOf(snapshot.get("neck back")));
                            }

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