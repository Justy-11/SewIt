package com.example.sewit;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class AcceptedReqFragment extends Fragment implements CustomerAcceptedReqAdapter.onCardListener {

    private static final String TAG = "AcceptedReqFragment";
    RecyclerView recyclerView;
    ArrayList<CustomerAcceptedReqData> customerAcceptedReqDataArrayList;
    CustomerAcceptedReqAdapter customerAcceptedReqAdapter;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    ProgressDialog progressDialog;
    //CustomerAcceptedReqAdapter.MyViewHolder myViewHolder;  // 9/7/2022 - for REMOVED
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextInputEditText distanceMax,priceMax;
    Button filterBtn;
    String mDist,mPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        View v = inflater.inflate(R.layout.fragment_accepted_req, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        radioGroup = v.findViewById(R.id.radioGrp);
        distanceMax = v.findViewById(R.id.inputDistanceMax);
        priceMax = v.findViewById(R.id.inputPriceMax);
        filterBtn = v.findViewById(R.id.filterBTN);

        recyclerView = v.findViewById(R.id.recyclerViewAcceptedReq);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        customerAcceptedReqDataArrayList = new ArrayList<>();
        customerAcceptedReqAdapter = new CustomerAcceptedReqAdapter(getActivity(),customerAcceptedReqDataArrayList,this);

        recyclerView.setAdapter(customerAcceptedReqAdapter);

        // swipe delete
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //default
        EventChangeListenerFilterDist(String.valueOf(distanceMax.getText()));
        //01/10/2022
        if(progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        filterBtn.setOnClickListener(v1 -> {
            //radio button logic
            int radioId = radioGroup.getCheckedRadioButtonId();
            radioButton = v.findViewById(radioId);

            if(radioButton.getText().toString().equals("Distance")){
                EventChangeListenerFilterDist(String.valueOf(distanceMax.getText()));
            }
            if(radioButton.getText().toString().equals("Price")){
                EventChangeListenerFilterPri(String.valueOf(priceMax.getText()));
            }
        });
        
        return v;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void EventChangeListenerFilterDist(String distance_max) {

        if(distance_max.isEmpty()){
            mDist = "5";
        }
        if(!distance_max.isEmpty()){
            mDist = distance_max;
        }

        customerAcceptedReqDataArrayList.clear();
        customerAcceptedReqAdapter.notifyDataSetChanged();

        CollectionReference df = fStore.collection("Users");

        df.whereEqualTo("isTailor","1")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots){
                        String tailorId = snapshot.getId();
                        CollectionReference cr =  fStore.collection("Users")
                                .document(tailorId).collection("AcceptedCustomerRequests");

                        FirebaseUser user = fAuth.getCurrentUser();
                        assert user != null;
                        String id = user.getUid();

                        //ordered by distance
                        cr.whereEqualTo("customerId",id)
                                .whereLessThan("distance",mDist)
                                .addSnapshotListener((value, error) -> {
                                    //value -  QuerySnapshot
                                    if(error != null){
                                        if(progressDialog.isShowing()){
                                            progressDialog.dismiss();
                                        }
                                        return;
                                    }

                                    if(value != null){

                                        Log.d(TAG,String.valueOf(value.size()));

                                        for(DocumentChange dc : value.getDocumentChanges()){
                                            if(dc.getType() == DocumentChange.Type.ADDED){
                                                customerAcceptedReqDataArrayList.add(dc.getDocument().toObject(CustomerAcceptedReqData.class));
                                            }

                                            /*
                                            // 9/7/2022 -commented because also swipe delete use remove()
                                            if(dc.getType() == DocumentChange.Type.REMOVED){
                                                int position = myViewHolder.getAdapterPosition();
                                                customerAcceptedReqDataArrayList.remove(position);

                                            }

                                             */

                                            customerAcceptedReqAdapter.notifyDataSetChanged();
                                            if(progressDialog.isShowing()){
                                                progressDialog.dismiss();
                                            }
                                        }
                                    }
                                    if(progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }

                                });

                    }
                }).addOnFailureListener(e -> Toast.makeText(getActivity(),"Error", Toast.LENGTH_SHORT).show());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void EventChangeListenerFilterPri(String price_max) {

        if(price_max.isEmpty()){
            mPrice = "1500";
        }
        if(!price_max.isEmpty()){
            mPrice = price_max;
        }

        customerAcceptedReqDataArrayList.clear();
        customerAcceptedReqAdapter.notifyDataSetChanged();

        CollectionReference df = fStore.collection("Users");

        df.whereEqualTo("isTailor","1")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots){
                        String tailorId = snapshot.getId();
                        CollectionReference cr =  fStore.collection("Users")
                                .document(tailorId).collection("AcceptedCustomerRequests");

                        FirebaseUser user = fAuth.getCurrentUser();
                        assert user != null;
                        String id = user.getUid();

                        //ordered by distance
                        cr.whereEqualTo("customerId",id)
                                .whereLessThan("price",mPrice)
                                .addSnapshotListener((value, error) -> {
                                    //value -  QuerySnapshot
                                    if(error != null){
                                        if(progressDialog.isShowing()){
                                            progressDialog.dismiss();
                                        }
                                        return;
                                    }

                                    if(value != null){
                                        for(DocumentChange dc : value.getDocumentChanges()){
                                            if(dc.getType() == DocumentChange.Type.ADDED){
                                                customerAcceptedReqDataArrayList.add(dc.getDocument().toObject(CustomerAcceptedReqData.class));
                                            }

                                            /*
                                            // 9/7/2022 -commented because also swipe delete use remove()
                                            if(dc.getType() == DocumentChange.Type.REMOVED){
                                                int position = myViewHolder.getAdapterPosition();
                                                customerAcceptedReqDataArrayList.remove(position);

                                            }

                                             */

                                            customerAcceptedReqAdapter.notifyDataSetChanged();
                                            if(progressDialog.isShowing()){
                                                progressDialog.dismiss();
                                            }
                                        }
                                    }

                                    if(progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }

                                });

                    }
                }).addOnFailureListener(e -> Toast.makeText(getActivity(),"Error", Toast.LENGTH_SHORT).show());
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if(direction == ItemTouchHelper.LEFT){
                CustomerAcceptedReqAdapter.MyViewHolder myViewHolder = (CustomerAcceptedReqAdapter.MyViewHolder) viewHolder;
                customerAcceptedReqDataArrayList.remove(myViewHolder.getAdapterPosition());
                customerAcceptedReqAdapter.notifyItemRemoved(myViewHolder.getAdapterPosition());
                myViewHolder.deleteItem();
            }

        }


        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
                    .addActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        }

    };

    @Override
    public void onCardClick(int position) {
        Intent intent = new Intent(getActivity(),CustomerRequestDetails.class);
        //attached to a bundle
        intent.putExtra("Selected_card",customerAcceptedReqDataArrayList.get(position));
        startActivity(intent);
    }

    @Override
    public void handleDeleteItem(DocumentSnapshot snapshot) {
        snapshot.getReference().delete()
                .addOnSuccessListener(unused -> Toast.makeText(getActivity(),"Request deleted", Toast.LENGTH_SHORT).show());
    }
}