package com.example.sewit;

import android.os.Parcel;
import android.os.Parcelable;

public class FinishedOrderData implements Parcelable {

    String orderId, item, customerName,status;

    public FinishedOrderData(){}

    public FinishedOrderData(String orderId, String item, String customerName, String status) {
        this.orderId = orderId;
        this.item = item;
        this.customerName = customerName;
        this.status = status;
    }

    protected FinishedOrderData(Parcel in) {
        orderId = in.readString();
        item = in.readString();
        customerName = in.readString();
        status = in.readString();
    }

    public static final Creator<FinishedOrderData> CREATOR = new Creator<FinishedOrderData>() {
        @Override
        public FinishedOrderData createFromParcel(Parcel in) {
            return new FinishedOrderData(in);
        }

        @Override
        public FinishedOrderData[] newArray(int size) {
            return new FinishedOrderData[size];
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
        dest.writeString(customerName);
        dest.writeString(status);
    }
}
