
package com.si.styletimer.models.salonDetails;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class SalonDetailMainModel {

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
