
package com.si.styletimer.models;

import com.google.gson.annotations.SerializedName;


public class SelectTimeModel {

    @SerializedName("discount")
    private String mDiscount;
    @SerializedName("price")
    private String mPrice;
    @SerializedName("time")
    private String mTime;
    private String isChecked;
    @SerializedName("price_start_option")
    private String priceStartOption;

    public String getPriceStartOption() {
        return priceStartOption;
    }

    public void setPriceStartOption(String priceStartOption) {
        this.priceStartOption = priceStartOption;
    }
    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getDiscount() {
        return mDiscount;
    }

    public void setDiscount(String discount) {
        mDiscount = discount;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    @Override
    public String toString() {
        return "SelectTimeModel{" +
                "mDiscount='" + mDiscount + '\'' +
                ", mPrice=" + mPrice +
                ", mTime='" + mTime + '\'' +
                '}';
    }
}
