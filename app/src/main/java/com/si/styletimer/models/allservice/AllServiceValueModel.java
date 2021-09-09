
package com.si.styletimer.models.allservice;

import com.google.gson.annotations.SerializedName;


public class AllServiceValueModel {

    @SerializedName("category_name")
    private String mCategoryName;
    @SerializedName("discount_price")
    private String mDiscountPrice;
    @SerializedName("duration")
    private String mDuration;
    @SerializedName("id")
    private String mId;
    @SerializedName("in_cart")
    private String mInCart;
    @SerializedName("name")
    private String mName;
    @SerializedName("price")
    private String mPrice;
    @SerializedName("subcategory_id")
    private String mSubcategoryId;
    @SerializedName("subcatid")
    private String mSubcatid;

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

    public String getInCart() {
        return mInCart;
    }

    public void setInCart(String inCart) {
        mInCart = inCart;
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

    public String getSubcategoryId() {
        return mSubcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        mSubcategoryId = subcategoryId;
    }

    public String getSubcatid() {
        return mSubcatid;
    }

    public void setSubcatid(String subcatid) {
        mSubcatid = subcatid;
    }

    @Override
    public String toString() {
        return "AllServiceValueModel{" +
                "mCategoryName='" + mCategoryName + '\'' +
                ", mDiscountPrice='" + mDiscountPrice + '\'' +
                ", mDuration='" + mDuration + '\'' +
                ", mId='" + mId + '\'' +
                ", mInCart='" + mInCart + '\'' +
                ", mName='" + mName + '\'' +
                ", mPrice='" + mPrice + '\'' +
                ", mSubcategoryId='" + mSubcategoryId + '\'' +
                ", mSubcatid='" + mSubcatid + '\'' +
                '}';
    }
}
