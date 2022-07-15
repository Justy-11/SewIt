package com.example.sewit;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderStatusData implements Parcelable {

    String orderId, item, tailorName,status;

    public OrderStatusData(){}

    public OrderStatusData(String orderId, String item, String tailorName, String status) {
        this.orderId = orderId;
        this.item = item;
        this.tailorName = tailorName;
        this.status = status;
    }

    protected OrderStatusData(Parcel in) {
        orderId = in.readString();
        item = in.readString();
        tailorName = in.readString();
        status = in.readString();
    }

    public static final Creator<OrderStatusData> CREATOR = new Creator<OrderStatusData>() {
        @Override
        public OrderStatusData createFromParcel(Parcel in) {
            return new OrderStatusData(in);
        }

        @Override
        public OrderStatusData[] newArray(int size) {
            return new OrderStatusData[size];
        }
    };

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getTailorName() {
        return tailorName;
    }

    public void setTailorName(String tailorName) {
        this.tailorName = tailorName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderId);
        dest.writeString(item);
        dest.writeString(tailorName);
        dest.writeString(status);
    }
}
