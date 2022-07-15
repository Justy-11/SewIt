package com.example.sewit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;


import java.util.ArrayList;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.MyViewHolder>{

    Context context;
    ArrayList<OrderStatusData> orderStatusDataArrayList;
    onCardListener mOnCardListener;

    public OrderStatusAdapter(Context context, ArrayList<OrderStatusData> orderStatusDataArrayList, onCardListener OnCardListener) {
        this.context = context;
        this.orderStatusDataArrayList = orderStatusDataArrayList;
        this.mOnCardListener = OnCardListener;
    }


    @NonNull
    @Override
    public OrderStatusAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.order_status_item,parent,false);

        return new MyViewHolder(v,mOnCardListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderStatusAdapter.MyViewHolder holder, int position) {

        OrderStatusData orderStatusData = orderStatusDataArrayList.get(position);
        holder.OrderID.setText(String.valueOf(orderStatusData.orderId));
        holder.Item.setText(String.valueOf(orderStatusData.item));
        holder.Name.setText(String.valueOf(orderStatusData.tailorName));
        holder.Status.setText(String.valueOf(orderStatusData.status));
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView OrderID, Item, Name,Status;
        onCardListener oncardlistener;

        public MyViewHolder(@NonNull View itemView, onCardListener oncardlistener) {
            super(itemView);

            OrderID = itemView.findViewById(R.id.getOrderID);
            Item = itemView.findViewById(R.id.getOrderItem);
            Name = itemView.findViewById(R.id.getTailorName);
            Status = itemView.findViewById(R.id.getStatus);
            this.oncardlistener = oncardlistener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            oncardlistener.onCardClick(getAdapterPosition());
        }

        public void deleteItem(){

            /*FirebaseFirestore fStore = FirebaseFirestore.getInstance();
            CollectionReference cr = fStore.collection("OrderStatus");

            cr.whereEqualTo("orderId",OrderID.getText().toString())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {

                        for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots) {
                            DocumentReference df3 =  fStore.collection("deletedAcceptedRequests").document();
                            Map<String,Object> deletedInfo;
                            deletedInfo = snapshot.getData();
                            df3.set(deletedInfo);

                            oncardlistener.handleDeleteItem(snapshot);
                        }

                    });

             */
        }
    }

    @Override
    public int getItemCount() {
        return orderStatusDataArrayList.size();
    }

    public interface onCardListener{
        void onCardClick(int position);
        void handleDeleteItem(DocumentSnapshot snapshot);
    }
}
