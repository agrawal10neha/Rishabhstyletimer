package com.si.styletimer.models.mybooking_list_model.confirm_booking_model;

import com.google.gson.annotations.SerializedName;

public class AllCategory {
    public String getmCategoryName() {
        return mCategoryName;
    }

    public void setmCategoryName(String mCategoryName) {
        this.mCategoryName = mCategoryName;
    }

    public String getmServiceName() {
        return mServiceName;
    }

    public void setmServiceName(String mServiceName) {
        this.mServiceName = mServiceName;
    }

    @SerializedName("category_name")
    private String mCategoryName;

    @SerializedName("service_name")
    private String mServiceName;
}
