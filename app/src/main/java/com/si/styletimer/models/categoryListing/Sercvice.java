
package com.si.styletimer.models.categoryListing;

import com.google.gson.annotations.SerializedName;


public class Sercvice {

    @SerializedName("buffer_time")
    private String mBufferTime;
    @SerializedName("discount_price")
    private String mDiscountPrice;
    @SerializedName("duration")
    private String mDuration;
    @SerializedName("id")
    private String mId;
    @SerializedName("m_category")
    private String mMCategory;
    @SerializedName("price")
    private String mPrice;
    @SerializedName("s_category")
    private String mSCategory;
    @SerializedName("subcategory_id")
    private String mSubcategoryId;

    @SerializedName("price_start_option")
    private String priceStartOption;
    @SerializedName("totalservice")
    private int totalService;

    public int getTotalService() {
        return totalService;
    }

    public void setTotalService(int totalService) {
        this.totalService = totalService;
    }

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

    public String getMCategory() {
        return mMCategory;
    }

    public void setMCategory(String mCategory) {
        mMCategory = mCategory;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getSCategory() {
        return mSCategory;
    }

    public void setSCategory(String sCategory) {
        mSCategory = sCategory;
    }

    public String getSubcategoryId() {
        return mSubcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        mSubcategoryId = subcategoryId;
    }

    @Override
    public String toString() {
        return "Sercvice{" +
                "mBufferTime='" + mBufferTime + '\'' +
                ", mDiscountPrice='" + mDiscountPrice + '\'' +
                ", mDuration='" + mDuration + '\'' +
                ", mId='" + mId + '\'' +
                ", mMCategory='" + mMCategory + '\'' +
                ", mPrice='" + mPrice + '\'' +
                ", mSCategory='" + mSCategory + '\'' +
                ", mSubcategoryId='" + mSubcategoryId + '\'' +
                '}';
    }
}
