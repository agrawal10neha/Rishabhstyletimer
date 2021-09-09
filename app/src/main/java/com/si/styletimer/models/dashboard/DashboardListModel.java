
package com.si.styletimer.models.dashboard;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DashboardListModel {

    @SerializedName("category_name")
    private String mCategoryName;
    @SerializedName("id")
    private String mId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("sub")
    private String mSub;
    @SerializedName("sub_category")
    private List<SubCategory> mSubCategory;

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

    public String getSub() {
        return mSub;
    }

    public void setSub(String sub) {
        mSub = sub;
    }

    public List<SubCategory> getSubCategory() {
        return mSubCategory;
    }

    public void setSubCategory(List<SubCategory> subCategory) {
        mSubCategory = subCategory;
    }

}
