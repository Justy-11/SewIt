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
import android.widget.Toast;

import com.example.sewit.Service.sendFCM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class PendingOrdersFragment extends Fragment implements FinishedOrderAdapter.onCardListener{

    RecyclerView recyclerView;
    ArrayList<FinishedOrderData> finishedOrderDataArrayList;
    FinishedOrderAdapter finishedOrderAdapter;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    ProgressDialog progressDialog;
    FinishedOrderAdapter.MyViewHolder myViewHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pending_orders, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        recyclerView = v.findViewById(R.id.recyclerViewFinishedOrders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        finishedOrderDataArrayList = new ArrayList<>();
        finishedOrderAdapter = new FinishedOrderAdapter(getActivity(),finishedOrderDataArrayList,this);

        recyclerView.setAdapter(finishedOrderAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        EventChangeListener();
        //01/10/2022
        if(progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        return v;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void EventChangeListener() {

        finishedOrderDataArrayList.clear();
        finishedOrderAdapter.notifyDataSetChanged();

        FirebaseUser user = fAuth.getCurrentUser();
        CollectionReference cr = fStore.collection("OrderStatus");

        assert user != null;
        cr.whereEqualTo("tailorUnique",user.getUid()+"finished")
                .addSnapshotListener((value, error) -> {
                    if(error != null){
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        return;
                    }

                    if(value != null){
                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                finishedOrderDataArrayList.add(dc.getDocument().toObject(FinishedOrderData.class));
                            }

                            /*
                            if(dc.getType() == DocumentChange.Type.REMOVED){
                                int position = myViewHolder.getAdapterPosition();
                                finishedOrderDataArrayList.remove(position);
                            }
                             */

                            finishedOrderAdapter.notifyDataSetChanged();
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

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if(direction == ItemTouchHelper.LEFT){
                FinishedOrderAdapter.MyViewHolder myViewHolder = (FinishedOrderAdapter.MyViewHolder) viewHolder;
                finishedOrderDataArrayList.remove(myViewHolder.getAdapterPosition());
                finishedOrderAdapter.notifyItemRemoved(myViewHolder.getAdapterPosition());
                myViewHolder.updateItem();
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(requireContext(), R.color.orange))
                    .addActionIcon(R.drawable.ic_delivered)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    public void onCardClick(int position) {
        Intent intent = new Intent(getActivity(),FinishedOrderDetails.class);
        intent.putExtra("Selected_card",finishedOrderDataArrayList.get(position));
        startActivity(intent);
    }

    @Override
    public void handleDeleteItem(DocumentSnapshot snapshot) {

        // send notification
        fStore.collection("Tokens").document(snapshot.getString("customerName") +"sewIt")
                .get()
                .addOnSuccessListener(snapshot1 -> {
                    String token = snapshot1.getString("token");
                    sendFCM.pushNotification(getContext(),token,"Order delivered", "Order for "
                            + snapshot.getString("item") + " (Order Id: " + snapshot.getString("orderId") + ") "
                            +"has been delivered, rate the tailor " + snapshot.getString("tailorName")
                    ,"OrderStatusFragment");
                });

        Toast.makeText(getActivity(),"Delivered", Toast.LENGTH_SHORT).show();
    }
}