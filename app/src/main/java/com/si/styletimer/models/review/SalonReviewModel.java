
package com.si.styletimer.models.review;


import com.google.gson.annotations.SerializedName;


public class SalonReviewModel {

    @SerializedName("service_name")
    private String mServiceName;
    @SerializedName("emp_firstname")
    private String mEmpFirstname;
    @SerializedName("created_on")
    private String mCreatedOn;
    @SerializedName("first_name")
    private String mFirstName;
    @SerializedName("last_name")
    private String mLastName;
    @SerializedName("rate")
    private String mRate;
    @SerializedName("review")
    private String mReview;
    @SerializedName("user_id")
    private String mUserId;
    @SerializedName("id")
    private String mId;
    @SerializedName("anonymous")
    private String mAnonymous;
    @SerializedName("time_ago")
    private String mTimeAgo;

    public String getmServiceName() {
        return mServiceName;
    }

    public void setmServiceName(String mServiceName) {
        this.mServiceName = mServiceName;
    }

    public String getmEmpFirstname() {
        return mEmpFirstname;
    }

    public void setmEmpFirstname(String mEmpFirstname) {
        this.mEmpFirstname = mEmpFirstname;
    }

    public String getCreatedOn() {
        return mCreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        mCreatedOn = createdOn;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getRate() {
        return mRate;
    }

    public void setRate(String rate) {
        mRate = rate;
    }

    public String getReview() {
        return mReview;
    }

    public void setReview(String review) {
        mReview = review;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getId() {
        return mId;
    }

    public void setId(String Id) {
        mId = Id;
    }


    public String getAnonymous() {
        return mAnonymous;
    }

    public void setAnonymous(String Anonymous) {
        mAnonymous = Anonymous;
    }



    public String getAgo() {
        return mTimeAgo;
    }

    public void setAgo(String TimeAgo) {
        mTimeAgo = TimeAgo;
    }


}
