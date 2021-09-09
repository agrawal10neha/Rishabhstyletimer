
package com.si.styletimer.models.receiptdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReceiptModel {

    @SerializedName("status")
    @Expose
    private long status;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("response_message")
    @Expose
    private String responseMessage;
    @SerializedName("data")
    @Expose
    private ReceiptData data;

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public ReceiptData getData() {
        return data;
    }

    public void setData(ReceiptData data) {
        this.data = data;
    }

}
