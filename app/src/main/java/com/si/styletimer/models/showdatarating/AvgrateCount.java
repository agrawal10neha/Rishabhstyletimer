
package com.si.styletimer.models.showdatarating;

import com.google.gson.annotations.SerializedName;


public class AvgrateCount {

    @SerializedName("avgrage")
    private String mAvgrage;
    @SerializedName("totalcount")
    private String mTotalcount;

    public String getAvgrage() {
        return mAvgrage;
    }

    public void setAvgrage(String avgrage) {
        mAvgrage = avgrage;
    }

    public String getTotalcount() {
        return mTotalcount;
    }

    public void setTotalcount(String totalcount) {
        mTotalcount = totalcount;
    }

}
