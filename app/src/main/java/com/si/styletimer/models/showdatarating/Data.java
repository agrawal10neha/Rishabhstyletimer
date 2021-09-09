
package com.si.styletimer.models.showdatarating;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("avgrate_count")
    private AvgrateCount mAvgrateCount;
    @SerializedName("cat_name")
    private String mCatName;
    @SerializedName("id")
    private String mId;
    @SerializedName("reviews")
    private List<Review> mReviews;
    @SerializedName("service_detail")
    private String mServiceDetail;
    @SerializedName("subcategory_id")
    private String mSubcategoryId;


    public AvgrateCount getAvgrateCount() {
        return mAvgrateCount;
    }

    public void setAvgrateCount(AvgrateCount avgrateCount) {
        mAvgrateCount = avgrateCount;
    }

    public String getCatName() {
        return mCatName;
    }

    public void setCatName(String catName) {
        mCatName = catName;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public List<Review> getReviews() {
        return mReviews;
    }

    public void setReviews(List<Review> reviews) {
        mReviews = reviews;
    }

    public String getServiceDetail() {
        return mServiceDetail;
    }

    public void setServiceDetail(String serviceDetail) {
        mServiceDetail = serviceDetail;
    }

    public String getSubcategoryId() {
        return mSubcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        mSubcategoryId = subcategoryId;
    }

}
