
package com.si.styletimer.models.salonsdetails;

import com.google.gson.annotations.SerializedName;

public class SubServiceModel {

    @SerializedName("category_name")
    private String mCategoryName;
    @SerializedName("discount_price")
    private String mDiscountPrice;
    @SerializedName("duration")
    private String mDuration;
    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("price")
    private String mPrice;

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

}
