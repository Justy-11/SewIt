package com.example.sewit;

import android.os.Parcel;
import android.os.Parcelable;

public class TailorReqData implements Parcelable {

    //these names should be match with fireStore stored name
    String reqId, item, customerName;
    float distance;

    public TailorReqData(){}

    public TailorReqData(String reqId, String item, String customerName, float distance) {
        this.reqId = reqId;
        this.item = item;
        this.customerName = customerName;
        this.distance = distance;
    }

    protected TailorReqData(Parcel in) {
        reqId = in.readString();
        item = in.readString();
        customerName = in.readString();
        distance = in.readFloat();
    }

    public static final Creator<TailorReqData> CREATOR = new Creator<TailorReqData>() {
        @Override
        public TailorReqData createFromParcel(Parcel in) {
            return new TailorReqData(in);
        }

        @Override
        public TailorReqData[] newArray(int size) {
            return new TailorReqData[size];
        }
    };

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
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

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reqId);
        dest.writeString(item);
        dest.writeString(customerName);
        dest.writeFloat(distance);
    }
}
