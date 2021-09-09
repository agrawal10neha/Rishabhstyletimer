
package com.si.styletimer.models.salonsdetails;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MatchSubcatModel {

    @SerializedName("key")
    private String mKey;
    @SerializedName("start_price")
    private String mStartPrice;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("value")
    private List<ValueModel> mValue;

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

    public List<ValueModel> getValue() {
        return mValue;
    }

    public void setValue(List<ValueModel> value) {
        mValue = value;
    }

    @Override
    public String toString() {
        return "MatchSubcatModel{" +
                "mKey='" + mKey + '\'' +
                ", mStartPrice='" + mStartPrice + '\'' +
                ", mStatus=" + mStatus +
                ", mValue=" + mValue +
                '}';
    }
}
