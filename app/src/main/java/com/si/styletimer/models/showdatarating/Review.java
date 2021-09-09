
package com.si.styletimer.models.showdatarating;

import com.google.gson.annotations.SerializedName;


public class Review {

    @SerializedName("anonymous")
    private String mAnonymous;
    @SerializedName("booking_id")
    private String mBookingId;
    @SerializedName("created_on")
    private String mCreatedOn;
    @SerializedName("emp_id")
    private String mEmpId;
    @SerializedName("employee")
    private String mEmployee;
    @SerializedName("id")
    private String mId;
    @SerializedName("rate")
    private String mRate;
    @SerializedName("review")
    private String mReview;
    @SerializedName("username")
    private String mUsername;
    @SerializedName("time_ago")
    private String timeAgo;

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }
    public String getAnonymous() {
        return mAnonymous;
    }

    public void setAnonymous(String anonymous) {
        mAnonymous = anonymous;
    }

    public String getBookingId() {
        return mBookingId;
    }

    public void setBookingId(String bookingId) {
        mBookingId = bookingId;
    }

    public String getCreatedOn() {
        return mCreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        mCreatedOn = createdOn;
    }

    public String getEmpId() {
        return mEmpId;
    }

    public void setEmpId(String empId) {
        mEmpId = empId;
    }

    public String getEmployee() {
        return mEmployee;
    }

    public void setEmployee(String employee) {
        mEmployee = employee;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
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

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

}
