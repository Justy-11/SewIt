package com.example.sewit;

import android.os.Parcel;
import android.os.Parcelable;

public class TailorOrderData implements Parcelable {

    String orderId, item, customerName,price,distance;

    public TailorOrderData(){}

    public TailorOrderData(String orderId, String item, String customerName, String price, String distance) {
        this.orderId = orderId;
        this.item = item;
        this.customerName = customerName;
        this.price = price;
        this.distance = distance;
    }

    protected TailorOrderData(Parcel in) {
        orderId = in.readString();
        item = in.readString();
        customerName = in.readString();
        price = in.readString();
        distance = in.readString();
    }

    public static final Creator<TailorOrderData> CREATOR = new Creator<TailorOrderData>() {
        @Override
        public TailorOrderData createFromParcel(Parcel in) {
            return new TailorOrderData(in);
        }

        @Override
        public TailorOrderData[] newArray(int size) {
            return new TailorOrderData[size];
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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
        dest.writeString(price);
        dest.writeString(distance);
    }
}
