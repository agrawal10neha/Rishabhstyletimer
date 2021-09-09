
package com.si.styletimer.models.bookind_detail_service;


import com.google.gson.annotations.SerializedName;


public class ServicesModel {

    @SerializedName("buffer_time")
    private String mBufferTime;
    @SerializedName("discount_price")
    private String mDiscountPrice;
    @SerializedName("duration")
    private String mDuration;
    @SerializedName("id")
    private String mId;
    @SerializedName("price")
    private String mPrice;
    @SerializedName("service_name")
    private String mServiceName;


    @SerializedName("category_name")
    private String mCatName;
    @SerializedName("price_start_option")
    private String priceStartOption;

    public String getPriceStartOption() {
        return priceStartOption;
    }

    public void setPriceStartOption(String priceStartOption) {
        this.priceStartOption = priceStartOption;
    }

    public String getBufferTime() {
        return mBufferTime;
    }

    public void setBufferTime(String bufferTime) {
        mBufferTime = bufferTime;
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

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getServiceName() {
        return mServiceName;
    }
    public void setServiceName(String serviceName) {
        mServiceName = serviceName;
    }


    public String getCatName() {
        return mCatName;
    }
    public void setCatName(String catName) {
        mCatName = catName;
    }

}
