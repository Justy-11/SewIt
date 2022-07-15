package com.example.sewit;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomerAcceptedReqData implements Parcelable {

    String reqId, item, tailorName,distance,price;
    int rating;

    public CustomerAcceptedReqData(){}

    public CustomerAcceptedReqData(String reqId, String item, String tailorName, String distance, String price,int rating) {
        this.reqId = reqId;
        this.item = item;
        this.tailorName = tailorName;
        this.distance = distance;
        this.price = price;
        this.rating = rating;
    }

    protected CustomerAcceptedReqData(Parcel in) {
        reqId = in.readString();
        item = in.readString();
        tailorName = in.readString();
        distance = in.readString();
        price = in.readString();
        rating = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reqId);
        dest.writeString(item);
        dest.writeString(tailorName);
        dest.writeString(distance);
        dest.writeString(price);
        dest.writeInt(rating);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CustomerAcceptedReqData> CREATOR = new Creator<CustomerAcceptedReqData>() {
        @Override
        public CustomerAcceptedReqData createFromParcel(Parcel in) {
            return new CustomerAcceptedReqData(in);
        }

        @Override
        public CustomerAcceptedReqData[] newArray(int size) {
            return new CustomerAcceptedReqData[size];
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

    public String getTailorName() {
        return tailorName;
    }

    public void setTailorName(String tailorName) {
        this.tailorName = tailorName;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
