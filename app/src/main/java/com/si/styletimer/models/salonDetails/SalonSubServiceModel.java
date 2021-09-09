
package com.si.styletimer.models.salonDetails;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class SalonSubServiceModel extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private String mId;
    @SerializedName("buffer_time")
    private String mBufferTime;
    @SerializedName("category_id")
    private String mCategoryId;
    @SerializedName("category_name")
    private String mCategoryName;
    @SerializedName("created_by")
    private String mCreatedBy;
    @SerializedName("created_on")
    private String mCreatedOn;
    @SerializedName("discount_percent")
    private String mDiscountPercent;
    @SerializedName("discount_price")
    private String mDiscountPrice;
    @SerializedName("duration")
    private String mDuration;
    @SerializedName("in_cart")
    private String mInCart;
    @SerializedName("ip_address")
    private String mIpAddress;
    @SerializedName("name")
    private String mName;
    @SerializedName("parent_service_id")
    private String mParentServiceId;
    @SerializedName("price")
    private String mPrice;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("subcategory_id")
    private String mSubcategoryId;
    @SerializedName("subcatid")
    private String mSubcatid;
    @SerializedName("updated_by")
    private String mUpdatedBy;
    @SerializedName("updated_on")
    private String mUpdatedOn;
    @SerializedName("isopen")
    private String mIsOpen = "0";
    @SerializedName("main_categoryid")
    private String mainCategoryid;
    @SerializedName("price_start_option")
    private String priceStartOption;

    public String getPriceStartOption() {
        return priceStartOption;
    }

    public void setPriceStartOption(String priceStartOption) {
        this.priceStartOption = priceStartOption;
    }

    public String getMainCategoryid() {
        return mainCategoryid;
    }

    public void setMainCategoryid(String mainCategoryid) {
        this.mainCategoryid = mainCategoryid;
    }

    public String getIsOpen() {
        return mIsOpen;
    }

    public void setIsOpen(String mIsOpen) {
        this.mIsOpen = mIsOpen;
    }

    public String getBufferTime() {
        return mBufferTime;
    }

    public void setBufferTime(String bufferTime) {
        mBufferTime = bufferTime;
    }

    public String getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(String categoryId) {
        mCategoryId = categoryId;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        mCategoryName = categoryName;
    }

    public String getCreatedBy() {
        return mCreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        mCreatedBy = createdBy;
    }

    public String getCreatedOn() {
        return mCreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        mCreatedOn = createdOn;
    }

    public String getDiscountPercent() {
        return mDiscountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        mDiscountPercent = discountPercent;
    }

    public String getDiscountPrice() {
        return mDiscountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        mDiscountPrice = discountPrice;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getInCart() {
        return mInCart;
    }

    public void setInCart(String inCart) {
        mInCart = inCart;
    }

    public String getIpAddress() {
        return mIpAddress;
    }

    public void setIpAddress(String ipAddress) {
        mIpAddress = ipAddress;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getParentServiceId() {
        return mParentServiceId;
    }

    public void setParentServiceId(String parentServiceId) {
        mParentServiceId = parentServiceId;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getSubcategoryId() {
        return mSubcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        mSubcategoryId = subcategoryId;
    }

    public String getSubcatid() {
        return mSubcatid;
    }

    public void setSubcatid(String subcatid) {
        mSubcatid = subcatid;
    }

    public String getUpdatedBy() {
        return mUpdatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        mUpdatedBy = updatedBy;
    }

    public String getUpdatedOn() {
        return mUpdatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        mUpdatedOn = updatedOn;
    }

    @Override
    public String toString() {
        return "SalonValueModel{" +
                "mId='" + mId + '\'' +
                ", mBufferTime='" + mBufferTime + '\'' +
                ", mCategoryId='" + mCategoryId + '\'' +
                ", mCategoryName='" + mCategoryName + '\'' +
                ", mCreatedBy='" + mCreatedBy + '\'' +
                ", mCreatedOn='" + mCreatedOn + '\'' +
                ", mDiscountPercent='" + mDiscountPercent + '\'' +
                ", mDiscountPrice='" + mDiscountPrice + '\'' +
                ", mDuration='" + mDuration + '\'' +
                ", mInCart='" + mInCart + '\'' +
                ", mIpAddress='" + mIpAddress + '\'' +
                ", mName='" + mName + '\'' +
                ", mParentServiceId='" + mParentServiceId + '\'' +
                ", mPrice='" + mPrice + '\'' +
                ", mStatus='" + mStatus + '\'' +
                ", mSubcategoryId='" + mSubcategoryId + '\'' +
                ", mSubcatid='" + mSubcatid + '\'' +
                ", mUpdatedBy='" + mUpdatedBy + '\'' +
                ", mUpdatedOn='" + mUpdatedOn + '\'' +
                '}';
    }


//    @SerializedName("category_name")
//    private String mCategoryName;
//    @SerializedName("discount_price")
//    private String mDiscountPrice;
//    @SerializedName("duration")
//    private String mDuration;
//    @SerializedName("id")
//    private String mId;
//    @SerializedName("in_cart")
//    private String mInCart;
//    @SerializedName("name")
//    private String mName;
//    @SerializedName("price")
//    private String mPrice;
//    @SerializedName("subcategory_id")
//    private String mSubcategoryId;
//    @SerializedName("subcatid")
//    private String mSubcatid;
//
//    public String getCategoryName() {
//        return mCategoryName;
//    }
//
//    public void setCategoryName(String categoryName) {
//        mCategoryName = categoryName;
//    }
//
//    public String getDiscountPrice() {
//        return mDiscountPrice;
//    }
//
//    public void setDiscountPrice(String discountPrice) {
//        mDiscountPrice = discountPrice;
//    }
//
//    public String getDuration() {
//        return mDuration;
//    }
//
//    public void setDuration(String duration) {
//        mDuration = duration;
//    }
//
//    public String getId() {
//        return mId;
//    }
//
//    public void setId(String id) {
//        mId = id;
//    }
//
//    public String getInCart() {
//        return mInCart;
//    }
//
//    public void setInCart(String inCart) {
//        mInCart = inCart;
//    }
//
//    public String getName() {
//        return mName;
//    }
//
//    public void setName(String name) {
//        mName = name;
//    }
//
//    public String getPrice() {
//        return mPrice;
//    }
//
//    public void setPrice(String price) {
//        mPrice = price;
//    }
//
//    public String getSubcategoryId() {
//        return mSubcategoryId;
//    }
//
//    public void setSubcategoryId(String subcategoryId) {
//        mSubcategoryId = subcategoryId;
//    }
//
//    public String getSubcatid() {
//        return mSubcatid;
//    }
//
//    public void setSubcatid(String subcatid) {
//        mSubcatid = subcatid;
//    }
//
//
//    @Override
//    public String toString() {
//        return "SalonSubServiceModel{" +
//                "mCategoryName='" + mCategoryName + '\'' +
//                ", mDiscountPrice='" + mDiscountPrice + '\'' +
//                ", mDuration='" + mDuration + '\'' +
//                ", mId='" + mId + '\'' +
//                ", mInCart='" + mInCart + '\'' +
//                ", mName='" + mName + '\'' +
//                ", mPrice='" + mPrice + '\'' +
//                ", mSubcategoryId='" + mSubcategoryId + '\'' +
//                ", mSubcatid='" + mSubcatid + '\'' +
//                '}';
//    }
}
