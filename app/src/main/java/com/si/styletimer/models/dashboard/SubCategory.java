
package com.si.styletimer.models.dashboard;

import com.google.gson.annotations.SerializedName;

public class SubCategory {

    @SerializedName("category_name")
    private String mCategoryName;
    @SerializedName("id")
    private String mId;
    @SerializedName("parent_id")
    private String mParentId;

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

    public String getParentId() {
        return mParentId;
    }

    public void setParentId(String parentId) {
        mParentId = parentId;
    }

}
