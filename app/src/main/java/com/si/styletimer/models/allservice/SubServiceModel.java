
package com.si.styletimer.models.allservice;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class SubServiceModel {

    @SerializedName("key")
    private String mKey;
    @SerializedName("start_price")
    private String mStartPrice;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("value")
    private List<AllServiceValueModel> mValue;
    @SerializedName("is_open")
    private Long mIsopen;

    public Long getmIsopen() {
        return mIsopen;
    }

    public void setmIsopen(Long mIsopen) {
        this.mIsopen = mIsopen;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getStartPrice() {
        return mStartPrice;
    }

    public void setStartPrice(String startPrice) {
        mStartPrice = startPrice;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

    public List<AllServiceValueModel> getValue() {
        return mValue;
    }

    public void setValue(List<AllServiceValueModel> value) {
        mValue = value;
    }

    @Override
    public String toString() {
        return "SubServiceModel{" +
                "mKey='" + mKey + '\'' +
                ", mStartPrice='" + mStartPrice + '\'' +
                ", mStatus=" + mStatus +
                ", mValue=" + mValue +
                '}';
    }
}
