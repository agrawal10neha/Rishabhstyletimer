
package com.si.styletimer.models.mybooking_list_model.confirm_booking_model;

import com.google.gson.annotations.SerializedName;
import com.si.styletimer.models.dashboard.SubCategory;

import java.util.List;

public class ConfirmModel {

    @SerializedName("address")
    private String mAddress;
    @SerializedName("booking_time")
    private String mBookingTime;
    @SerializedName("business_name")
    private String mBusinessName;
    @SerializedName("city")
    private String mCity;
    @SerializedName("id")
    private String mId;
    @SerializedName("merchant_id")
    private String mMerchantId;
    @SerializedName("service_name")
    private String mServiceName;


    public List<AllCategory> getAllCategory() {
        return allCategory;
    }

    public void setAllCategory(List<AllCategory> allCategory) {
        this.allCategory = allCategory;
    }
    @SerializedName("all_services")
    private List<AllCategory> allCategory;

    @SerializedName("status")
    private String mStatus;
    @SerializedName("total_minutes")
    private String mTotalMinutes;
    @SerializedName("total_price")
    private String mTotalPrice;
    @SerializedName("category_name")
    private String mCatName;
    @SerializedName("employee_id")
    private String mEmployeeId;
    @SerializedName("cancel_booking_allow")
    private String cancelBookingAllow;
    @SerializedName("hr_before_cancel")
    private String hrBeforeCancel;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("price_start_option")
    private String priceStartOption;
    @SerializedName("zip")
    private String mZip;

    @SerializedName("reshedule_status")
    private String resheduleStatus;

    public ConfirmModel() {
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getResheduleStatus() {
        return resheduleStatus;
    }

    public void setResheduleStatus(String resheduleStatus) {
        this.resheduleStatus = resheduleStatus;
    }

    public String getmEmployeeId() {
        return mEmployeeId;
    }

    public void setmEmployeeId(String mEmployeeId) {
        this.mEmployeeId = mEmployeeId;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getBookingTime() {
        return mBookingTime;
    }

    public void setBookingTime(String bookingTime) {
        mBookingTime = bookingTime;
    }

    public String getBusinessName() {
        return mBusinessName;
    }

    public void setBusinessName(String businessName) {
        mBusinessName = businessName;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getMerchantId() {
        return mMerchantId;
    }

    public void setMerchantId(String merchantId) {
        mMerchantId = merchantId;
    }

    public String getServiceName() {
        return mServiceName;
    }

    public void setServiceName(String serviceName) {
        mServiceName = serviceName;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getTotalMinutes() {
        return mTotalMinutes;
    }

    public void setTotalMinutes(String totalMinutes) {
        mTotalMinutes = totalMinutes;
    }

    public String getTotalPrice() {
        return mTotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        mTotalPrice = totalPrice;
    }

    public String getCatName() {
        return mCatName;
    }

    public void setCatName(String catName) {
        mCatName = catName;
    }


    public String getZip() {
        return mZip;
    }

    public void setZip(String zip) {
        mZip = zip;
    }

    public String getCancelBookingAllow() {
        return cancelBookingAllow;
    }

    public void setCancelBookingAllow(String cancelBookingAllow) {
        this.cancelBookingAllow = cancelBookingAllow;
    }

    public String getHrBeforeCancel() {
        return hrBeforeCancel;
    }

    public void setHrBeforeCancel(String hrBeforeCancel) {
        this.hrBeforeCancel = hrBeforeCancel;
    }
    public String getPriceStartOption() {
        return priceStartOption;
    }

    public void setPriceStartOption(String priceStartOption) {
        this.priceStartOption = priceStartOption;
    }
}
