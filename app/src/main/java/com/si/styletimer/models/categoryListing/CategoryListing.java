
package com.si.styletimer.models.categoryListing;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;


public class CategoryListing  {

    @SerializedName("about_salon")
    private String mAboutSalon;
    @SerializedName("favourite")
    private String mFavourite;
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
    @SerializedName("distance")
    private String mDistance;
    @SerializedName("first_name")
    private String mFirstName;
    @SerializedName("id")
    private String mId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("image1")
    private String mImage1;
    @SerializedName("image2")
    private String mImage2;
    @SerializedName("image3")
    private String mImage3;
    @SerializedName("image4")
    private String mImage4;
    @SerializedName("last_name")
    private String mLastName;
    @SerializedName("latitude")
    private String mLatitude;
    @SerializedName("longitude")
    private String mLongitude;
    @SerializedName("rating")
    private String mRating;
    @SerializedName("sercvices")
    private List<Sercvice> mSercvices;
    @SerializedName("total_review")
    private String mTotalReview;
    @SerializedName("zip")
    private String mZip;

    public String getmDistance() {
        return mDistance;
    }

    public void setmDistance(String mDistance) {
        this.mDistance = mDistance;
    }

    public String getmFavourite() {
        return mFavourite;
    }

    public void setmFavourite(String mFavourite) {
        this.mFavourite = mFavourite;
    }

    public String getAboutSalon() {
        return mAboutSalon;
    }

    public void setAboutSalon(String aboutSalon) {
        mAboutSalon = aboutSalon;
    }

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

    public String getDistance() {
        return mDistance;
    }

    public void setDistance(String distance) {
        mDistance = distance;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
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

    public String getImage1() {
        return mImage1;
    }

    public void setImage1(String image1) {
        mImage1 = image1;
    }

    public String getImage2() {
        return mImage2;
    }

    public void setImage2(String image2) {
        mImage2 = image2;
    }

    public String getImage3() {
        return mImage3;
    }

    public void setImage3(String image3) {
        mImage3 = image3;
    }

    public String getImage4() {
        return mImage4;
    }

    public void setImage4(String image4) {
        mImage4 = image4;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
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

    public String getRating() {
        return mRating;
    }

    public void setRating(String rating) {
        mRating = rating;
    }

    public List<Sercvice> getSercvices() {
        return mSercvices;
    }

    public void setSercvices(List<Sercvice> sercvices) {
        mSercvices = sercvices;
    }

    public String getTotalReview() {
        return mTotalReview;
    }

    public void setTotalReview(String totalReview) {
        mTotalReview = totalReview;
    }

    public String getZip() {
        return mZip;
    }

    public void setZip(String zip) {
        mZip = zip;
    }

    @Override
    public String toString() {
        return "CategoryListing{" +
                "mAboutSalon='" + mAboutSalon + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mBusinessName='" + mBusinessName + '\'' +
                ", mBusinessType='" + mBusinessType + '\'' +
                ", mCity='" + mCity + '\'' +
                ", mCountry='" + mCountry + '\'' +
                ", mDistance='" + mDistance + '\'' +
                ", mFirstName='" + mFirstName + '\'' +
                ", mId='" + mId + '\'' +
                ", mImage='" + mImage + '\'' +
                ", mImage1='" + mImage1 + '\'' +
                ", mImage2='" + mImage2 + '\'' +
                ", mImage3='" + mImage3 + '\'' +
                ", mImage4='" + mImage4 + '\'' +
                ", mLastName='" + mLastName + '\'' +
                ", mLatitude='" + mLatitude + '\'' +
                ", mLongitude='" + mLongitude + '\'' +
                ", mRating='" + mRating + '\'' +
                ", mSercvices=" + mSercvices +
                ", mTotalReview='" + mTotalReview + '\'' +
                ", mZip='" + mZip + '\'' +
                '}';
    }
}
