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

public class TailorReqAdapter extends RecyclerView.Adapter<TailorReqAdapter.MyViewHolder> {

    Context context;
    ArrayList<TailorReqData> tailorReqDataArrayList;
    onCardListener mOnCardListener;

    public TailorReqAdapter(Context context, ArrayList<TailorReqData> tailorReqDataArrayList,onCardListener OnCardListener) {
        this.context = context;
        this.tailorReqDataArrayList = tailorReqDataArrayList;
        this.mOnCardListener = OnCardListener;
    }

    @NonNull
    @Override
    public TailorReqAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.tailor_req_item,parent,false);

        return new MyViewHolder(v,mOnCardListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TailorReqAdapter.MyViewHolder holder, int position) {
        TailorReqData tailorReqData = tailorReqDataArrayList.get(position);

        holder.ReqID.setText(String.valueOf(tailorReqData.reqId));
        holder.ReqItem.setText(String.valueOf(tailorReqData.item));
        holder.ReqFrom.setText(String.valueOf(tailorReqData.customerName));
        holder.ReqDistance.setText(String.valueOf(tailorReqData.distance));
    }

    @Override
    public int getItemCount() {
        return tailorReqDataArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView ReqID, ReqItem, ReqFrom,ReqDistance;
        onCardListener oncardlistener;

        public MyViewHolder(@NonNull View itemView,onCardListener oncardlistener) {
            super(itemView);
            ReqID = itemView.findViewById(R.id.getTailorReqID);
            ReqItem = itemView.findViewById(R.id.getTailorReqItem);
            ReqFrom = itemView.findViewById(R.id.getTailorReqFrom);
            ReqDistance = itemView.findViewById(R.id.getTailorReqDistance);
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
                                    .document(customerId).collection("Requests");

                            cr2.whereEqualTo("reqId",ReqID.getText().toString())
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                        for(QueryDocumentSnapshot snapshot1: queryDocumentSnapshots1){

                                            // first add it to another document, then delete the document
                                            DocumentReference df3 =  fStore.collection("deletedRequests").document();
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
