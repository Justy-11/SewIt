package com.example.sewit;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;


public class OrderStatusFragment extends Fragment implements OrderStatusAdapter.onCardListener{

    RecyclerView recyclerView;
    ArrayList<OrderStatusData> orderStatusDataArrayList;
    OrderStatusAdapter orderStatusAdapter;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    ProgressDialog progressDialog;
    OrderStatusAdapter.MyViewHolder myViewHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_order_status, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        recyclerView = v.findViewById(R.id.recyclerViewOrderStatus);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        orderStatusDataArrayList = new ArrayList<>();
        orderStatusAdapter = new OrderStatusAdapter(getActivity(),orderStatusDataArrayList,this);

        recyclerView.setAdapter(orderStatusAdapter);

        EventChangeListener();

        return v;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void EventChangeListener() {

        orderStatusDataArrayList.clear();
        orderStatusAdapter.notifyDataSetChanged();

        FirebaseUser user = fAuth.getCurrentUser();
        CollectionReference cr = fStore.collection("OrderStatus");

        assert user != null;
        cr.whereEqualTo("customerId",user.getUid())
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
                                orderStatusDataArrayList.add(dc.getDocument().toObject(OrderStatusData.class));
                            }

                            //comment this if need to add swipe
                            if(dc.getType() == DocumentChange.Type.REMOVED){
                                int position = myViewHolder.getAdapterPosition();
                                orderStatusDataArrayList.remove(position);
                            }

                            orderStatusAdapter.notifyDataSetChanged();
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


    //TODO - test
    /*
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if(direction == ItemTouchHelper.LEFT){
                OrderStatusAdapter.MyViewHolder myViewHolder = (OrderStatusAdapter.MyViewHolder) viewHolder;
                orderStatusDataArrayList.remove(myViewHolder.getAdapterPosition());
                orderStatusAdapter.notifyItemRemoved(myViewHolder.getAdapterPosition());
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

     */

    @Override
    public void onCardClick(int position) {
        Intent intent = new Intent(getActivity(),OrderStatusDetails.class);
        //attached to a bundle
        intent.putExtra("Selected_card",orderStatusDataArrayList.get(position));
        startActivity(intent);
    }

    @Override
    public void handleDeleteItem(DocumentSnapshot snapshot) {
        //todo test
        /*snapshot.getReference().delete()
                .addOnSuccessListener(unused -> Toast.makeText(getActivity(),"Request deleted", Toast.LENGTH_SHORT).show());

         */
    }
}