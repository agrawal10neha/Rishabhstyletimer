
package com.si.styletimer.models.salonsdetails;

import com.google.gson.annotations.SerializedName;


public class MainCategoryModel {

    @SerializedName("category_name")
    private String mCategoryName;
    @SerializedName("id")
    private String mId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("icon")
    private String mIcon;
    @SerializedName("sub")
    private String mSub;

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        mCategoryName = categoryName;
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




    public String getIcon() {
        return mIcon;
    }
    public void setIcon(String icon) {
        mIcon= icon;
    }



    public String getSub() {
        return mSub;
    }

    public void setSub(String sub) {
        mSub = sub;
    }

    @Override
    public String toString() {
        return "MainCategoryModel{" +
                "mCategoryName='" + mCategoryName + '\'' +
                ", mId='" + mId + '\'' +
                ", mImage='" + mImage + '\'' +
                ", mIcon='" + mIcon + '\'' +
                ", mSub='" + mSub + '\'' +
                '}';
    }
}
