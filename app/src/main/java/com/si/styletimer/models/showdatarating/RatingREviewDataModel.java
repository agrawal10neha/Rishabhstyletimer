
package com.si.styletimer.models.showdatarating;

import com.google.gson.annotations.SerializedName;


public class RatingREviewDataModel {

    @SerializedName("access_token")
    private String mAccessToken;
    @SerializedName("data")
    private Data mData;
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

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
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

}
