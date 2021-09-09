
package com.si.styletimer.models.mybooking_list_model.cancelled_booking_model;


import com.google.gson.annotations.SerializedName;


public class CancelModel {

    @SerializedName("address")
    private String mAddress;
    @SerializedName("booking_time")
    private String mBookingTime;
    @SerializedName("business_name")
    private String mBusinessName;
    @SerializedName("city")
    private String mCity;
    @SerializedName("id")
    private String mId;
    @SerializedName("merchant_id")
    private String mMerchantId;
    @SerializedName("service_name")
    private String mServiceName;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("total_minutes")
    private String mTotalMinutes;
    @SerializedName("total_price")
    private String mTotalPrice;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getBookingTime() {
        return mBookingTime;
    }

    public void setBookingTime(String bookingTime) {
        mBookingTime = bookingTime;
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

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getMerchantId() {
        return mMerchantId;
    }

    public void setMerchantId(String merchantId) {
        mMerchantId = merchantId;
    }

    public String getServiceName() {
        return mServiceName;
    }

    public void setServiceName(String serviceName) {
        mServiceName = serviceName;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getTotalMinutes() {
        return mTotalMinutes;
    }

    public void setTotalMinutes(String totalMinutes) {
        mTotalMinutes = totalMinutes;
    }

    public String getTotalPrice() {
        return mTotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        mTotalPrice = totalPrice;
    }

}
