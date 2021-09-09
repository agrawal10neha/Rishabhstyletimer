
package com.si.styletimer.models.salonDetails;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class SalonMainCategoryModel  {

    @SerializedName("category_name")
    private String mCategoryName;
    @SerializedName("icon")
    private String mIcon;
    @SerializedName("id")
    private String mId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("sub")
    private String mSub;
    @SerializedName("total_in_cart")
    private long totalInCart;

    public long getTotalInCart() {
        return totalInCart;
    }

    public void setTotalInCart(long totalInCart) {
        this.totalInCart = totalInCart;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        mCategoryName = categoryName;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getSub() {
        return mSub;
    }

    public void setSub(String sub) {
        mSub = sub;
    }


    @Override
    public String toString() {
        return "SalonMainCategoryModel{" +
                "mCategoryName='" + mCategoryName + '\'' +
                ", mIcon='" + mIcon + '\'' +
                ", mId='" + mId + '\'' +
                ", mImage='" + mImage + '\'' +
                ", mSub='" + mSub + '\'' +
                '}';
    }
}
