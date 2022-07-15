package com.example.sewit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FinishedOrderAdapter extends RecyclerView.Adapter<FinishedOrderAdapter.MyViewHolder>{

    Context context;
    ArrayList<FinishedOrderData> finishedOrderDataArrayList;
    onCardListener mOnCardListener;

    public FinishedOrderAdapter(Context context, ArrayList<FinishedOrderData> finishedOrderDataArrayList, onCardListener mOnCardListener) {
        this.context = context;
        this.finishedOrderDataArrayList = finishedOrderDataArrayList;
        this.mOnCardListener = mOnCardListener;
    }

    @NonNull
    @Override
    public FinishedOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.finished_order_tem,parent,false);

        return new MyViewHolder(v,mOnCardListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FinishedOrderAdapter.MyViewHolder holder, int position) {

        FinishedOrderData finishedOrderData = finishedOrderDataArrayList.get(position);
        holder.OrderID.setText(String.valueOf(finishedOrderData.orderId));
        holder.Item.setText(String.valueOf(finishedOrderData.item));
        holder.Name.setText(String.valueOf(finishedOrderData.customerName));
        holder.Status.setText(String.valueOf(finishedOrderData.status));
    }

    @Override
    public int getItemCount() {
        return finishedOrderDataArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView OrderID, Item, Name,Status;
        onCardListener oncardlistener;

        public MyViewHolder(@NonNull View itemView, onCardListener oncardlistener) {
            super(itemView);

            OrderID = itemView.findViewById(R.id.getOrderID);
            Item = itemView.findViewById(R.id.getOrderItem);
            Name = itemView.findViewById(R.id.getCustomerName);
            Status = itemView.findViewById(R.id.getStatus);
            this.oncardlistener = oncardlistener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            oncardlistener.onCardClick(getAdapterPosition());
        }

        public void updateItem(){
            FirebaseFirestore fStore = FirebaseFirestore.getInstance();
            FirebaseAuth fAuth = FirebaseAuth.getInstance();
            CollectionReference cr = fStore.collection("OrderStatus");

            cr.whereEqualTo("orderId",OrderID.getText().toString())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {

                        for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots) {
                            String id = snapshot.getId();

                            Map<String,Object> map = new HashMap<>();
                            map.put("status","delivered");
                            map.put("tailorUnique", Objects.requireNonNull(fAuth.getCurrentUser()).getUid()+"delivered");
                            map.put("orderDeliveredAt", Timestamp.now());

                            fStore.collection("OrderStatus").document(id)
                                    .update(map);

                            oncardlistener.handleDeleteItem(snapshot);
                        }

                    });

        }

    }


    public interface onCardListener{
        void onCardClick(int position);
        void handleDeleteItem(DocumentSnapshot snapshot);
    }
}

