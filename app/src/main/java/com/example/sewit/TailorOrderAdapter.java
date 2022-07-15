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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TailorOrderAdapter extends RecyclerView.Adapter<TailorOrderAdapter.MyViewHolder>{

    Context context;
    ArrayList<TailorOrderData> tailorOrderDataArrayList;
    onCardListener mOnCardListener;

    public TailorOrderAdapter(Context context, ArrayList<TailorOrderData> tailorOrderDataArrayList,onCardListener OnCardListener) {
        this.context = context;
        this.tailorOrderDataArrayList = tailorOrderDataArrayList;
        this.mOnCardListener = OnCardListener;
    }

    @NonNull
    @Override
    public TailorOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.tailor_orders_item,parent,false);

        return new MyViewHolder(v,mOnCardListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TailorOrderAdapter.MyViewHolder holder, int position) {
        TailorOrderData tailorOrderData = tailorOrderDataArrayList.get(position);

        holder.OrderID.setText(String.valueOf(tailorOrderData.orderId));
        holder.Item.setText(String.valueOf(tailorOrderData.item));
        holder.Name.setText(String.valueOf(tailorOrderData.customerName));
        holder.Price.setText(String.valueOf(tailorOrderData.price));
        holder.Distance.setText(String.valueOf(tailorOrderData.distance));
    }

    @Override
    public int getItemCount() {
        return tailorOrderDataArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView OrderID, Item, Name,Price,Distance;
        onCardListener oncardlistener;

        public MyViewHolder(@NonNull View itemView,onCardListener oncardlistener) {
            super(itemView);

            OrderID = itemView.findViewById(R.id.getOrderID);
            Item = itemView.findViewById(R.id.getOrderItem);
            Name = itemView.findViewById(R.id.getOrderFrom);
            Price = itemView.findViewById(R.id.getPrice);
            Distance = itemView.findViewById(R.id.getDistance);
            this.oncardlistener = oncardlistener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            oncardlistener.onCardClick(getAdapterPosition());
        }

        public void deleteItem(){
            FirebaseFirestore fStore = FirebaseFirestore.getInstance();
            CollectionReference cr = fStore.collection("Users");

            cr.whereEqualTo("isCustomer","1")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots) {
                            String customerId = snapshot.getId();
                            CollectionReference cr2 =  fStore.collection("Users")
                                    .document(customerId).collection("Orders");

                            cr2.whereEqualTo("orderId",OrderID.getText().toString())
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                        for(QueryDocumentSnapshot snapshot1: queryDocumentSnapshots1){

                                            DocumentReference df =  fStore.collection("Users").document(customerId).collection("finishedOrders").document();
                                            Map<String,Object> finishedOrderInfo;
                                            finishedOrderInfo = snapshot1.getData();
                                            df.set(finishedOrderInfo);

                                            //update to finished order
                                            fStore.collection("OrderStatus")
                                                    .whereEqualTo("orderId",OrderID.getText().toString())
                                                    .get()
                                                    .addOnSuccessListener(queryDocumentSnapshots2 -> {
                                                        for(QueryDocumentSnapshot snapshot2: queryDocumentSnapshots2){
                                                            String id = snapshot2.getId();

                                                            FirebaseAuth fAuth;
                                                            fAuth = FirebaseAuth.getInstance();
                                                            FirebaseUser user = fAuth.getCurrentUser();

                                                            Map<String,Object> map = new HashMap<>();
                                                            map.put("status","finished");
                                                            assert user != null;
                                                            map.put("tailorUnique",user.getUid()+"finished");
                                                            map.put("orderFinishedAt", Timestamp.now());

                                                            fStore.collection("OrderStatus").document(id)
                                                                    .update(map);
                                                        }
                                                    });


                                            oncardlistener.handleDeleteItem(snapshot1);
                                        }
                                    });
                        }

                    });
        }
    }

    public interface onCardListener{
        void onCardClick(int position);
        void handleDeleteItem(DocumentSnapshot snapshot);
    }
}
