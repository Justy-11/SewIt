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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import java.util.ArrayList;

public class RequestsFragment extends Fragment implements TailorReqAdapter.onCardListener{

    RecyclerView recyclerView;
    ArrayList<TailorReqData> tailorReqDataArrayList;
    TailorReqAdapter tailorReqAdapter;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    ProgressDialog progressDialog;
    //TailorReqAdapter.MyViewHolder myViewHolder; // 9/7/2022 - for REMOVED
    TextInputEditText distanceMax;
    Button filterBtn;
    float max;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_requests, container, false);

        //progress bar during fetching
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        distanceMax = v.findViewById(R.id.inputDistanceMax);
        filterBtn = v.findViewById(R.id.filterBTN);

        recyclerView = v.findViewById(R.id.recyclerViewTailorReq);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        tailorReqDataArrayList = new ArrayList<>();

        tailorReqAdapter = new TailorReqAdapter(getActivity(),tailorReqDataArrayList,this);
        recyclerView.setAdapter(tailorReqAdapter);

        // swipe delete
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        EventChangeListenerFilter(String.valueOf(distanceMax.getText()));

        filterBtn.setOnClickListener(v1 -> EventChangeListenerFilter(String.valueOf(distanceMax.getText())));

        return v;
    }


    @SuppressLint("NotifyDataSetChanged")
    private void EventChangeListenerFilter(String distance_max) {

        if(distance_max.isEmpty()){
            max = 5;
        }
        if(!distance_max.isEmpty()){
            max = Float.parseFloat(distance_max);
        }

        tailorReqDataArrayList.clear();
        tailorReqAdapter.notifyDataSetChanged();

        CollectionReference df = fStore.collection("Users");

        df.whereEqualTo("isCustomer","1")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots){
                        String customerId = snapshot.getId();
                        CollectionReference cr =  fStore.collection("Users")
                                .document(customerId).collection("Requests");

                        FirebaseUser user = fAuth.getCurrentUser();
                        assert user != null;
                        String id = user.getUid();

                        //ordered by distance
                        //TODO .orderBy("distance", Query.Direction.ASCENDING) is not working
                        cr.whereEqualTo("tailorId",id)
                                .whereLessThan("distance",max)
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
                                                tailorReqDataArrayList.add(dc.getDocument().toObject(TailorReqData.class));
                                            }

                                            /*
                                            // 9/7/2022 -commented because also swipe delete use remove()
                                            if(dc.getType() == DocumentChange.Type.REMOVED){

                                                int position = myViewHolder.getAdapterPosition();
                                                tailorReqDataArrayList.remove(position);

                                            }
                                            */

                                            tailorReqAdapter.notifyDataSetChanged();
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
                }).addOnFailureListener(e -> Toast.makeText(getActivity(),"Could not find customers", Toast.LENGTH_SHORT).show());

    }
    // for swipe delete
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if(direction == ItemTouchHelper.LEFT){
                TailorReqAdapter.MyViewHolder myViewHolder = (TailorReqAdapter.MyViewHolder) viewHolder;
                tailorReqDataArrayList.remove(myViewHolder.getAdapterPosition());
                tailorReqAdapter.notifyItemRemoved(myViewHolder.getAdapterPosition());
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
        Intent intent = new Intent(getActivity(),TailorRequestsDetails.class);
        //attached to a bundle
        intent.putExtra("Selected_card",tailorReqDataArrayList.get(position));
        startActivity(intent);
    }

    @Override
    public void handleDeleteItem(DocumentSnapshot snapshot) {
        // delete document
        snapshot.getReference().delete()
                .addOnSuccessListener(unused -> Toast.makeText(getActivity(),"Request deleted", Toast.LENGTH_SHORT).show());
    }
}