package com.example.sewit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class CustomerAcceptedReqAdapter extends RecyclerView.Adapter<CustomerAcceptedReqAdapter.MyViewHolder>{

    Context context;
    ArrayList<CustomerAcceptedReqData> customerAcceptedReqDataArrayList;
    onCardListener mOnCardListener;

    public CustomerAcceptedReqAdapter(Context context, ArrayList<CustomerAcceptedReqData> customerAcceptedReqDataArrayList, onCardListener OnCardListener) {
        this.context = context;
        this.customerAcceptedReqDataArrayList = customerAcceptedReqDataArrayList;
        this.mOnCardListener = OnCardListener;
    }

    @NonNull
    @Override
    public CustomerAcceptedReqAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.customer_accepted_req_item,parent,false);

        return new MyViewHolder(v,mOnCardListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAcceptedReqAdapter.MyViewHolder holder, int position) {

        CustomerAcceptedReqData customerAcceptedReqData = customerAcceptedReqDataArrayList.get(position);

        holder.ReqID.setText(String.valueOf(customerAcceptedReqData.reqId));
        holder.ReqItem.setText(String.valueOf(customerAcceptedReqData.item));
        holder.From.setText(String.valueOf(customerAcceptedReqData.tailorName));
        holder.Distance.setText(String.valueOf(customerAcceptedReqData.distance));
        holder.Price.setText(String.valueOf(customerAcceptedReqData.price));
        holder.Rating.setText(String.valueOf(customerAcceptedReqData.rating));
    }

    @Override
    public int getItemCount() {
        return customerAcceptedReqDataArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView ReqID, ReqItem, From, Distance,Price,Rating;
        onCardListener oncardlistener;

        public MyViewHolder(@NonNull View itemView, onCardListener oncardlistener) {
            super(itemView);

            ReqID = itemView.findViewById(R.id.getCustomerAcceptedReqID);
            ReqItem = itemView.findViewById(R.id.getCustomerAcceptedReqItem);
            From = itemView.findViewById(R.id.getCustomerAcceptedReqFrom);
            Distance = itemView.findViewById(R.id.getCustomerAcceptedReqDistance);
            Price = itemView.findViewById(R.id.getCustomerAcceptedReqPrice);
            Rating = itemView.findViewById(R.id.TailorRating);
            this.oncardlistener = oncardlistener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            oncardlistener.onCardClick(getAdapterPosition());
        }

        public void deleteItem() {
            FirebaseFirestore fStore = FirebaseFirestore.getInstance();
            CollectionReference cr = fStore.collection("Users");

            cr.whereEqualTo("isTailor","1")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots) {
                            String tailorId = snapshot.getId();
                            CollectionReference cr2 =  fStore.collection("Users")
                                    .document(tailorId).collection("AcceptedCustomerRequests");

                            cr2.whereEqualTo("reqId",ReqID.getText().toString())
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                        for(QueryDocumentSnapshot snapshot1: queryDocumentSnapshots1){

                                            // first add it to another document, then delete the document
                                            DocumentReference df3 =  fStore.collection("deletedAcceptedRequests").document();
                                            Map<String,Object> deletedInfo;
                                            deletedInfo = snapshot1.getData();
                                            df3.set(deletedInfo);

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
