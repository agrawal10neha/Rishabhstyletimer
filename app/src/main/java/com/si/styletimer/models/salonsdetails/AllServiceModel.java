
package com.si.styletimer.models.salonsdetails;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class AllServiceModel {

    @SerializedName("category_name")
    private String mCategoryName;
    @SerializedName("count")
    private String mCount;
    @SerializedName("id")
    private String mId;
    @SerializedName("sub_services")
    private List<SubServiceModel> mSubServices;
    @SerializedName("subid")
    private String mSubid;

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        mCategoryName = categoryName;
    }

    public String getCount() {
        return mCount;
    }

    public void setCount(String count) {
        mCount = count;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public List<SubServiceModel> getSubServices() {
        return mSubServices;
    }

    public void setSubServices(List<SubServiceModel> subServices) {
        mSubServices = subServices;
    }

    public String getSubid() {
        return mSubid;
    }

    public void setSubid(String subid) {
        mSubid = subid;
    }

    @Override
    public String toString() {
        return "AllServiceTwoModel{" +
                "mCategoryName='" + mCategoryName + '\'' +
                ", mCount='" + mCount + '\'' +
                ", mId='" + mId + '\'' +
                ", mSubServices=" + mSubServices +
                ", mSubid='" + mSubid + '\'' +
                '}';
    }
}
