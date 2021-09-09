
package com.si.styletimer.models.fav_list_model;

import java.util.List;

import com.google.gson.annotations.SerializedName;
public class FavouriteListModel {

    @SerializedName("address")
    private String mAddress;
    @SerializedName("business_name")
    private String mBusinessName;
    @SerializedName("business_type")
    private String mBusinessType;
    @SerializedName("city")
    private String mCity;
    @SerializedName("country")
    private String mCountry;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("id")
    private String mId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("latitude")
    private String mLatitude;
    @SerializedName("longitude")
    private String mLongitude;
    @SerializedName("mobile")
    private String mMobile;
    @SerializedName("salon_id")
    private String mSalonId;
    @SerializedName("sercvices")
    private List<Sercvice> mSercvices;
    @SerializedName("zip")
    private String mZip;

    @SerializedName("rating")
    private String mRating;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getBusinessName() {
        return mBusinessName;
    }

    public void setBusinessName(String businessName) {
        mBusinessName = businessName;
    }

    public String getBusinessType() {
        return mBusinessType;
    }

    public void setBusinessType(String businessType) {
        mBusinessType = businessType;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
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

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public String getMobile() {
        return mMobile;
    }

    public void setMobile(String mobile) {
        mMobile = mobile;
    }

    public String getSalonId() {
        return mSalonId;
    }

    public void setSalonId(String salonId) {
        mSalonId = salonId;
    }

    public List<Sercvice> getSercvices() {
        return mSercvices;
    }

    public void setSercvices(List<Sercvice> sercvices) {
        mSercvices = sercvices;
    }

    public String getZip() {
        return mZip;
    }

    public void setZip(String zip) {
        mZip = zip;
    }

    public String getRating() {
        return mRating;
    }

    public void setRating(String Rating) {
        mRating = Rating;
    }


}
