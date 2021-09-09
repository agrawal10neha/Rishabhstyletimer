
package com.si.styletimer.models;

import com.google.gson.annotations.SerializedName;

public class SearchListModel {

    @SerializedName("address")
    private String mAddress;
    @SerializedName("business_name")
    private String mBusinessName;
    @SerializedName("city")
    private String mCity;
    @SerializedName("country")
    private String mCountry;
    @SerializedName("id")
    private String mId;
    @SerializedName("rating")
    private String mRating;
    @SerializedName("reviewcount")
    private String mReviewcount;
    @SerializedName("zip")
    private String mZip;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getBusinessName() {
        return mBusinessName;
    }

    public void setBusinessName(String businessName) {
        mBusinessName = businessName;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getRating() {
        return mRating;
    }

    public void setRating(String rating) {
        mRating = rating;
    }

    public String getReviewcount() {
        return mReviewcount;
    }

    public void setReviewcount(String reviewcount) {
        mReviewcount = reviewcount;
    }

    public String getZip() {
        return mZip;
    }

    public void setZip(String zip) {
        mZip = zip;
    }

    @Override
    public String toString() {
        return "SearchListModel{" +
                "mAddress='" + mAddress + '\'' +
                ", mBusinessName='" + mBusinessName + '\'' +
                ", mCity='" + mCity + '\'' +
                ", mCountry='" + mCountry + '\'' +
                ", mId='" + mId + '\'' +
                ", mRating='" + mRating + '\'' +
                ", mReviewcount='" + mReviewcount + '\'' +
                ", mZip='" + mZip + '\'' +
                '}';
    }

    public String myaddress(){
        return mAddress+"\n"+mZip+" "+mCity;
    }
}
