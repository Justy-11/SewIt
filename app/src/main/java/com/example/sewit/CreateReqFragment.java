package com.example.sewit;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sewit.BodyMeasurements.BlouseFragment;
import com.example.sewit.BodyMeasurements.PantsFragment;
import com.example.sewit.BodyMeasurements.SalwarFragment;
import com.example.sewit.BodyMeasurements.ShirtLSFragment;
import com.example.sewit.BodyMeasurements.ShirtSSFragment;
import com.example.sewit.BodyMeasurements.ShortsFragment;
import com.example.sewit.BodyMeasurements.TshirtLSFragment;
import com.example.sewit.BodyMeasurements.TshirtSSFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class CreateReqFragment extends Fragment{

    private static final String TAG = "CreateReqFragment";
    Spinner spinnerItems;
    ImageView addPhoto;
    CheckBox male,female;
    TextInputEditText name,number,lat,lng,note;

    //28/09/2022
    TextInputEditText neck,blouseLength,armhole,lowerBust,chest,shoulderLength,neckFront,neckBack,sleeveLength,sleeveWidth,
    hip,waist,crotchLength,inseam,outSeam,thighAround,bottom,kneeAround,calf,salwarLength,kameezLength,aboveAroundWaist,
    shirtLength,wrist,bicepAround,shortsLength,legOpening,tShirtLength;

    TextInputLayout neckL,blouseLengthL,armholeL,lowerBustL,chestL,shoulderLengthL,neckFrontL,neckBackL,sleeveLengthL,sleeveWidthL,
            hipL,waistL,crotchLengthL,inseamL,outSeamL,thighAroundL,bottomL,kneeAroundL,calfL,salwarLengthL,kameezLengthL,aboveAroundWaistL,
            shirtLengthL,wristL,bicepAroundL,shortsLengthL,legOpeningL,tShirtLengthL;

    double startLatitude,startLongitude,endLatitude, endLongitude;
    Button placeReq;
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
    int REQUEST_CODE = 1001;
    boolean isPermissionGranted = false;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Uri imageUri;
    StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_req, container, false);

        spinnerItems = v.findViewById(R.id.SPItems);
        addPhoto = v.findViewById(R.id.SamplePhoto);
        male = v.findViewById(R.id.checkBoxMale);
        female = v.findViewById(R.id.checkBoxFemale);
        name = v.findViewById(R.id.inputName);
        number = v.findViewById(R.id.inputNumber);
        lat = v.findViewById(R.id.inputLat);
        lng = v.findViewById(R.id.inputLng);
        note = v.findViewById(R.id.noteAdd);
        placeReq = v.findViewById(R.id.placeReqBtn);
        /* //commented on 23/09/2022
        neck = v.findViewById(R.id.neckAdd);
        shoulder = v.findViewById(R.id.shoulderAdd);
        sleeve = v.findViewById(R.id.sleeveAdd);
        chest = v.findViewById(R.id.chestAdd);
        waist = v.findViewById(R.id.waistAdd);
        hip = v.findViewById(R.id.hipAdd);
        centerBack = v.findViewById(R.id.centerBackAdd);
        crotchLength = v.findViewById(R.id.crotchLengthAdd);
        wrist = v.findViewById(R.id.wristAdd);
        calf = v.findViewById(R.id.calfAdd);
        inseam = v.findViewById(R.id.inseamAdd);
        outSeam = v.findViewById(R.id.outSeamAdd);
        */

        //28/09/2022
        neck = v.findViewById(R.id.Neck);
        blouseLength = v.findViewById(R.id.BlouseLength);
        armhole = v.findViewById(R.id.Armhole);
        lowerBust = v.findViewById(R.id.LowerBust);
        chest = v.findViewById(R.id.Bust);
        shoulderLength = v.findViewById(R.id.ShoulderLength);
        neckFront = v.findViewById(R.id.NeckFront);
        neckBack = v.findViewById(R.id.NeckBack);
        sleeveLength = v.findViewById(R.id.SleeveLength);
        sleeveWidth = v.findViewById(R.id.SleeveWidth);
        hip = v.findViewById(R.id.Hip);
        waist = v.findViewById(R.id.Waist);
        crotchLength = v.findViewById(R.id.CrotchLength);
        inseam = v.findViewById(R.id.Inseam);
        outSeam = v.findViewById(R.id.OutSeam);
        thighAround = v.findViewById(R.id.ThighAround);
        bottom = v.findViewById(R.id.Bottom);
        kneeAround = v.findViewById(R.id.KneeAround);
        calf = v.findViewById(R.id.Calf);
        salwarLength = v.findViewById(R.id.SalwarLength);
        kameezLength = v.findViewById(R.id.KameezLength);
        aboveAroundWaist = v.findViewById(R.id.AroundAboveWaist);
        shirtLength = v.findViewById(R.id.ShirtLength);
        wrist = v.findViewById(R.id.Wrist);
        bicepAround = v.findViewById(R.id.BicepAround);
        shortsLength = v.findViewById(R.id.ShortLength);
        legOpening = v.findViewById(R.id.LegOpening);
        tShirtLength = v.findViewById(R.id.TShirtLength);

        neckL = v.findViewById(R.id.NeckTIL);
        blouseLengthL = v.findViewById(R.id.BlouseLengthTIL);
        armholeL = v.findViewById(R.id.ArmholeTIL);
        lowerBustL = v.findViewById(R.id.LowerBustTIL);
        chestL = v.findViewById(R.id.BustTIL);
        shoulderLengthL = v.findViewById(R.id.ShoulderLengthTIL);
        neckFrontL = v.findViewById(R.id.NeckFrontTIL);
        neckBackL = v.findViewById(R.id.NeckBackTIL);
        sleeveLengthL = v.findViewById(R.id.SleeveLengthTIL);
        sleeveWidthL = v.findViewById(R.id.SleeveWidthTIL);
        hipL = v.findViewById(R.id.HipTIL);
        waistL = v.findViewById(R.id.WaistTIL);
        crotchLengthL = v.findViewById(R.id.CrotchLengthTIL);
        inseamL = v.findViewById(R.id.InseamTIL);
        outSeamL = v.findViewById(R.id.OutSeamTIL);
        thighAroundL = v.findViewById(R.id.ThighAroundTIL);
        bottomL = v.findViewById(R.id.BottomTIL);
        kneeAroundL = v.findViewById(R.id.KneeAroundTIL);
        calfL = v.findViewById(R.id.CalfTIL);
        salwarLengthL = v.findViewById(R.id.SalwarLengthTIL);
        kameezLengthL = v.findViewById(R.id.KameezLengthTIL);
        aboveAroundWaistL = v.findViewById(R.id.AroundAboveWaistTIL);
        shirtLengthL = v.findViewById(R.id.ShirtLengthTIL);
        wristL = v.findViewById(R.id.WristTIL);
        bicepAroundL = v.findViewById(R.id.BicepAroundTIL);
        shortsLengthL = v.findViewById(R.id.ShortLengthTIL);
        legOpeningL = v.findViewById(R.id.LegOpeningTIL);
        tShirtLengthL = v.findViewById(R.id.TShirtLengthTIL);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");


        //checkbox logic
        male.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                female.setChecked(false);
            }
        });

        female.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                male.setChecked(false);
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.items,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItems.setAdapter(adapter);


        FirebaseUser user = fAuth.getCurrentUser();
        assert user != null;
        DocumentReference df = fStore.collection("Users").document(user.getUid());
        DocumentReference df2 = fStore.collection("Users").document(user.getUid())
                .collection("Body measurements").document("measurements");

        df.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){

                        String Name = Objects.requireNonNull(documentSnapshot.get("UserName")).toString();
                        String Number = Objects.requireNonNull(documentSnapshot.get("PhoneNumber")).toString();

                        name.setText(Name);
                        number.setText(Number);

                        //if specific fields are there only - then update the field values
                        if(documentSnapshot.contains("latitude")){
                            String Latitude =  Objects.requireNonNull(documentSnapshot.get("latitude")).toString();
                            lat.setText(Latitude);
                        }

                        if(documentSnapshot.contains("longitude")){
                            String Longitude =  Objects.requireNonNull(documentSnapshot.get("longitude")).toString();
                            lng.setText(Longitude);
                        }

                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(),"Error while loading", Toast.LENGTH_SHORT).show());

        //edited on 28/09/2022
        df2.get()
                .addOnSuccessListener(documentSnapshot2 -> {
                    if(documentSnapshot2.exists()){

                        if(documentSnapshot2.contains("neck")){
                            String Neck = Objects.requireNonNull(documentSnapshot2.get("neck")).toString();
                            neck.setText(Neck);
                        }

                        if(documentSnapshot2.contains("shoulder length")){
                            String Shoulder = Objects.requireNonNull(documentSnapshot2.get("shoulder length")).toString();
                            shoulderLength.setText(Shoulder);
                        }

                        if(documentSnapshot2.contains("sleeve length")){
                            String Sleeve = Objects.requireNonNull(documentSnapshot2.get("sleeve length")).toString();
                            sleeveLength.setText(Sleeve);
                        }

                        if(documentSnapshot2.contains("chest")){
                            String Chest = Objects.requireNonNull(documentSnapshot2.get("chest")).toString();
                            chest.setText(Chest);
                        }

                        if(documentSnapshot2.contains("waist")){
                            String Waist = Objects.requireNonNull(documentSnapshot2.get("waist")).toString();
                            waist.setText(Waist);
                        }

                        if(documentSnapshot2.contains("hip")){
                            String Hip = Objects.requireNonNull(documentSnapshot2.get("hip")).toString();
                            hip.setText(Hip);
                        }

                        if(documentSnapshot2.contains("crotch length")){
                            String CrotchLength = Objects.requireNonNull(documentSnapshot2.get("crotch length")).toString();
                            crotchLength.setText(CrotchLength);
                        }

                        if(documentSnapshot2.contains("wrist")){
                            String Wrist = Objects.requireNonNull(documentSnapshot2.get("wrist")).toString();
                            wrist.setText(Wrist);
                        }

                        if(documentSnapshot2.contains("calf")){
                            String Calf = Objects.requireNonNull(documentSnapshot2.get("calf")).toString();
                            calf.setText(Calf);
                        }

                        if(documentSnapshot2.contains("inseam")){
                            String Inseam = Objects.requireNonNull(documentSnapshot2.get("inseam")).toString();
                            inseam.setText(Inseam);
                        }

                        if(documentSnapshot2.contains("outSeam")){
                            String OutSeam = Objects.requireNonNull(documentSnapshot2.get("outSeam")).toString();
                            outSeam.setText(OutSeam);
                        }

                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(),"Error while loading", Toast.LENGTH_SHORT).show());

        //spinner item selected listener - 28/09/2022

        spinnerItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
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
                        break;
                    case 1:
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
                        break;
                    case 2:
                    case 3:

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
                        break;
                    case 4:

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
                        break;
                    case 5:

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
                        break;
                    case 6:

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
                        break;
                    case 7:

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
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addPhoto.setOnClickListener(v1 -> checkPermission());


        // place request btn listener - save placed requests
        placeReq.setOnClickListener(v12 -> {

            checkValidity();

            if(valid){
                fStore.collection("Users")
                        .whereEqualTo("isTailor","1")
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {

                            Toast.makeText(getActivity(),"Request placed to nearby tailors", Toast.LENGTH_SHORT).show();

                            for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots){

                                //getting tailor locations to calculate distance
                                if(snapshot.contains("latitude")) {
                                    endLatitude = Objects.requireNonNull(snapshot.getDouble("latitude"));

                                }
                                if(snapshot.contains("longitude")) {
                                    endLongitude = Objects.requireNonNull(snapshot.getDouble("longitude"));
                                }

                                startLatitude = Double.parseDouble(String.valueOf(lat.getText()));
                                startLongitude = Double.parseDouble(String.valueOf(lng.getText()));
                                String tailorName = snapshot.getString("UserName");
                                String tailorDocId = snapshot.getId();


                                //check whether the tailor has updated the location, then send request
                                if(snapshot.contains("latitude") && snapshot.contains("longitude")){
                                    float[] results = new float[1];
                                    Location.distanceBetween(startLatitude,startLongitude,endLatitude,endLongitude,results);
                                    float distance = results[0]/1000;  // in km

                                    FirebaseUser us = fAuth.getCurrentUser();
                                    DocumentReference df3 = fStore.collection("Users").document(us.getUid())
                                            .collection("Requests").document();
                                    String reqId = df3.getId();

                                    // nearby tailors - less than 5 km
                                    if(distance < 5.0){
                                        //if photo is not added
                                        if(imageUri != null) {
                                            //save it to firebase cloud storage then add the url of it to fireStore
                                            StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                                            fileRef.putFile(imageUri)
                                                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                                                            .addOnSuccessListener(uri -> {

                                                                String uploadUrl = uri.toString();
                                                                Map<String,Object> reqInfo = new HashMap<>();
                                                                reqInfo.put("reqId",reqId);
                                                                reqInfo.put("customerName", Objects.requireNonNull(name.getText()).toString());
                                                                reqInfo.put("tailorName",tailorName);
                                                                reqInfo.put("tailorId",tailorDocId);
                                                                // added on 23/6/20222
                                                                reqInfo.put("customerId",us.getUid());
                                                                reqInfo.put("number", Objects.requireNonNull(number.getText()).toString());
                                                                reqInfo.put("distance",distance);
                                                                if(male.isChecked()){
                                                                    reqInfo.put("gender","male");
                                                                }
                                                                if(female.isChecked()){
                                                                    reqInfo.put("gender","female");
                                                                }
                                                                reqInfo.put("item",spinnerItems.getSelectedItem().toString());

                                                                if(!String.valueOf(note.getText()).isEmpty()){
                                                                    reqInfo.put("note", Objects.requireNonNull(note.getText()).toString());
                                                                }
                                                                reqInfo.put("imageUrl",uploadUrl);

                                                                /* // commented 23/09/2022
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
                                                                */
                                                                //Log.i(TAG, "TEst");


                                                                // 27/09/2022
                                                                if(spinnerItems.getSelectedItem().toString().equals("Shirt (Short sleeve)")){

                                                                    reqInfo.put("neck", Objects.requireNonNull(neck.getText()).toString());
                                                                    reqInfo.put("shoulder length", Objects.requireNonNull(shoulderLength.getText()).toString());
                                                                    reqInfo.put("sleeve length", Objects.requireNonNull(sleeveLength.getText()).toString());
                                                                    reqInfo.put("chest", Objects.requireNonNull(chest.getText()).toString());
                                                                    reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                                                    reqInfo.put("shirt length", Objects.requireNonNull(shirtLength.getText()).toString());
                                                                    reqInfo.put("armhole", Objects.requireNonNull(armhole.getText()).toString());
                                                                    reqInfo.put("sleeve width", Objects.requireNonNull(sleeveWidth.getText()).toString());

                                                                }
                                                                else if(spinnerItems.getSelectedItem().toString().equals("Shirt (Long sleeve)")){

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
                                                                else if(spinnerItems.getSelectedItem().toString().equals("T-shirt (Short sleeve)")){

                                                                    reqInfo.put("neck", Objects.requireNonNull(neck.getText()).toString());
                                                                    reqInfo.put("shoulder length", Objects.requireNonNull(shoulderLength.getText()).toString());
                                                                    reqInfo.put("sleeve length", Objects.requireNonNull(sleeveLength.getText()).toString());
                                                                    reqInfo.put("chest", Objects.requireNonNull(chest.getText()).toString());
                                                                    reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                                                    reqInfo.put("t-shirt length", Objects.requireNonNull(tShirtLength.getText()).toString());
                                                                    reqInfo.put("armhole", Objects.requireNonNull(armhole.getText()).toString());
                                                                    reqInfo.put("sleeve width", Objects.requireNonNull(sleeveWidth.getText()).toString());

                                                                }

                                                                else if(spinnerItems.getSelectedItem().toString().equals("T-shirt (Long sleeve)")){

                                                                    reqInfo.put("neck", Objects.requireNonNull(neck.getText()).toString());
                                                                    reqInfo.put("shoulder length", Objects.requireNonNull(shoulderLength.getText()).toString());
                                                                    reqInfo.put("sleeve length", Objects.requireNonNull(sleeveLength.getText()).toString());
                                                                    reqInfo.put("chest", Objects.requireNonNull(chest.getText()).toString());
                                                                    reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                                                    reqInfo.put("t-shirt length", Objects.requireNonNull(tShirtLength.getText()).toString());
                                                                    reqInfo.put("armhole", Objects.requireNonNull(armhole.getText()).toString());
                                                                    reqInfo.put("sleeve width", Objects.requireNonNull(sleeveWidth.getText()).toString());

                                                                }

                                                                else if(spinnerItems.getSelectedItem().toString().equals("Pants")){

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
                                                                else if(spinnerItems.getSelectedItem().toString().equals("Shorts")){

                                                                    reqInfo.put("hip", Objects.requireNonNull(hip.getText()).toString());
                                                                    reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                                                    reqInfo.put("crotch length", Objects.requireNonNull(crotchLength.getText()).toString());
                                                                    reqInfo.put("thigh around", Objects.requireNonNull(thighAround.getText()).toString());
                                                                    reqInfo.put("shorts length", Objects.requireNonNull(shortsLength.getText()).toString());
                                                                    reqInfo.put("leg opening", Objects.requireNonNull(legOpening.getText()).toString());

                                                                }
                                                                else if(spinnerItems.getSelectedItem().toString().equals("Salwar")){

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

                                                                reqInfo.put("reqAccepted",false);
                                                                reqInfo.put("created",Timestamp.now());

                                                                df3.set(reqInfo);



                                                            })
                                                            .addOnFailureListener(e -> {

                                                            })).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
                                        }

                                        //if photo is added
                                        if(imageUri == null){
                                            Map<String,Object> reqInfo = new HashMap<>();
                                            reqInfo.put("reqId",reqId);
                                            reqInfo.put("customerName", Objects.requireNonNull(name.getText()).toString());
                                            reqInfo.put("tailorName",tailorName);
                                            reqInfo.put("tailorId",tailorDocId);
                                            // added on 23/6/20222
                                            reqInfo.put("customerId",us.getUid());
                                            reqInfo.put("number", Objects.requireNonNull(number.getText()).toString());
                                            reqInfo.put("distance",distance);
                                            if(male.isChecked()){
                                                reqInfo.put("gender","male");
                                            }
                                            if(female.isChecked()){
                                                reqInfo.put("gender","female");
                                            }
                                            reqInfo.put("item",spinnerItems.getSelectedItem().toString());

                                            if(!String.valueOf(note.getText()).isEmpty()){
                                                reqInfo.put("note", Objects.requireNonNull(note.getText()).toString());
                                            }
                                            /* // commented 23/09/2022
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
                                            */

                                            // 27/09/2022
                                            if(spinnerItems.getSelectedItem().toString().equals("Shirt (Short sleeve)")){

                                                reqInfo.put("neck", Objects.requireNonNull(neck.getText()).toString());
                                                reqInfo.put("shoulder length", Objects.requireNonNull(shoulderLength.getText()).toString());
                                                reqInfo.put("sleeve length", Objects.requireNonNull(sleeveLength.getText()).toString());
                                                reqInfo.put("chest", Objects.requireNonNull(chest.getText()).toString());
                                                reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                                reqInfo.put("shirt length", Objects.requireNonNull(shirtLength.getText()).toString());
                                                reqInfo.put("armhole", Objects.requireNonNull(armhole.getText()).toString());
                                                reqInfo.put("sleeve width", Objects.requireNonNull(sleeveWidth.getText()).toString());

                                            }
                                            else if(spinnerItems.getSelectedItem().toString().equals("Shirt (Long sleeve)")){

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
                                            else if(spinnerItems.getSelectedItem().toString().equals("T-shirt (Short sleeve)")){

                                                reqInfo.put("neck", Objects.requireNonNull(neck.getText()).toString());
                                                reqInfo.put("shoulder length", Objects.requireNonNull(shoulderLength.getText()).toString());
                                                reqInfo.put("sleeve length", Objects.requireNonNull(sleeveLength.getText()).toString());
                                                reqInfo.put("chest", Objects.requireNonNull(chest.getText()).toString());
                                                reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                                reqInfo.put("t-shirt length", Objects.requireNonNull(tShirtLength.getText()).toString());
                                                reqInfo.put("armhole", Objects.requireNonNull(armhole.getText()).toString());
                                                reqInfo.put("sleeve width", Objects.requireNonNull(sleeveWidth.getText()).toString());

                                            }

                                            else if(spinnerItems.getSelectedItem().toString().equals("T-shirt (Long sleeve)")){

                                                reqInfo.put("neck", Objects.requireNonNull(neck.getText()).toString());
                                                reqInfo.put("shoulder length", Objects.requireNonNull(shoulderLength.getText()).toString());
                                                reqInfo.put("sleeve length", Objects.requireNonNull(sleeveLength.getText()).toString());
                                                reqInfo.put("chest", Objects.requireNonNull(chest.getText()).toString());
                                                reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                                reqInfo.put("t-shirt length", Objects.requireNonNull(tShirtLength.getText()).toString());
                                                reqInfo.put("armhole", Objects.requireNonNull(armhole.getText()).toString());
                                                reqInfo.put("sleeve width", Objects.requireNonNull(sleeveWidth.getText()).toString());

                                            }

                                            else if(spinnerItems.getSelectedItem().toString().equals("Pants")){

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
                                            else if(spinnerItems.getSelectedItem().toString().equals("Shorts")){

                                                reqInfo.put("hip", Objects.requireNonNull(hip.getText()).toString());
                                                reqInfo.put("waist", Objects.requireNonNull(waist.getText()).toString());
                                                reqInfo.put("crotch length", Objects.requireNonNull(crotchLength.getText()).toString());
                                                reqInfo.put("thigh around", Objects.requireNonNull(thighAround.getText()).toString());
                                                reqInfo.put("shorts length", Objects.requireNonNull(shortsLength.getText()).toString());
                                                reqInfo.put("leg opening", Objects.requireNonNull(legOpening.getText()).toString());

                                            }
                                            else if(spinnerItems.getSelectedItem().toString().equals("Salwar")){

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

                                            reqInfo.put("reqAccepted",false);
                                            reqInfo.put("created",Timestamp.now());

                                            df3.set(reqInfo);
                                        }
                                    }


                                }

                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(getActivity(),"Could not find tailors", Toast.LENGTH_SHORT).show());
            }
        });

        return v;
    }


    // check validity of fields while clicking on place request button
    public boolean checkValidity(){

        //commented on 23/09/2022
        if(String.valueOf(name.getText()).isEmpty() || String.valueOf(number.getText()).isEmpty()
                || String.valueOf(lat.getText()).isEmpty() || String.valueOf(lng.getText()).isEmpty()
                || !(male.isChecked() || female.isChecked()) || spinnerItems.getSelectedItem().toString().isEmpty()){
            Toast.makeText(getActivity(),"All fields are required except note and image", Toast.LENGTH_LONG).show();
            valid = false;
        }
        else if(spinnerItems.getSelectedItem().toString().equals("Blouse") && (String.valueOf(blouseLength.getText()).isEmpty() || String.valueOf(armhole.getText()).isEmpty() ||
                String.valueOf(lowerBust.getText()).isEmpty() || String.valueOf(chest.getText()).isEmpty() ||
                String.valueOf(shoulderLength.getText()).isEmpty() || String.valueOf(neckFront.getText()).isEmpty()||
                String.valueOf(neckBack.getText()).isEmpty() || String.valueOf(sleeveLength.getText()).isEmpty() ||
                String.valueOf(sleeveWidth.getText()).isEmpty())){
            Toast.makeText(getActivity(),"All measurements are required", Toast.LENGTH_LONG).show();
            valid = false;
        }
        else if(spinnerItems.getSelectedItem().toString().equals("Salwar") && (String.valueOf(hip.getText()).isEmpty() || String.valueOf(waist.getText()).isEmpty() ||
                String.valueOf(calf.getText()).isEmpty() || String.valueOf(chest.getText()).isEmpty() ||
                String.valueOf(sleeveLength.getText()).isEmpty() || String.valueOf(salwarLength.getText()).isEmpty()||
                String.valueOf(kameezLength.getText()).isEmpty() || String.valueOf(bottom.getText()).isEmpty() ||
                String.valueOf(neckFront.getText()).isEmpty() || String.valueOf(neckBack.getText()).isEmpty() ||
                String.valueOf(sleeveWidth.getText()).isEmpty() || String.valueOf(aboveAroundWaist.getText()).isEmpty() ||
                String.valueOf(kneeAround.getText()).isEmpty() || String.valueOf(thighAround.getText()).isEmpty())){
            Toast.makeText(getActivity(),"All measurements are required", Toast.LENGTH_LONG).show();
            valid = false;
        }
        else if(spinnerItems.getSelectedItem().toString().equals("Shorts") && (String.valueOf(hip.getText()).isEmpty() || String.valueOf(waist.getText()).isEmpty() ||
                String.valueOf(crotchLength.getText()).isEmpty() || String.valueOf(shortsLength.getText()).isEmpty() ||
                String.valueOf(thighAround.getText()).isEmpty() || String.valueOf(legOpening.getText()).isEmpty())){
            Toast.makeText(getActivity(),"All measurements are required", Toast.LENGTH_LONG).show();
            valid = false;
        }
        else if(spinnerItems.getSelectedItem().toString().equals("Pants") && (String.valueOf(hip.getText()).isEmpty() || String.valueOf(waist.getText()).isEmpty() ||
                String.valueOf(crotchLength.getText()).isEmpty() || String.valueOf(inseam.getText()).isEmpty() ||
                String.valueOf(outSeam.getText()).isEmpty() || String.valueOf(calf.getText()).isEmpty()||
                String.valueOf(thighAround.getText()).isEmpty() || String.valueOf(kneeAround.getText()).isEmpty() ||
                String.valueOf(bottom.getText()).isEmpty())){
            Toast.makeText(getActivity(),"All measurements are required", Toast.LENGTH_LONG).show();
            valid = false;
        }
        else if(spinnerItems.getSelectedItem().toString().equals("Shirt (Short sleeve)") && (String.valueOf(neck.getText()).isEmpty() || String.valueOf(waist.getText()).isEmpty() ||
                String.valueOf(shoulderLength.getText()).isEmpty() || String.valueOf(chest.getText()).isEmpty() ||
                String.valueOf(sleeveLength.getText()).isEmpty() || String.valueOf(shirtLength.getText()).isEmpty()||
                String.valueOf(armhole.getText()).isEmpty() || String.valueOf(sleeveWidth.getText()).isEmpty())){
            Toast.makeText(getActivity(),"All measurements are required", Toast.LENGTH_LONG).show();
            valid = false;
        }
        else if(spinnerItems.getSelectedItem().toString().equals("Shirt (Long sleeve)") && (String.valueOf(neck.getText()).isEmpty() || String.valueOf(waist.getText()).isEmpty() ||
                String.valueOf(shoulderLength.getText()).isEmpty() || String.valueOf(chest.getText()).isEmpty() ||
                String.valueOf(sleeveWidth.getText()).isEmpty() || String.valueOf(shirtLength.getText()).isEmpty()||
                String.valueOf(wrist.getText()).isEmpty() || String.valueOf(bicepAround.getText()).isEmpty() ||
                String.valueOf(sleeveLength.getText()).isEmpty())){
            Toast.makeText(getActivity(),"All measurements are required", Toast.LENGTH_LONG).show();
            valid = false;
        }
        else if(spinnerItems.getSelectedItem().toString().equals("T-shirt (Short sleeve)") && (String.valueOf(neck.getText()).isEmpty() || String.valueOf(waist.getText()).isEmpty() ||
                String.valueOf(shoulderLength.getText()).isEmpty() || String.valueOf(chest.getText()).isEmpty() ||
                String.valueOf(sleeveLength.getText()).isEmpty() || String.valueOf(tShirtLength.getText()).isEmpty()||
                String.valueOf(armhole.getText()).isEmpty() || String.valueOf(sleeveWidth.getText()).isEmpty())){
            Toast.makeText(getActivity(),"All measurements are required", Toast.LENGTH_LONG).show();
            valid = false;
        }
        else if(spinnerItems.getSelectedItem().toString().equals("T-shirt (Long sleeve)") && (String.valueOf(neck.getText()).isEmpty() || String.valueOf(waist.getText()).isEmpty() ||
                String.valueOf(shoulderLength.getText()).isEmpty() || String.valueOf(chest.getText()).isEmpty() ||
                String.valueOf(sleeveLength.getText()).isEmpty() || String.valueOf(tShirtLength.getText()).isEmpty()||
                String.valueOf(armhole.getText()).isEmpty() || String.valueOf(sleeveWidth.getText()).isEmpty())){
            Toast.makeText(getActivity(),"All measurements are required", Toast.LENGTH_LONG).show();
            valid = false;
        }
        else{
            valid = true;
        }
        return valid;
    }

    public void checkPermission(){
        if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            isPermissionGranted = true;
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Complete Action Using"),REQUEST_CODE);
        }else{
            ActivityCompat.requestPermissions(getActivity(),permissions,REQUEST_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            isPermissionGranted = true;
            Toast.makeText(getContext(),"Permission Granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(),"Permission Denied", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_CODE == 1001 && data != null){
            imageUri = data.getData();
            addPhoto.setImageURI(imageUri);

        }else{
            Toast.makeText(getContext(),"Nothing selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = requireContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //23/09/2022
    /*
    public void setFragment(Fragment fragment){
        assert getFragmentManager() != null;
        FragmentTransaction fragmentTransaction =  getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerBodyMeasurements, fragment);
        fragmentTransaction.commit();
    }*/

}