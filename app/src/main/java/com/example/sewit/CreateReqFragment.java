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
import androidx.fragment.app.FragmentTransaction;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class CreateReqFragment extends Fragment {

    //private static final String TAG = "CreateReqFragment";
    Spinner spinnerItems;
    ImageView addPhoto;
    CheckBox male,female;
    TextInputEditText name,number,lat,lng, neck,shoulder,sleeve,chest,waist
            ,hip,centerBack,crotchLength,wrist,calf,inseam,outSeam,note;
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
        note = v.findViewById(R.id.noteAdd);
        placeReq = v.findViewById(R.id.placeReqBtn);

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

        //spinner item selected listener - 23/09/2022
        spinnerItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        setFragment(new ShirtSSFragment());
                        break;
                    case 1:
                        setFragment(new ShirtLSFragment());
                        break;
                    case 2:
                        setFragment(new TshirtSSFragment());
                        break;
                    case 3:
                        setFragment(new TshirtLSFragment());
                        break;
                    case 4:
                        setFragment(new PantsFragment());
                        break;
                    case 5:
                        setFragment(new ShortsFragment());
                        break;
                    case 6:
                        setFragment(new SalwarFragment());
                        break;
                    case 7:
                        setFragment(new BlouseFragment());
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

        df2.get()
                .addOnSuccessListener(documentSnapshot2 -> {
                    if(documentSnapshot2.exists()){

                        if(documentSnapshot2.contains("neck")){
                            String Neck = Objects.requireNonNull(documentSnapshot2.get("neck")).toString();
                            neck.setText(Neck);
                        }

                        if(documentSnapshot2.contains("shoulder length")){
                            String Shoulder = Objects.requireNonNull(documentSnapshot2.get("shoulder length")).toString();
                            shoulder.setText(Shoulder);
                        }
                        if(documentSnapshot2.contains("sleeve length")){
                            String Sleeve = Objects.requireNonNull(documentSnapshot2.get("sleeve length")).toString();
                            sleeve.setText(Sleeve);
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

                        if(documentSnapshot2.contains("center back")){
                            String CenterBack = Objects.requireNonNull(documentSnapshot2.get("center back")).toString();
                            centerBack.setText(CenterBack);
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

        if(String.valueOf(name.getText()).isEmpty() || String.valueOf(number.getText()).isEmpty()
                || String.valueOf(lat.getText()).isEmpty() || String.valueOf(lng.getText()).isEmpty()
                || String.valueOf(neck.getText()).isEmpty() || String.valueOf(shoulder.getText()).isEmpty()
                || String.valueOf(sleeve.getText()).isEmpty() || String.valueOf(chest.getText()).isEmpty()
                || String.valueOf(waist.getText()).isEmpty() || String.valueOf(hip.getText()).isEmpty()
                || String.valueOf(centerBack.getText()).isEmpty() || String.valueOf(crotchLength.getText()).isEmpty()
                || String.valueOf(wrist.getText()).isEmpty() || String.valueOf(calf.getText()).isEmpty()
                || String.valueOf(inseam.getText()).isEmpty() || String.valueOf(outSeam.getText()).isEmpty()
                || !(male.isChecked() || female.isChecked()) || spinnerItems.getSelectedItem().toString().isEmpty()){
            Toast.makeText(getActivity(),"All fields are required except note and image", Toast.LENGTH_LONG).show();
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
    public void setFragment(Fragment fragment){
        assert getFragmentManager() != null;
        FragmentTransaction fragmentTransaction =  getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerBodyMeasurements, fragment);
        fragmentTransaction.commit();
    }
}