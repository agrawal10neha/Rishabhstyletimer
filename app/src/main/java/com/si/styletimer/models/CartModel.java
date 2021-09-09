
package com.si.styletimer.models;

import com.google.gson.annotations.SerializedName;

public class CartModel {

    @SerializedName("buffer_time")
    private String mBufferTime;
    @SerializedName("category_name")
    private String mCategoryName;
    @SerializedName("discount_price")
    private String mDiscountPrice;
    @SerializedName("duration")
    private String mDuration;
    @SerializedName("id")
    private String mId;
    @SerializedName("merchant_id")
    private String mMerchantId;
    @SerializedName("name")
    private String mName;
    @SerializedName("price")
    private String mPrice;
    @SerializedName("service_id")
    private String mServiceId;
    @SerializedName("cancel_booking_allow")
    private String cancelBookingAllow;
    @SerializedName("hr_before_cancel")
    private String hrBeforeCancel;

    @SerializedName("discount_percent")
    private String discountPercent;
    @SerializedName("price_start_option")
    private String priceStartOption;

    public String getPriceStartOption() {
        return priceStartOption;
    }

    public void setPriceStartOption(String priceStartOption) {
        this.priceStartOption = priceStartOption;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getCancelBookingAllow() {
        return cancelBookingAllow;
    }

    public void setCancelBookingAllow(String cancelBookingAllow) {
        this.cancelBookingAllow = cancelBookingAllow;
    }

    public String getHrBeforeCancel() {
        return hrBeforeCancel;
    }

    public void setHrBeforeCancel(String hrBeforeCancel) {
        this.hrBeforeCancel = hrBeforeCancel;
    }

    public String getBufferTime() {
        return mBufferTime;
    }

    public void setBufferTime(String bufferTime) {
        mBufferTime = bufferTime;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        mCategoryName = categoryName;
    }

    public String getDiscountPrice() {
        return mDiscountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        mDiscountPrice = discountPrice;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
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

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getServiceId() {
        return mServiceId;
    }

    public void setServiceId(String serviceId) {
        mServiceId = serviceId;
    }

    @Override
    public String toString() {
        return "CartModel{" +
                "mBufferTime='" + mBufferTime + '\'' +
                ", mCategoryName='" + mCategoryName + '\'' +
                ", mDiscountPrice='" + mDiscountPrice + '\'' +
                ", mDuration='" + mDuration + '\'' +
                ", mId='" + mId + '\'' +
                ", mMerchantId='" + mMerchantId + '\'' +
                ", mName='" + mName + '\'' +
                ", mPrice='" + mPrice + '\'' +
                ", mServiceId='" + mServiceId + '\'' +
                '}';
    }
}
