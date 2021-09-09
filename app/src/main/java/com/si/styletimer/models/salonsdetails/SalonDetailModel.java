
package com.si.styletimer.models.salonsdetails;

import com.google.gson.annotations.SerializedName;


public class SalonDetailModel {

    @SerializedName("access_token")
    private String mAccessToken;
    @SerializedName("data")
    private DataModel mData;
    @SerializedName("response_message")
    private String mResponseMessage;
    @SerializedName("status")
    private Long mStatus;

    public String getAccessToken() {
        return mAccessToken;
    }

    public void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
    }

    public DataModel getData() {
        return mData;
    }

    public void setData(DataModel data) {
        mData = data;
    }

    public String getResponseMessage() {
        return mResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        mResponseMessage = responseMessage;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

    @Override
    public String toString() {
        return "SalonDetailModel{" +
                "mAccessToken='" + mAccessToken + '\'' +
                ", mData=" + mData +
                ", mResponseMessage='" + mResponseMessage + '\'' +
                ", mStatus=" + mStatus +
                '}';
    }
}
