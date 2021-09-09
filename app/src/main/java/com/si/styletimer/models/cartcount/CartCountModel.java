
package com.si.styletimer.models.cartcount;

import com.google.gson.annotations.SerializedName;


public class CartCountModel {

    @SerializedName("access_token")
    private String mAccessToken;
    @SerializedName("data")
    private CartCountData mData;
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

    public CartCountData getData() {
        return mData;
    }

    public void setData(CartCountData data) {
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
        return "CartCountModel{" +
                "mAccessToken='" + mAccessToken + '\'' +
                ", mData=" + mData +
                ", mResponseMessage='" + mResponseMessage + '\'' +
                ", mStatus=" + mStatus +
                '}';
    }
}
